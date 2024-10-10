package eu.trufchev.intargovishte.user.controller;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.security.JwtTokenProvider;
import eu.trufchev.intargovishte.security.RefreshToken;
import eu.trufchev.intargovishte.security.RefreshTokenService;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
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
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("name", user.getName());
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
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);
        if (optionalRefreshToken.isPresent()) {
            refreshTokenService.deleteByUser(optionalRefreshToken.get().getUser());
            return ResponseEntity.ok("User logged out successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
    }
}