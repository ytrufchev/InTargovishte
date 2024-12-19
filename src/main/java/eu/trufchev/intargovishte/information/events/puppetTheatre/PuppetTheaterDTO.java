package eu.trufchev.intargovishte.information.events.puppetTheatre;

import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PuppetTheaterDTO {
    private Long id;
    private String imageUrl;
    private String eventMonth;
    private String eventDay;
    private String title;
    private String playTime;
    private Long likesCount;
    private boolean likedByCurrentUser;
}
