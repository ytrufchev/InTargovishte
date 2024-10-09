package eu.trufchev.intargovishte.information.weather.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Station {

    @Id
    @Column(name = "station_id")
    private String id;  // Primary key for Station (use the station's unique ID)

    private double distance;
    private double latitude;
    private double longitude;
    private int useCount;
    private String name;
    private double quality;
    private int contribution;
}
