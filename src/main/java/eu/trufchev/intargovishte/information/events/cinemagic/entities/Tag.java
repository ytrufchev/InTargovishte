package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;
    private @Setter String tag;
    @JsonCreator
    public Tag(String tag){
        this.tag = tag;
    }
    @JsonValue
    public String getTag(){
        return this.tag;
    }
}
