package eu.trufchev.intargovishte.information.events.puppetTheatre.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PuppetTheater {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "imageUrl")
    private String imageUrl;
    @Column(name = "eventMonth")
    private String eventMonth;
    @Column(name = "eventDay")
    private String eventDay;
    @Column(name = "title")
    private String title;
    @Column(name = "playTime")
    private String playTime;
    @JsonManagedReference
    @OneToMany(mappedBy = "event")
    private List<PuppetTheaterLike> likes;
}