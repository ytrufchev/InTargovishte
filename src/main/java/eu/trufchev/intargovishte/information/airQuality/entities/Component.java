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
public class Component {
    @Column(name = "co")
    private double co;
    @Column(name = "no")
    private double no;
    @Column(name = "no2")
    private double no2;
    @Column(name = "o3")
    private double o3;
    @Column(name = "so2")
    private double so2;
    @Column(name = "pm2_5")
    private double pm2_5;
    @Column(name = "pm10")
    private double pm10;
    @Column(name = "nh3")
    private double nh3;
}
