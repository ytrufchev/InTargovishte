package eu.trufchev.intargovishte.information.inAppInformation.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notificationClient", url = "https://api.telegram.org")
public interface NotificationClient {

    @GetMapping(value = "/{token}/sendMessage")
    String sendMessage(
            @PathVariable("token") String token,
            @RequestParam("chat_id") String chatId,
            @RequestParam("text") String text
    );
}
