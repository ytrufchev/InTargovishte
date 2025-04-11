package eu.trufchev.intargovishte.information.inAppInformation.entity;

import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Information {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private long id;
        @Column(name = "title")
        private String title;
        @Column(name  = "validTo")
        private String validTo;
        @Column(name = "description", length = 10000)
        private String description;
        @Column(name = "publisher")
        private Long user;
        @Column(name = "status")
        private StatusENUMS status;
}
