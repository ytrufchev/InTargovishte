package eu.trufchev.intargovishte.information.weather.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key for Day

    private String dateTime;
    private long datetimeEpoch;
    private double tempmax;
    private double tempmin;
    private double temp;
    private double feelslikemax;
    private double feelslikemin;
    private double dew;
    private double humidity;
    private double precip;
    private double precipprob;
    private double precipcover;

    @ElementCollection  // For storing a list of simple types (like Strings)
    private List<String> preciptype;

    private double snow;
    private double snowdepth;
    private double windgust;
    private double windspeed;
    private double winddir;
    private double pressure;
    private double cloudcover;
    private double visibility;
    private double solarradiation;
    private double solarenergy;
    private double uvindex;
    private double severerisk;
    private String sunrise;
    private long sunriseEpoch;
    private String sunset;
    private long sunsetEpoch;
    private double moonphase;
    private String conditions;
    private String description;
    private String icon;

    @ElementCollection
    @CollectionTable(name = "day_stations", joinColumns = @JoinColumn(name = "day_id"))
    @Column(name = "station_id")
    private List<String> stations;

    private String source;

    // Add the foreign key relationship to WeatherResponse
    @ManyToOne
    @JoinColumn(name = "weather_response_id")  // Foreign key in Day table referencing WeatherResponse
    private WeatherResponse weatherResponse;
}