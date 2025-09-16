package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CampaignSyncScheduler {

    private final CampaignSyncService syncService;

    // Run every day at 3:00 AM
    @Scheduled(cron = "0 0 3 * * *")
    public void runDailySync() {
        syncService.syncCampaigns();
    }
}

