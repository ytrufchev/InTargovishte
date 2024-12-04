package eu.trufchev.intargovishte.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid-token";
        String username = "testUser";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        // Mock getRequestURI to avoid NullPointerException
        when(request.getRequestURI()).thenReturn("/some-uri");

        // Mock Authorization header to return a valid Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Mock the JwtTokenProvider and UserDetailsService behavior
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Execute the filter method
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify the authentication in SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());

        // Ensure the filter chain continues processing
        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    public void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalid-token";

        // Mock the HttpServletRequest to return a non-null URI
        when(request.getRequestURI()).thenReturn("/some-uri");

        // Mock the Authorization header and token validation
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // Call the filter method
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify no authentication is set and the filter chain continues
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    public void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // Mock getRequestURI to avoid NullPointerException
        when(request.getRequestURI()).thenReturn("/some-uri");

        // Mock the Authorization header to return null (no token scenario)
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute the filter method
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify no authentication is set in the SecurityContext
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Ensure the filter chain continues processing
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_SkipsTokenValidationForSpecificEndpoint() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/information/news/update");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtTokenProvider, never()).validateToken(anyString());
    }

    @Test
    public void testGetJwtFromRequest_ValidAuthorizationHeader() {
        String token = "test-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        String extractedToken = jwtAuthenticationFilter.getJwtFromRequest(request);

        assertEquals(token, extractedToken);
    }

    @Test
    public void testGetJwtFromRequest_InvalidAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        String extractedToken = jwtAuthenticationFilter.getJwtFromRequest(request);

        assertNull(extractedToken);
    }

    @Test
    public void testGetJwtFromRequest_NoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String extractedToken = jwtAuthenticationFilter.getJwtFromRequest(request);

        assertNull(extractedToken);
    }
}
