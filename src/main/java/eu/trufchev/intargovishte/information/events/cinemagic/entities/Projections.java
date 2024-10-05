package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Projections {
    @Id
    private @Getter @Setter String id;
    private @Getter @Setter String screenId;
    private @Getter @Setter String screeningTimeFrom;
    private @Getter @Setter String screeningTimeTo;
    private @Getter @Setter int audience;
    private @Getter @Setter int maxOccupancy;
    private @Getter @Setter String movieId;
    private @Getter @Setter String printType;
    private @Getter @Setter String language;
    private @Getter @Setter String subtitles;
}
