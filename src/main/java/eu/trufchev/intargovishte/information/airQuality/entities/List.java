package eu.trufchev.intargovishte.information.airQuality.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class List {
    private Main main;
    private Component components;
    @Column(name = "dt")
    private Long dt;
}
