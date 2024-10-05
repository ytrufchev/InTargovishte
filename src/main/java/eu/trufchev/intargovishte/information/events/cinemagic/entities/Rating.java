package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;
    private @Setter @Getter String cinemaGroupId;
    private  @Getter @Setter String symbol;
    private @Getter @Setter String value;
    private @Getter @Setter String description;
}
