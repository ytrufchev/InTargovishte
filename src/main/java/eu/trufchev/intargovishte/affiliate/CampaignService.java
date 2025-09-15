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
            if (response == null || response.getResult() == null) break;

            List<Campaign> campaigns = response.getResult().getCampaigns();
            if (campaigns != null) {
                all.addAll(campaigns.stream()
                        .filter(c -> c.getBanners() != null && hasSquareBanner(c.getBanners()))
                        .toList());
            }

            page++;
        } while (page <= response.getResult().getPaginator().getTotalPages());

        return all;
    }

    private boolean hasSquareBanner(Map<String, Banner> banners) {
        return banners.values().stream()
                .anyMatch(b -> Math.abs(b.getWidth() - b.getWidth()) < 30); // "square-ish"
    }
}
