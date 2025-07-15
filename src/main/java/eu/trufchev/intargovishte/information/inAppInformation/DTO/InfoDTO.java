package eu.trufchev.intargovishte.information.inAppInformation.DTO;

public class InfoDTO {
    private String title;
    private String description;
    private Long endDate;
    private String image; // Base64 image string
    private Long userId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getEndDate() { return endDate; }
    public void setEndDate(Long endDate) { this.endDate = endDate; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
