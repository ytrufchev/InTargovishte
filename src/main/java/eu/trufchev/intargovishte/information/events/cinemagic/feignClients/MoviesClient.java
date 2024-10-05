package eu.trufchev.intargovishte.information.events.cinemagic.feignClients;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.dto.ProjectionsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "cinemagic", url = "https://restapi.cineland.bg/api")
public interface MoviesClient {
    @GetMapping("/movie")
    List<MovieDTO> getMovies(@RequestParam("dateTimeFrom") String dateTimeFrom,
                             @RequestParam("dateTimeTo") String dateTimeTo,
                             @RequestParam("cinemaId") String cinemaId);

    @GetMapping("/region/6e831fe5-33e8-4e2f-b097-e94d61257dea/screening")
    List<ProjectionsDTO> getAllProjections(@RequestParam("dateTimeFrom") String dateTimeFrom,
                                        @RequestParam("dateTimeTo") String dateTimeTo

    );
}

