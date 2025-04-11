package eu.trufchev.intargovishte.information.inAppInformation.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfoTelegramNotifier {

    private final InfoNotificationClient notificationClient;

    @Value("${app.botToken}")
    private String botToken;

    @Autowired
    public InfoTelegramNotifier(InfoNotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendNotification(String message) {
        try {
            String chatId = "6405221088";
            String response = notificationClient.sendMessage("bot" + botToken, chatId, message);
            System.out.println("Notification sent successfully: " + response);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
}
