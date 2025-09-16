package eu.trufchev.intargovishte.affiliate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "profitshareCampaigns", url = "${profitshare.api-url}", configuration = ProfitshareFeignConfig.class)
public interface ProfitshareCampaignClient {

    @GetMapping("/affiliate-campaigns/")
    CampaignsResponse getCampaigns(@RequestParam(value = "page", required = false) Integer page);
}
