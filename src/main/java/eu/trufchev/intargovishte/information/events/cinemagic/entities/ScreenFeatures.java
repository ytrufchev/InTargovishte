package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScreenFeatures {
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;

    private @Getter @Setter String feature;
}
