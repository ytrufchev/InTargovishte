package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDTO {
    private @Getter @Setter String id;
    private @Getter @Setter int duration;
    private @Getter @Setter String description;
    private @Getter @Setter String title;
    private @Getter @Setter String originalTitle;
    private @Getter @Setter Boolean isForChildren;
    private @Getter @Setter String imdbId;
    private @Getter @Setter List<Projections> projections;
    private Long likesCount;
    private boolean likedByCurrentUser;
}
