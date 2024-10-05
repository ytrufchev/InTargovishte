package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpcomingEvent {
    private String EventId;
    private GeoPoint geoPoint;
    private Location location;
}
