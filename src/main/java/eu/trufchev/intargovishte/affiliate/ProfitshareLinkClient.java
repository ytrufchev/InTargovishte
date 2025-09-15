package eu.trufchev.intargovishte.affiliate;

import eu.trufchev.intargovishte.affiliate.ProfitshareDtos.ReferralLinkRequest;
import eu.trufchev.intargovishte.affiliate.ProfitshareDtos.ReferralLinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profitshareLinks", url = "${profitshare.api-url}", configuration = ProfitshareFeignConfig.class)
public interface ProfitshareLinkClient {
    @PostMapping("/affiliate-links/")
    ReferralLinkResponse createLink(@RequestBody ReferralLinkRequest request);
}
