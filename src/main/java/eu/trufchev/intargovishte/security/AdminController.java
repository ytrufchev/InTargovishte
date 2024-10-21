package eu.trufchev.intargovishte.security;

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

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        Optional<User> userForDeletion = userRepository.findById(userId);
        if (userForDeletion.isPresent()) {
            userRepository.deleteById(userId);
            return "User with id " + userId + " deleted";
        } else {
            return "User with id " + userId + " not found";
        }
    }



}
