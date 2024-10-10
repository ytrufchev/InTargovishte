package eu.trufchev.intargovishte.information.weather.controllers;

import eu.trufchev.intargovishte.information.weather.entities.WeatherResponse;
import eu.trufchev.intargovishte.information.weather.feignClients.WeatherClient;
import eu.trufchev.intargovishte.information.weather.repositories.WeatherRepository;
import eu.trufchev.intargovishte.information.weather.services.GetWeatherInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("information/weather")
public class WeatherController {

    @Autowired
    private WeatherClient weatherClient;

    @Autowired
    private GetWeatherInformation getWeatherInformation;

    @Autowired
    WeatherRepository weatherRepository;

    @GetMapping("/update")
    public ResponseEntity<WeatherResponse> updateWeatherConditions() {
        WeatherResponse weatherResponse = getWeatherInformation.getCurrentWeatherConditions();
        weatherRepository.deleteAll();
        weatherRepository.save(weatherResponse);
        return ResponseEntity.ok(weatherResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<List<WeatherResponse>> showWeatherConditions() {

        List<WeatherResponse> weatherResponse = new ArrayList<>();
        weatherRepository.findAll().forEach(weatherResponse::add);
        return ResponseEntity.ok(weatherResponse);
    }
}


