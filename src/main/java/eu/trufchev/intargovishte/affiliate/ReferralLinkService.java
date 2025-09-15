package eu.trufchev.intargovishte.affiliate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class ReferralLinkService {

    private final ProfitshareLinkClient linkClient;

    public String createReferralLink(String campaignUrl) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("0[name]", "Pazaruvai Lesno");
        formData.add("0[url]", campaignUrl);

        ProfitshareDtos.ReferralLinkResponse response = linkClient.createLink(formData);
        return response.result().short_link();
    }
}