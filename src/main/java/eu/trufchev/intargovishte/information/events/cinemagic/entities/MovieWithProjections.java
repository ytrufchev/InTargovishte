package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieWithProjections {
    private @Getter @Setter String id;
    private @Getter @Setter int duration;
    private @Getter @Setter String description;
    private @Getter @Setter String title;
    private @Getter @Setter String originalTitle;
    private @Getter @Setter Boolean isForChildren;
    private @Getter @Setter String imdbId;
    private @Getter @Setter List<Projections> projections;
}
