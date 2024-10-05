package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseEvents {
    private String status;
    private List<Event> events;
}
