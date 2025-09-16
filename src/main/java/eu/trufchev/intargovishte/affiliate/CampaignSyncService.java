package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignSyncService {

    private final CampaignService campaignService;
    private final CampaignRepository repository;
    private final ReferralLinkService referralLinkService;

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

            // **NEW LOGIC HERE**
            // Safely extract the banner URL from the campaign DTO
            String bannerUrl = null;
            Map<String, Banner> banners = campaign.getBanners();
            if (banners != null && !banners.isEmpty()) {
                // Get the URL from the 'src' field of the first Banner in the map
                bannerUrl = banners.values().iterator().next().getSrc();
            }
            entity.setBannerUrl(bannerUrl); // Set the banner URL on the entity

            repository.save(entity);
        }

        // Remove expired campaigns
        repository.findAll().stream()
                .filter(c -> c.getEndDate() != null && c.getEndDate().isBefore(LocalDateTime.now()))
                .forEach(c -> repository.delete(c));
    }
}