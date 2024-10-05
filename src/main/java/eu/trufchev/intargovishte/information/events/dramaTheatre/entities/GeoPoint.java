package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeoPoint {
    private String type;
    private List<Double> coordinates;
}