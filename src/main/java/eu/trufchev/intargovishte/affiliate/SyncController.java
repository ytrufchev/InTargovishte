package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final CampaignSyncService campaignSyncService;

    @PostMapping("/run")
    public String triggerSync() {
        campaignSyncService.syncCampaigns();
        return "Campaign synchronization triggered successfully. Check logs for details.";
    }
}