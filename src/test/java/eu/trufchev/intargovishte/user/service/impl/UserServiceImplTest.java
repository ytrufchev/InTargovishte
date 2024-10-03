package eu.trufchev.intargovishte.user.service.impl;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.security.JwtTokenProvider;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.Role;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.mapper.UserMapper;
import eu.trufchev.intargovishte.user.repository.RoleRepository;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterDto registerDto;
    private LoginDto loginDto;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup mock data for RegisterDto, LoginDto, and User
        registerDto = new RegisterDto();
        registerDto.setName("testUser");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setRepeatPassword("password");

        loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testUser");
        loginDto.setPassword("password");

        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    // ----------------- Test for register() method ------------------------

    @Test
    void testRegister_Success() {
        // Mock repository calls to simulate a successful registration
        when(userRepository.existsByUsername(registerDto.getName())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(registerDto)).thenReturn(user);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        // Act
        String result = userService.register(registerDto);

        // Assert that the user was successfully registered and saved
        verify(userRepository).save(user);
        assertEquals("User Registered Successfully!", result);
    }

    @Test
    void testRegister_PasswordMismatch() {
        // Test scenario where passwords do not match
        registerDto.setRepeatPassword("differentPassword");

        APIException exception = assertThrows(APIException.class, () -> userService.register(registerDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Passwords do not match!", exception.getMessage());
    }

    @Test
    void testRegister_UsernameExists() {
        // Test scenario where username already exists
        when(userRepository.existsByUsername(registerDto.getName())).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> userService.register(registerDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username already exists!", exception.getMessage());
    }

    @Test
    void testRegister_EmailExists() {
        // Test scenario where email already exists
        when(userRepository.existsByUsername(registerDto.getName())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> userService.register(registerDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email already exists!", exception.getMessage());
    }

    @Test
    void testRegister_RoleNotFound() {
        // Test scenario where the role "ROLE_USER" is not found
        when(userRepository.existsByUsername(registerDto.getName())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(registerDto)).thenReturn(user);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(null);

        // Act
        String result = userService.register(registerDto);

        // Assert
        assertTrue(user.getRoles().isEmpty());
        verify(userRepository).save(user);
        assertEquals("User Registered Successfully!", result);
    }

    // ----------------- Test for login() method ------------------------

    @Test
    void testLogin_Success() {
        // Mock successful authentication and token generation
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("fakeJwtToken");

        String result = userService.login(loginDto);

        assertEquals("fakeJwtToken", result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLogin_UsernameNotFound() {
        // Test scenario where the username is not found
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        APIException exception = assertThrows(APIException.class, () -> userService.login(loginDto));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Invalid username or email", exception.getMessage());
    }

    @Test
    void testLogin_BadCredentials() {
        // Test scenario where bad credentials are provided
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        APIException exception = assertThrows(APIException.class, () -> userService.login(loginDto));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testLogin_AuthenticationFailure() {
        // Test scenario where there is a general failure in authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        APIException exception = assertThrows(APIException.class, () -> userService.login(loginDto));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertTrue(exception.getMessage().contains("Authentication failed"));
    }
}
