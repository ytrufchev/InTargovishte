package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferralLinkService {

    private final ProfitshareLinkClient linkClient;

    public String createReferralLink(String campaignUrl) {
        ProfitshareDtos.ReferralLinkResponse response = linkClient.createLink(new ProfitshareDtos.ReferralLinkRequest(campaignUrl));
        return response.result().short_link(); // Store the short link
    }
}
