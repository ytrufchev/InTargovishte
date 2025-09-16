package eu.trufchev.intargovishte.affiliate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "profitshareLinks",
        url = "${profitshare.api-url}",
        configuration = ProfitshareFeignConfig.class // <-- Add this line
)
public interface ProfitshareLinkClient {
    @PostMapping(value = "/affiliate-links/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ProfitshareDtos.ReferralLinkResponse createLink(@RequestBody MultiValueMap<String, String> formData);
}