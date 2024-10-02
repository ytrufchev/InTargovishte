package eu.trufchev.InTargovishte.information.fuelo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class GasStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @ElementCollection
    @CollectionTable(name = "fuel_prices", joinColumns = @JoinColumn(name = "gas_station_id"))
    @Column(name = "fuel_price")
    private List<FuelPrice> fuelPrices;

    // Constructor, getters, and setters

    public GasStation(String name, String address, List<FuelPrice> fuelPrices) {
        this.name = name;
        this.address = address;
        this.fuelPrices = fuelPrices;
    }

    // toString method to display the data
}
