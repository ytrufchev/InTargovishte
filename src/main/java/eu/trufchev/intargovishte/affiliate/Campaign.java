package eu.trufchev.intargovishte.affiliate;

import java.util.List;
import java.util.Map;

record CampaignsResponse(CampaignsResult result) {}

record CampaignsResult(Paginator paginator, List<Campaign> campaigns) {}

record Paginator(int itemsPerPage, int currentPage, int totalPages) {}

public record Campaign(
        int id,
        String name,
        String commissionType,
        String startDate,
        String endDate,
        String url,
        Map<String, Banner> banners
) {}

record Banner(int width, int height, String src) {}
