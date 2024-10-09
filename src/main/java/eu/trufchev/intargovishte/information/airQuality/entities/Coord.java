package eu.trufchev.intargovishte.information.airQuality.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class Coord {
    @Column(name = "lon")
    private double lon;
    @Column(name = "lat")
    private double lat;

}
