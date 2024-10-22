package eu.trufchev.intargovishte.information.fuelo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FuelStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @ElementCollection
    @CollectionTable(name = "fuel_prices", joinColumns = @JoinColumn(name = "station_id"))
    private List<FuelPrice> fuelPrices;

    public FuelStation(String name, String address, List<FuelPrice> fuelPrices) {
        this.name = name;
        this.address = address;
        this.fuelPrices = fuelPrices;
    }
}
