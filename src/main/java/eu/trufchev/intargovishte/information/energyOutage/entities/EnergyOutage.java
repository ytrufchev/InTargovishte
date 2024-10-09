package eu.trufchev.intargovishte.information.energyOutage.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class EnergyOutage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "location_period")
    private String location_period;
    @Column(name = "location_text", length = 1000)
    private String location_text;
}