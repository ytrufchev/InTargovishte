package eu.trufchev.intargovishte.information.events.cinemagic.dto;

import eu.trufchev.intargovishte.information.events.cinemagic.entities.*;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieDTO {
@Id
private @Getter @Setter String id;
private @Getter @Setter int duration;
private @Getter @Setter String ageLongDescription;
private @Getter @Setter String description;
private @Getter @Setter String releaseDate;
private @Getter @Setter String distributor;
private @Getter @Setter String distributorId;
private @Getter @Setter List<Genre> genres;
private @Getter @Setter List<Trailer> trailers;
private @Getter @Setter List<Poster> posters;
private @Getter @Setter List<Picture> pictures;
private @Getter @Setter List<PosPoster> posPosters;
private @Getter @Setter List<Banner> banners;
private @Getter @Setter List<Rating> ratings;
private @Getter @Setter List<BrightcoveVideo> brightcoveVideos;
private @Getter @Setter List<Tag> tagGroups;
private @Getter @Setter String title;
private @Getter @Setter String shortTitle;
private @Getter @Setter String originalTitle;
private @Getter @Setter String onlineTitle;
private @Getter @Setter String filmCast;
private @Getter @Setter String scenario;
private @Getter @Setter Boolean isForChildren;
private @Getter @Setter String yearOfProduction;
private @Getter @Setter String director;
private @Getter @Setter String shortDescription;
private @Getter @Setter String premiereDate;
private @Getter @Setter String worldPremiereDate;
private @Getter @Setter String country;
private @Getter @Setter String originalLanguage;
private @Getter @Setter String imdbId;
private @Getter @Setter String imdbRating;
private @Getter @Setter String type;

}
