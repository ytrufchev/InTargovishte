package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetPlaysRequest {
    private String obj;
    private int skip;
    private int limit;
}

