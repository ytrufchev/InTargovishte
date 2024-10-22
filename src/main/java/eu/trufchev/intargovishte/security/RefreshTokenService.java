package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt-refresh-expiration-in-ms}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // Find a refresh token by token string
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Create a new refresh token
    public String createRefreshToken(User user) {
        // Check if a refresh token already exists for the user
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;

        if (existingToken.isPresent()) {
            // Either update the existing token's expiry date or delete it
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());  // Regenerate token
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        } else {
            // Create a new refresh token
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        }

        // Save the refresh token
        refreshTokenRepository.save(refreshToken);

  // Use the refresh token

        return refreshToken.getToken();
    }

    // Delete all refresh tokens by user
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    // Check if a refresh token is expired
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
    public RefreshToken validateRefreshToken(String token) {
        System.out.println("Validating token: " + token);
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
    }
}