package eu.trufchev.intargovishte.user.controller;

import eu.trufchev.intargovishte.user.dto.PasswordUpdateDto;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEditController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/update-password")
    public ResponseEntity<String> updateUserPassword(@RequestBody PasswordUpdateDto passwordUpdateDto, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        // Verify the old password
        if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}
