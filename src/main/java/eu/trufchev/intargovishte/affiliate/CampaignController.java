package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/campaigns/square")
    public List<Campaign> getSquareCampaigns() {
        return campaignService.getSquareCampaigns();
    }
}
