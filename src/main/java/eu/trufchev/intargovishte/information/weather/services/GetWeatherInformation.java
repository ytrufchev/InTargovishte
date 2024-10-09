package eu.trufchev.intargovishte.information.weather.services;

import eu.trufchev.intargovishte.information.weather.entities.WeatherResponse;
import eu.trufchev.intargovishte.information.weather.feignClients.WeatherClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class GetWeatherInformation {

    @Autowired
    private WeatherClient weatherClient;
    public WeatherResponse getCurrentWeatherConditions() {
        String unitGroup = "metric";
        String include = "current%2Cdays";
        String contentType = "json";
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("WEATHER_API");
        // Fetch weather data via Feign client
        return weatherClient.getCurrentConditions(unitGroup, include, key, contentType);

    }
}