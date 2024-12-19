package eu.trufchev.intargovishte.information.events.dramaTheatre.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayDTO {
    private String id;
    private String title;
    private int length;
    private int minAgeRestriction;
    private String largePhoto;
    private String placeName;
    private List<String> startDates;
    private Long likesCount;
    private boolean likedByCurrentUser;
}
