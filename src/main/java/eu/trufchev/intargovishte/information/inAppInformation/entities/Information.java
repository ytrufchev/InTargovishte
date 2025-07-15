package eu.trufchev.intargovishte.information.inAppInformation.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
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
public class Information
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "title", length = 50)
    private String title;
    @Column(name = "description", length = 2000)
    private String description;
    @Column(name = "endDate")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long endDate;
    @Column(name = "status")
    private StatusENUMS status;
    @Column(name = "publisher")
    private Long userId;
    @Column(name = "image", length = 500000)
    private String image;
}
