package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stats {
    private int capacity;
    private int reserved;
    private int selled;
    private int totalAmount;
    private int validated;
}
