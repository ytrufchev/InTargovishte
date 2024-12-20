package eu.trufchev.intargovishte.information.events.appEvents.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "notificationClient", url = "https://api.telegram.org")
public interface NotificationClient {

    @GetMapping(value = "/bot{token}/sendMessage")
    String sendMessage(
            @PathVariable("token") String token,
            @RequestParam("chat_id") String chatId,
            @RequestParam("text") String text
    );
}
