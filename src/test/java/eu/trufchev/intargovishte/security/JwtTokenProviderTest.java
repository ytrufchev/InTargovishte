package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize JwtTokenProvider with test values
        jwtTokenProvider = new JwtTokenProvider(); // Inject test values

        // Mock authentication
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    void testGenerateToken() {
        String token = jwtTokenProvider.generateToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtTokenProvider.generateToken(authentication);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        jwtTokenProvider = new JwtTokenProvider(); // 1 second expiration
        String token = jwtTokenProvider.generateToken(authentication);
        Thread.sleep(2000);
        assertFalse(jwtTokenProvider.validateToken(token));
    }
}
