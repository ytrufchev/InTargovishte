package eu.trufchev.intargovishte.information.events.appEvents.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEventDTO {
    private Long id;
    private String title;
    private String content;
    private Long date; // Combined date and time
    private String location;
    private String image; // Base64 image string
    private Long userId;
    private Long likesCount;
    private boolean likedByCurrentUser;
}
