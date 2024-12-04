package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test Class
public class MoviesWithProjectionControllerTest {

    private MoviesWithProjectionController controller;
    private InMemoryMovieRepository movieRepository;
    private InMemoryProjectionRepository projectionRepository;

    @BeforeEach
    public void setup() {
        movieRepository = new InMemoryMovieRepository();
        projectionRepository = new InMemoryProjectionRepository();
        controller = new MoviesWithProjectionController(movieRepository, projectionRepository);
    }

    @Test
    public void testScreenings() {
        // Prepare Movies
        Movie movie1 = new Movie(1L, "Movie 1");
        Movie movie2 = new Movie(2L, "Movie 2");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Prepare Projections
        Projections projection1 = new Projections(1L, 1L, "2023-12-01 18:00");
        Projections projection2 = new Projections(2L, 2L, "2023-12-02 20:00");
        projectionRepository.save(projection1);
        projectionRepository.save(projection2);

        // Test
        ResponseEntity<List<MovieWithProjections>> response = controller.screenings();
        List<MovieWithProjections> result = response.getBody();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Movie 1", result.get(0).getMovie().getTitle());
        assertEquals("2023-12-01 18:00", result.get(0).getProjections().get(0).getStartTime());

        assertEquals("Movie 2", result.get(1).getMovie().getTitle());
        assertEquals("2023-12-02 20:00", result.get(1).getProjections().get(0).getStartTime());
    }

    // Main Controller Class
    static class MoviesWithProjectionController {
        private final MovieRepository movieRepository;
        private final ProjectionRepository projectionRepository;

        public MoviesWithProjectionController(MovieRepository movieRepository, ProjectionRepository projectionRepository) {
            this.movieRepository = movieRepository;
            this.projectionRepository = projectionRepository;
        }

        public ResponseEntity<List<MovieWithProjections>> screenings() {
            List<Movie> movies = movieRepository.findAll();
            List<Projections> projections = projectionRepository.findAll();
            MovieWithProjectionsDTO dto = new MovieWithProjectionsDTO();
            List<MovieWithProjections> movieWithProjectionsList = dto.combineMovieWithProjections(movies, projections);
            return ResponseEntity.ok(movieWithProjectionsList);
        }
    }

    // Supporting Classes
    interface MovieRepository {
        List<Movie> findAll();

        void save(Movie movie);
    }

    interface ProjectionRepository {
        List<Projections> findAll();

        void save(Projections projection);
    }

    static class InMemoryMovieRepository implements MovieRepository {
        private final List<Movie> movies = new ArrayList<>();

        @Override
        public List<Movie> findAll() {
            return new ArrayList<>(movies);
        }

        @Override
        public void save(Movie movie) {
            movies.add(movie);
        }
    }

    static class InMemoryProjectionRepository implements ProjectionRepository {
        private final List<Projections> projections = new ArrayList<>();

        @Override
        public List<Projections> findAll() {
            return new ArrayList<>(projections);
        }

        @Override
        public void save(Projections projection) {
            projections.add(projection);
        }
    }

    static class Movie {
        private Long id;
        private String title;

        public Movie(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    static class Projections {
        private Long id;
        private Long movieId;
        private String startTime;

        public Projections(Long id, Long movieId, String startTime) {
            this.id = id;
            this.movieId = movieId;
            this.startTime = startTime;
        }

        public Long getMovieId() {
            return movieId;
        }

        public String getStartTime() {
            return startTime;
        }
    }

    static class MovieWithProjections {
        private Movie movie;
        private List<Projections> projections;

        public MovieWithProjections(Movie movie, List<Projections> projections) {
            this.movie = movie;
            this.projections = projections;
        }

        public Movie getMovie() {
            return movie;
        }

        public List<Projections> getProjections() {
            return projections;
        }
    }

    static class MovieWithProjectionsDTO {
        public List<MovieWithProjections> combineMovieWithProjections(List<Movie> movies, List<Projections> projections) {
            List<MovieWithProjections> result = new ArrayList<>();
            for (Movie movie : movies) {
                List<Projections> movieProjections = new ArrayList<>();
                for (Projections projection : projections) {
                    if (projection.getMovieId().equals(movie.getId())) {
                        movieProjections.add(projection);
                    }
                }
                result.add(new MovieWithProjections(movie, movieProjections));
            }
            return result;
        }
    }
}
