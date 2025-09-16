package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignRepository campaignRepository;

    @GetMapping("/campaigns/square")
    public List<Campaign> getSquareCampaigns() {
        return campaignService.getSquareCampaigns();
    }
    @GetMapping("/campaigns/active")
    public List<CampaignEntity> getActiveCampaignsFromDb() {
        // Fetch all campaigns that are marked as active
        return campaignRepository.findAll().stream()
                .filter(CampaignEntity::isActive)
                .toList();
    }
}
