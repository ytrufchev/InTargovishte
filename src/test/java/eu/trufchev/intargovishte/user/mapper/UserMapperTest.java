package eu.trufchev.intargovishte.user.mapper;

import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper(); // Use real ModelMapper
        userMapper = new UserMapper(passwordEncoder, modelMapper);
    }

    @Test
    void testToEntity_withValidRegisterDto() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setName("JohnDoe");
        registerDto.setEmail("johndoe@example.com");
        registerDto.setPassword("password");

        // Mock password encoding
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        // When
        User user = userMapper.toEntity(registerDto);

        // Then
        assertNotNull(user);
        assertEquals("JohnDoe", user.getName());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword()); // Ensure password is encoded
        verify(passwordEncoder).encode(registerDto.getPassword());
    }

    @Test
    void testToEntity_withNullRegisterDto() {
        // When
        User user = userMapper.toEntity(null);

        // Then
        assertNull(user); // Should return null if the input DTO is null
    }

    @Test
    void testToDto_withValidUser() {
        // Given
        User user = new User();
        user.setName("JaneDoe");
        user.setEmail("janedoe@example.com");

        // When
        RegisterDto registerDto = userMapper.toDto(user);

        // Then
        assertNotNull(registerDto);
        assertEquals("JaneDoe", registerDto.getName());
        assertEquals("janedoe@example.com", registerDto.getEmail());
    }

    @Test
    void testToDto_withNullUser() {
        // When
        RegisterDto registerDto = userMapper.toDto(null);

        // Then
        assertNull(registerDto); // Should return null if the input entity is null
    }
}
