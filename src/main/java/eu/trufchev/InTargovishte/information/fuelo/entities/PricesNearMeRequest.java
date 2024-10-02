package eu.trufchev.InTargovishte.information.fuelo.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PricesNearMeRequest {
    private String key;
    private double lat;
    private double lon;
    private String fuel;
    private int limit;
    private int distance;
}