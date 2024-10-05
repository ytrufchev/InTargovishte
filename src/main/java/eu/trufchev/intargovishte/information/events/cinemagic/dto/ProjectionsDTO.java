package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.RestrictedToCardTypeIds;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.ScreenFeatures;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectionsDTO {
    @Id
    private @Getter @Setter String id;
    private @Getter @Setter String cinemaId;
    private @Getter @Setter String regionId;
    private @Getter @Setter String screenId;
    private @Getter @Setter String moviePrintId;
    private @Getter @Setter String movieExternalId;
    private @Getter @Setter String moviePrintExternalId;
    private @Getter @Setter String screeningTimeFrom;
    private @Getter @Setter String screeningTimeTo;
    private @Getter @Setter String screeningDuration;
    private @Getter @Setter int audience;
    private @Getter @Setter String generalAdmission;
    private @Getter @Setter String saleTimeTo;
    private @Getter @Setter String reservationTimeTo;
    private @Getter @Setter Boolean isScreenSwapping;
    private @Getter @Setter int availabilityStatus;
    private @Getter @Setter int maxOccupancy;
    private @Getter @Setter String movieId;
    private @Getter @Setter String printType;
    private @Getter @Setter String speakingType;
    private @Getter @Setter String language;
    private @Getter @Setter String subtitles;
    private @Getter @Setter String subtitles2;
    private @Getter @Setter String soundType;
    private @Getter @Setter String release;
    private @Getter @Setter String format;
    private @Getter @Setter String resolution;
    private @Getter @Setter String frameRate;
    private @Getter @Setter List<RestrictedToCardTypeIds> restrictedToCardTypeIds;
    private @Getter @Setter List<ScreenFeatures> screenFeatures;
}
