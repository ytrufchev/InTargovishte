package eu.trufchev.intargovishte.information.events.appEvents.dto;

import java.util.Date;

public class EventDTO {
    private String title;
    private String content;
    private Long date; // Combined date and time
    private String location;
    private String image; // Base64 image string
    private Long userId;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getDate() { return date; }
    public void setDate(Long date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
