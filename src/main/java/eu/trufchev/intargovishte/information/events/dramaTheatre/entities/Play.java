package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Play {
    @Id
    private String id;
    private int length;
    private int minAgeRestriction;
    private String title;
    private String largePhoto;
    private String mediumPhoto;
    private String smallPhoto;
    private String placeName;
    private String startDates;
    @OneToMany(mappedBy = "event")
    @JsonIgnore  // prevent infinite recursion
    private List<DramaLike> likes;
    public void setStartDates(List<String> startDatesList) {
        this.startDates = String.join(",", startDatesList);  // Converts list to a single string
    }

    // Convert the stored string back into a List<String>
    public List<String> getStartDates() {
        return List.of(startDates.split(","));
    }
}