package eu.trufchev.intargovishte.information.vikOutage.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vikOutageClient", url = "http://viktg.com", configuration = FeignConfiguration.class)
public interface VikOutageClient {

    @GetMapping("/info/info")
    String getOutage(@RequestParam("tid") String tid, @RequestParam("id") String id, @RequestParam("idc") String idc);

}
