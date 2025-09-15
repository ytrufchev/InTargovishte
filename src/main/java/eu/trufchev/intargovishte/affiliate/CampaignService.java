package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final ProfitshareCampaignClient client;

    public List<Campaign> getSquareCampaigns() {
        List<Campaign> all = new ArrayList<>();
        int page = 1;
        CampaignsResponse response;

        do {
            response = client.getCampaigns(page);
            if (response == null || response.result() == null) break;

            List<Campaign> campaigns = response.result().campaigns();
            if (campaigns != null) {
                all.addAll(campaigns.stream()
                        .filter(c -> c.banners() != null && hasSquareBanner(c.banners()))
                        .toList());
            }

            page++;
        } while (page <= response.result().paginator().totalPages());

        return all;
    }

    private boolean hasSquareBanner(Map<String, Banner> banners) {
        return banners.values().stream()
                .anyMatch(b -> Math.abs(b.width() - b.height()) < 30); // "square-ish"
    }
}
