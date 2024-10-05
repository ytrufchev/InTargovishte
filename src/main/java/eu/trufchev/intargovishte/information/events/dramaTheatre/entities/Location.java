package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location {
    private String address;
    private String cityName;
    private String countryCode;
    private String countryName;
    private double lat;
    private double lng;
    private String placeName;
    private String postCode;
    private String timezone;
}