package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Genre {
    @Id
    private @Getter @Setter String id;
    private @Getter @Setter String name;
    private @Getter @Setter String description;

}
