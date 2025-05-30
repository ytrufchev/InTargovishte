package eu.trufchev.intargovishte.user.controller;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.security.JwtTokenProvider;
import eu.trufchev.intargovishte.security.RefreshToken;
import eu.trufchev.intargovishte.security.RefreshTokenService;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.Roles;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.RolesRepository;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;
    private RolesRepository rolesRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        // Password validation regex
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);

        // Check if the password matches the regex
        if (!pattern.matcher(registerDto.getPassword()).matches()) {
            return new ResponseEntity<>(
                    "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, and one number.",
                    HttpStatus.BAD_REQUEST
            );
        }

        // Proceed with registration if password is valid
        String response = userService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);

        if (token != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userServiceImpl.findByUsername(loginDto.getUsernameOrEmail());
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = refreshTokenService.createRefreshToken(user);
            Map<String, String> tokens = new HashMap<>();
            List<String> roleNames = user.getRoles().stream()
                    .map(Roles::getName) // Assuming Role is an entity with a getName() method
                    .collect(Collectors.toList());

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("name", user.getName());
            tokens.put("roles", roleNames.toString());
            tokens.put("id", user.getId().toString());
            if(user.getAvatar() == null){
                user.setAvatar("");
            }
            tokens.put("avatar", user.getAvatar());
            tokens.put("email", user.getEmail());
            tokens.put("username", user.getUsername());
            // Return the map containing accessToken and refreshToken
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // or a better error response
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody Map<String, String> request) {
        // Validate the refresh token
        String token = request.get("refreshToken");
        if (token == null || token.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Refresh token is missing");
        }
        RefreshToken validToken = refreshTokenService.validateRefreshToken(token);
        // Check if the refresh token has expired
        if (refreshTokenService.isTokenExpired(validToken)) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Refresh token is expired");
        }

        // Fetch user associated with the refresh token
        User user = validToken.getUser();
        String username = user.getUsername();

        // Generate new access token (implement this method in your service)
        String accessToken = jwtTokenProvider.generateTokenWithUsername(username); // Adjust according to your JWT service

        // Return the new access token and refresh token
        return ResponseEntity.ok(accessToken);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader, @RequestHeader("refreshToken") String refreshToken) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authorization header");
        }

        // Extract the token from the Authorization header
        String accessToken = authorizationHeader.substring(7);

        // Look for the refresh token using the access token (assuming they are linked somehow)
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);
        if (optionalRefreshToken.isPresent()) {
            // Delete the refresh token for the user, effectively logging them out
            refreshTokenService.deleteByUser(optionalRefreshToken.get().getUser());
            return ResponseEntity.ok("User logged out successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
    }

    @DeleteMapping("/deleteuser/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Use userDetails.getUsername() to identify the user without casting
            String username = userDetails.getUsername();

            // Fetch the authenticated user from the database
            User user = userRepository.findByUsername(username);

            if (user != null && user.getId().equals(userId)) {
                userServiceImpl.deletePersonalUser(userId);
            } else {
                throw new AccessDeniedException("You can only delete your own account.");
            }
        } else {
            throw new AccessDeniedException("User not authenticated.");
        }
    }


//        TODO: Remove when appropriate
@PostMapping("/escalate")
public ResponseEntity<User> escalate(@RequestParam String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Roles superAdminRole = rolesRepository.findByName(Roles.SUPERADMIN);
    if (superAdminRole == null) {
        superAdminRole = new Roles();
        superAdminRole.setName(Roles.SUPERADMIN);
        rolesRepository.save(superAdminRole);
    }

    Set<Roles> roles = user.getRoles();
    roles.add(superAdminRole);
    user.setRoles(roles);

    User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
}


}