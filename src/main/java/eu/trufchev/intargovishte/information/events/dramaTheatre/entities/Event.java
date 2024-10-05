package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
    private String dateStart;
    private Host host;
    private String id;
    private boolean isCohost;
    private Owner owner;
    private ProductionEvent production;
    private Stats stats;
    private int type;
}