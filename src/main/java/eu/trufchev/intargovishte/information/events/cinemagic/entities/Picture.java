package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;
    private @Setter String url;

    @JsonCreator
    public Picture(String url){
        this.url = url;
    }
    @JsonValue
    public String getUrl(){
        return this.url;
    }
}
