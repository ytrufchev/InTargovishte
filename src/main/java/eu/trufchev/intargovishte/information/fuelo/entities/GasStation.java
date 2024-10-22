package eu.trufchev.intargovishte.information.fuelo.entities;

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
    @Column(name = "last_updated")
    private long lastUpdated;
    // Constructor, getters, and setters

    public GasStation(String name, String address, List<FuelPrice> fuelPrices) {
        this.name = name;
        this.address = address;
        this.fuelPrices = fuelPrices;
    }

    public void setFuelPrices(String fuelType, Double price) {
        setFuelPrices(fuelType, price);
    }

    // toString method to display the data
}
