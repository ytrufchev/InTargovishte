package eu.trufchev.intargovishte.information.roadConditions.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class RoadConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "endDate")
    private String endDate;
    @Column(name = "description", length = 1000)
    private String description;
}
