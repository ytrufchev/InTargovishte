package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {
    private String status;
    List<Production> Productions;
}

