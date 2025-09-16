package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignSyncService {

    private final CampaignService campaignService; // Fetches from Profitshare
    private final CampaignRepository repository;
    private final ReferralLinkService referralLinkService; // We'll create this next

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void syncCampaigns() {
        List<Campaign> profitshareCampaigns = campaignService.getSquareCampaigns();

        // Mark all existing campaigns inactive first
        repository.findAll().forEach(c -> {
            c.setActive(false);
            repository.save(c);
        });

        for (Campaign campaign : profitshareCampaigns) {
            LocalDateTime start = LocalDateTime.parse(campaign.getStartDate(), formatter);
            LocalDateTime end = LocalDateTime.parse(campaign.getEndDate(), formatter);

            // Skip campaigns that are already expired
            if (end.isBefore(LocalDateTime.now())) continue;

            CampaignEntity entity = repository.findById(campaign.getId())
                    .orElseGet(() -> {
                        // First time we see this campaign -> create referral link
                        String referralLink = referralLinkService.createReferralLink(campaign.getUrl());
                        return CampaignEntity.builder()
                                .id(campaign.getId())
                                .referralLink(referralLink)
                                .build();
                    });

            entity.setName(campaign.getName());
            entity.setCommissionType(campaign.getCommissionType());
            entity.setStartDate(start);
            entity.setEndDate(end);
            entity.setUrl(campaign.getUrl());
            entity.setActive(true);

            repository.save(entity);
        }

        // Remove expired campaigns
        repository.findAll().stream()
                .filter(c -> c.getEndDate() != null && c.getEndDate().isBefore(LocalDateTime.now()))
                .forEach(c -> repository.delete(c));
    }
}
