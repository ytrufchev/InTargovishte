package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshTokenService = new RefreshTokenService(3600000, refreshTokenRepository, jwtTokenProvider); // 1 hour
    }

    @Test
    void testFindByToken_TokenExists() {
        String token = "test-token";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getToken());
        verify(refreshTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void testFindByToken_TokenDoesNotExist() {
        String token = "non-existent-token";

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertFalse(result.isPresent());
        verify(refreshTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void testCreateRefreshToken_UserWithoutExistingToken() {
        User user = new User();
        RefreshToken newToken = new RefreshToken();
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(Instant.now().plusMillis(3600000));
        newToken.setUser(user);

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(newToken);

        String token = refreshTokenService.createRefreshToken(user);

        assertNotNull(token);
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testCreateRefreshToken_UserWithExistingToken() {
        User user = new User();
        RefreshToken existingToken = new RefreshToken();
        existingToken.setToken("old-token");
        existingToken.setExpiryDate(Instant.now().minusSeconds(3600)); // Expired

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(existingToken));

        String newToken = refreshTokenService.createRefreshToken(user);

        assertNotNull(newToken);
        assertNotEquals("old-token", newToken); // Token should be regenerated
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(1)).save(existingToken);
    }

    @Test
    void testDeleteByUser() {
        User user = new User();

        doNothing().when(refreshTokenRepository).deleteByUser(user);

        refreshTokenService.deleteByUser(user);

        verify(refreshTokenRepository, times(1)).deleteByUser(user);
    }

    @Test
    void testIsTokenExpired_ExpiredToken() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minusSeconds(3600)); // Expired 1 hour ago

        boolean result = refreshTokenService.isTokenExpired(token);

        assertTrue(result);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(3600)); // Expires in 1 hour

        boolean result = refreshTokenService.isTokenExpired(token);

        assertFalse(result);
    }

    @Test
    void testValidateRefreshToken_ValidToken() {
        String token = "valid-token";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.validateRefreshToken(token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        verify(refreshTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void testValidateRefreshToken_InvalidToken() {
        String token = "invalid-token";

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> refreshTokenService.validateRefreshToken(token));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Invalid refresh token", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(token);
    }
}
