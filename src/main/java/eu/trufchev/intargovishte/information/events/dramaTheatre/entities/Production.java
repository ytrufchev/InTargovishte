package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Production {
    private String id;
    private int length;
    private int minAgeRestriction;
    private Photo photos;
    private List<TagRef> tagRefs;
    private String title;
    private List<UpcomingEvent> upcomingEvents;
}
