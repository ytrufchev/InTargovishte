package eu.trufchev.intargovishte.information.fuelo.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceResponse {
    private String status;
    private double lat;
    private double lon;
    private int limit;
    private int num_results;
    List<GasStation> gasstations;
}