package eu.trufchev.intargovishte.information.airQuality.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class Main {
    @Column(name = "aqi")
    private String aqi;
}
