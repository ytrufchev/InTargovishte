package eu.trufchev.intargovishte.information.events.cinemagic.services;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieWithProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieLikeRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DeleteOldMovies {
    private final MovieRepository movieRepository;
    private final ProjectionRepository projectionRepository;
    private final MovieLikeRepository movieLikeRepository;

    @Autowired
    public DeleteOldMovies(MovieRepository movieRepository, ProjectionRepository projectionRepository, MovieLikeRepository movieLikeRepository) {
        this.movieRepository = movieRepository;
        this.projectionRepository = projectionRepository;
        this.movieLikeRepository = movieLikeRepository;
    }


    public void deleteOldMovies() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String dateTimeTo = now.format(formatter);
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        List<Projections> projections = (List<Projections>) projectionRepository.findAll();
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
        List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections);
        for(MovieWithProjections movieWithProjections : movieWithProjectionsList){
            if(movieWithProjections.getProjections().get(movieWithProjections.getProjections().size()-1).getScreeningTimeTo().equals(dateTimeTo)){
                movieLikeRepository.deleteByEventId(movieWithProjections.getId());
                movieRepository.deleteById(movieWithProjections.getId());
            }
        }
    }
}
