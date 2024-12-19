package eu.trufchev.intargovishte.information.events.dramaTheatre.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayDTO {
    private Long id;
    private String title;
    private String length;
    private String minAgeRestriction;
    private String largePhoto;
    private String placeName;
    private String startDates;

}
