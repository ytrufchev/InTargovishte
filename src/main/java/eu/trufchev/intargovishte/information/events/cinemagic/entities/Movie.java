package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Movie {
@Id
private @Getter @Setter String id;
@Column(name = "duration") private @Getter @Setter int duration;
@Column(name ="description", length = 2500) private @Getter @Setter String description;
@Column(name ="title") private @Getter @Setter String title;
@Column(name ="originalTitle") private @Getter @Setter String originalTitle;
@Column(name ="isForChildren") private @Getter @Setter Boolean isForChildren;
@Column(name ="imdbId") private @Getter @Setter String imdbId;
    @JsonManagedReference
@OneToMany(mappedBy = "event")
private @Getter @Setter List<MovieLike> likes;

}
