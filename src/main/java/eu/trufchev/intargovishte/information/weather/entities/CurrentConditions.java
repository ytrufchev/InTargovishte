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
public class CurrentConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "datetime_epoch", unique = true)
    private long datetimeEpoch;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "temperature")
    private double temp;

    @Column(name = "feels_like")
    private double feelsLike;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "dew_point")
    private double dew;

    @Column(name = "precipitation")
    private double precip;

    @Column(name = "precip_probability")
    private double precipprob;

    @Column(name = "snow")
    private double snow;

    @Column(name = "snow_depth")
    private double snowdepth;

    // Handle the List<String> preciptype as an ElementCollection
    @ElementCollection
    @CollectionTable(name = "precip_types", joinColumns = @JoinColumn(name = "current_conditions_id"))
    @Column(name = "precip_type")
    private List<String> preciptype;

    @Column(name = "wind_gust")
    private double windgust;

    @Column(name = "wind_speed")
    private double windspeed;

    @Column(name = "wind_direction")
    private double winddir;

    @Column(name = "pressure")
    private double pressure;

    @Column(name = "visibility")
    private double visibility;

    @Column(name = "cloud_cover")
    private double cloudcover;

    @Column(name = "solar_radiation")
    private double solarradiation;

    @Column(name = "solar_energy")
    private double solarenergy;

    @Column(name = "uv_index")
    private double uvindex;

    @Column(name = "conditions")
    private String conditions;

    @Column(name = "icon")
    private String icon;

    @Column(name = "source")
    private String source;

    @Column(name = "sunrise")
    private String sunrise;

    @Column(name = "sunrise_epoch")
    private long sunriseEpoch;

    @Column(name = "sunset")
    private String sunset;

    @Column(name = "sunset_epoch")
    private long sunsetEpoch;

    @Column(name = "moon_phase")
    private double moonphase;

    @ElementCollection
    @CollectionTable(name = "current_conditions_stations", joinColumns = @JoinColumn(name = "current_conditions_id"))
    @Column(name = "station_id")
    private List<String> stations;
}
