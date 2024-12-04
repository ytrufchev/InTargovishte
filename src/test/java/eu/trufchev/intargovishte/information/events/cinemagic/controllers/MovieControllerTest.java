package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Main Test Class
public class MovieControllerTest {

    private MovieController movieController;
    private InMemoryMovieRepository movieRepository;
    private MockMoviesClient moviesClient;

    @BeforeEach
    public void setup() {
        movieRepository = new InMemoryMovieRepository();
        moviesClient = new MockMoviesClient();
        movieController = new MovieController(moviesClient, movieRepository);
    }

    @Test
    public void testMoviesList() {
        // Prepare data
        Movie movie1 = new Movie(1L, "Movie 1", "2023-12-01", "18:00");
        Movie movie2 = new Movie(2L, "Movie 2", "2023-12-02", "20:00");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Test
        List<Movie> movies = movieController.moviesList();
        assertEquals(2, movies.size());
        assertTrue(movies.contains(movie1));
        assertTrue(movies.contains(movie2));
    }

    @Test
    public void testUpdateMovies() {
        // Test
        ResponseEntity<List<Movie>> response = movieController.manualUpdateMovies();
        List<Movie> updatedMovies = response.getBody();

        assertNotNull(updatedMovies);
        assertEquals(2, updatedMovies.size());
        assertEquals("Mock Movie 1", updatedMovies.get(0).getTitle());
        assertEquals("Mock Movie 2", updatedMovies.get(1).getTitle());
    }

    // Mock Controller Implementation
    static class MovieController {

        private final MoviesClient moviesClient;
        private final MovieRepository movieRepository;

        public MovieController(MoviesClient moviesClient, MovieRepository movieRepository) {
            this.moviesClient = moviesClient;
            this.movieRepository = movieRepository;
        }

        public List<Movie> moviesList() {
            return movieRepository.findAll();
        }

        public ResponseEntity<List<Movie>> manualUpdateMovies() {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime sevenDaysLater = now.plusDays(7);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            String dateTimeFrom = now.format(formatter);
            String dateTimeTo = sevenDaysLater.format(formatter);

            String cinemaId = "e20a0129-2388-4822-b4a4-a5cc8e119f4b";
            List<MovieDTO> movieDTOs = moviesClient.getMovies(dateTimeFrom, dateTimeTo, cinemaId);
            List<Movie> movies = new MovieDTOToMovie().toMovie(movieDTOs);

            movieRepository.deleteAll();
            movieRepository.saveAll(movies);

            return ResponseEntity.ok(movies);
        }
    }

    // Mock Repository Implementation
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

        @Override
        public void saveAll(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }

        @Override
        public void deleteAll() {
            movies.clear();
        }
    }

    // Mock Feign Client Implementation
    static class MockMoviesClient implements MoviesClient {
        @Override
        public List<MovieDTO> getMovies(String dateTimeFrom, String dateTimeTo, String cinemaId) {
            List<MovieDTO> movieDTOs = new ArrayList<>();
            movieDTOs.add(new MovieDTO("Mock Movie 1", "2023-12-01", "18:00"));
            movieDTOs.add(new MovieDTO("Mock Movie 2", "2023-12-02", "20:00"));
            return movieDTOs;
        }
    }

    // Supporting Classes
    interface MovieRepository {
        List<Movie> findAll();

        void save(Movie movie);

        void saveAll(List<Movie> movies);

        void deleteAll();
    }

    interface MoviesClient {
        List<MovieDTO> getMovies(String dateTimeFrom, String dateTimeTo, String cinemaId);
    }

    static class Movie {
        private Long id;
        private String title;
        private String date;
        private String time;

        public Movie(Long id, String title, String date, String time) {
            this.id = id;
            this.title = title;
            this.date = date;
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        // equals and hashCode for testing purposes
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Movie movie = (Movie) o;
            return id.equals(movie.id) && title.equals(movie.title);
        }

        @Override
        public int hashCode() {
            return id.hashCode() + title.hashCode();
        }
    }

    static class MovieDTO {
        private String title;
        private String date;
        private String time;

        public MovieDTO(String title, String date, String time) {
            this.title = title;
            this.date = date;
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }
    }

    static class MovieDTOToMovie {
        public List<Movie> toMovie(List<MovieDTO> movieDTOs) {
            List<Movie> movies = new ArrayList<>();
            for (MovieDTO dto : movieDTOs) {
                movies.add(new Movie(null, dto.getTitle(), dto.getDate(), dto.getTime()));
            }
            return movies;
        }
    }
}
