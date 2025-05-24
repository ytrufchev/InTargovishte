package eu.trufchev.intargovishte.information.events.municipality.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MunicipalityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "title")
    private String title;
    @Column(name = "description", length = 5000)
    private String description;
}