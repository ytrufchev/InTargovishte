package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/superadmin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private GasStationRepository gasStationRepository;
    @Autowired
    private ParseGasStationToHtml parseGasStationToHtml;
    @Autowired
    private FueloClient fueloClient;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        Optional<User> userForDeletion = userRepository.findById(userId);
        if (userForDeletion.isPresent()) {
            userRepository.deleteById(userId);
            return "User with id " + userId + " deleted";
        } else {
            return "User with id " + userId + " not found";
        }
    }
    @DeleteMapping("/deletenews/{newsId}")
    public String deleteNewsEntry(@PathVariable Long newsId) {
        Optional<News> newsForDeletion = newsRepository.findById(newsId);
        if (newsForDeletion.isPresent()) {
            newsRepository.deleteById(newsId);
            return "News with id " + newsId + " deleted";
        } else {
            return "News with id " + newsId + " not found";
        }
    }

    @GetMapping("/update")
    public ResponseEntity<List<GasStation>> manualUpdate(){
        return updateGasStations();
    }
    public ResponseEntity<List<GasStation>> updateGasStations() {
        GasstationsList gasstationsLists = new GasstationsList();
        List<GasStation> gasStations = new ArrayList<>();
        for(int i = 0; i < gasstationsLists.getGasstations().size(); i++){
            gasStations.add(parseGasStationToHtml.parseGasStationHtml(fueloClient.getGasstationDetails(gasstationsLists.getGasstations().get(i), "bg")));
        }
        gasStationRepository.deleteAll();
        gasStationRepository.saveAll(gasStations);
        return ResponseEntity.ok(gasStations);
    }



}
