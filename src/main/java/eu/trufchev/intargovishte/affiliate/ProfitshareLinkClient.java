package eu.trufchev.intargovishte.affiliate;

// In ProfitshareLinkClient.java
import eu.trufchev.intargovishte.affiliate.AffiliateLinkRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "profitshareLinks",
        url = "${profitshare.api-url}",
        configuration = ProfitshareFeignConfig.class
)
public interface ProfitshareLinkClient {
    @PostMapping(value = "/affiliate-links/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ProfitshareDtos.ReferralLinkResponse createLink(@RequestBody AffiliateLinkRequest request);
}