package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ReferralLinkService {

    private final ProfitshareLinkClient linkClient;

    public String createReferralLink(String campaignUrl) {
        // Create the LinkData object from the provided URL
        ProfitshareDtos.LinkData linkData = new ProfitshareDtos.LinkData(null, campaignUrl);

        // Create the ReferralLinkRequest with a list containing the single LinkData object
        ProfitshareDtos.ReferralLinkRequest request = new ProfitshareDtos.ReferralLinkRequest(Collections.singletonList(linkData));

        // Call the Feign client with the correctly formatted request object
        ProfitshareDtos.ReferralLinkResponse response = linkClient.createLink(request);

        return response.result().short_link(); // Return the short link
    }
}