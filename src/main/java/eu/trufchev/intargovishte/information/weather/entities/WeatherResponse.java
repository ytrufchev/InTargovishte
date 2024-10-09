package eu.trufchev.intargovishte.information.weather.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class WeatherResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "queryCost")
    private int queryCost;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name  = "resolvedAddress")
    private String resolvedAddress;
    @Column(name = "address")
    private String address;
    @Column(name = "timezone")
    private String timezone;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_response_id")
    private List<Day> days;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name = "id")  // Map by station ID
    private Map<String, Station> stations;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "current_conditions_id")
    private CurrentConditions currentConditions;
}
