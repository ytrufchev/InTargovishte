package eu.trufchev.intargovishte.information.events.cinemagic.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private @Getter @Setter Movie movie;
    private String url;

    @JsonCreator
    public Banner(String url){
        this.url = url;
    }
    @JsonValue
    public String getUrl(){
        return this.url;
    }
}
