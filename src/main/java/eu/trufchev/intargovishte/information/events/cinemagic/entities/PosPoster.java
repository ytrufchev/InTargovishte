package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PosPoster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @Setter String url;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;
    @JsonCreator
    public PosPoster(String url){
        this.url = url;
    }
    @JsonValue
    public String getUrl(){
        return this.url;
    }
}
