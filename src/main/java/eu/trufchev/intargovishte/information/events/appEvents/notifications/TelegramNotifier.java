package eu.trufchev.intargovishte.information.events.appEvents.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotifier {

    private final NotificationClient notificationClient;

    @Value("${app.botToken}")
    private String botToken;

    @Value("${app.chatId}")
    private String chatId;

    public TelegramNotifier(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendNotification(String message) {
        try {
            String response = notificationClient.sendMessage("bot" + botToken, chatId, message);
            System.out.println("Notification sent successfully: " + response);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
}
