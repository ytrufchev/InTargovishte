package eu.trufchev.intargovishte.information.fuelo.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Embeddable
public class FuelPrice {
    private String fuelType;
    private Double price;

    // Constructor, getters, and setters

    public FuelPrice(String fuelType, Double price) {
        this.fuelType = fuelType;
        this.price = price;
    }

    // toString method
}