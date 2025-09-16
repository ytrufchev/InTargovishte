package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// In ReferralLinkService.java
import eu.trufchev.intargovishte.affiliate.AffiliateLinkRequest;

@Service
@RequiredArgsConstructor
public class ReferralLinkService {

    private final ProfitshareLinkClient linkClient;

    public String createReferralLink(String campaignUrl) {
        AffiliateLinkRequest request = new AffiliateLinkRequest("Pazaruvai Lesno", campaignUrl);
        ProfitshareDtos.ReferralLinkResponse response = linkClient.createLink(request);
        return response.result().get(0).ps_url();
    }
}