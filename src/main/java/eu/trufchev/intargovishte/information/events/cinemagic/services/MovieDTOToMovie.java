package eu.trufchev.intargovishte.information.events.cinemagic.services;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDTOToMovie {

    public List<Movie> toMovie(List<MovieDTO> movieDTOs) {
        List<Movie> movies = new ArrayList<>();
        for (MovieDTO movieDTO : movieDTOs) {
            Movie movie = new Movie(
                    movieDTO.getId(),
                    movieDTO.getDuration(),
                    movieDTO.getDescription(),
                    movieDTO.getTitle(),
                    movieDTO.getOriginalTitle(),
                    movieDTO.getIsForChildren(),
                    movieDTO.getImdbId()
            );
            movies.add(movie);
        }
        return movies;
    }
}
