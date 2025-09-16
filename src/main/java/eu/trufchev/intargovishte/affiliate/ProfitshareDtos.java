package eu.trufchev.intargovishte.affiliate;

import java.util.List;
import java.util.Map;

public class ProfitshareDtos {

    // ===== Campaigns =====
    public record CampaignsResponse(CampaignsResult result) {}

    public record CampaignsResult(Paginator paginator, List<Campaign> campaigns) {}

    public record Paginator(int itemsPerPage, int currentPage, int totalPages) {}

    public record Campaign(
            int id,
            String name,
            String commissionType,
            String startDate,
            String endDate,
            String url,
            Map<String, Banner> banners
    ) {}

    public record Banner(int width, int height, String src) {}

    // ===== Referral Links =====
    // Fixed: The API returns an array of link objects, not a single result
    public record ReferralLinkResponse(List<ReferralLink> result) {}

    public record ReferralLink(String name, String url, String ps_url) {}
}