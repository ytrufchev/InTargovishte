package eu.trufchev.intargovishte.information.vikOutage.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VikOutage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "date")
    private String date;
    @Column(name = "startTime")
    private String startTime;
    @Column(name  = "endTime")
    private String endTime;
    @Column(name = "description", length = 500)
    private String description;
}
