package eu.trufchev.intargovishte.user.controller;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.security.JwtTokenProvider;
import eu.trufchev.intargovishte.security.RefreshToken;
import eu.trufchev.intargovishte.security.RefreshTokenService;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.Roles;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = userService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        // Authenticate and get the access token
        String token = userService.login(loginDto);

        if (token != null) {
            // Fetch the authentication from the context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Check if authentication is successful
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Fetch user from the authentication principal
            User user = (User) authentication.getPrincipal();

            // Generate access and refresh tokens
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = refreshTokenService.createRefreshToken(user); // Ensure this creates a valid refresh token

            // Prepare the response tokens
            Map<String, String> tokens = new HashMap<>();
            List<String> roleNames = user.getRoles().stream()
                    .map(Roles::getName) // Assuming Roles is an entity with a getName() method
                    .collect(Collectors.toList());

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("name", user.getName());
            tokens.put("roles", roleNames.toString());
            tokens.put("id", user.getId().toString());
            tokens.put("avatar", Optional.ofNullable(user.getAvatar()).orElse(""));
            tokens.put("email", user.getEmail());
            tokens.put("username", user.getUsername());

            // Return the map containing accessToken and refreshToken
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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

        // Generate new access token
        String accessToken = jwtTokenProvider.generateTokenWithUsername(username); // Adjust according to your JWT service

        // Return the new access token
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestHeader("refreshToken") String refreshToken) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authorization header");
        }

        // Extract the token from the Authorization header
        String accessToken = authorizationHeader.substring(7);

        // Look for the refresh token using the provided refresh token
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);
        if (optionalRefreshToken.isPresent()) {
            // Delete the refresh token for the user, effectively logging them out
            refreshTokenService.deleteByUser(optionalRefreshToken.get().getUser());
            return ResponseEntity.ok("User logged out successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
    }
}
