package eu.trufchev.intargovishte.user.service.impl;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.security.JwtTokenProvider;
import eu.trufchev.intargovishte.security.RefreshTokenService;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.Roles;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.mapper.UserMapper;
import eu.trufchev.intargovishte.user.repository.RolesRepository;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;  // Inject JwtTokenProvider

    @Override
    public String register(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Passwords do not match!");
        }
        if (userRepository.existsByUsername(registerDto.getName())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        // Create new user entity
        User user = userMapper.toEntity(registerDto);
        Roles userRole;

        // Check if the role already exists
        if (rolesRepository.existsByName(Roles.USER)) {
            userRole = rolesRepository.findByName(Roles.USER); // Fetch existing role
        } else {
            userRole = new Roles();
            userRole.setName(Roles.USER);
            rolesRepository.save(userRole); // Save the new role
        }

        user.setRoles(Set.of(userRole)); // Assign the role to the user
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Encrypt password
        user.setAvatar(registerDto.getAvatarBase64());
        userRepository.save(user);
        return "User Registered Successfully!";
    }

    @Override
    public String login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);
            return jwt; // Return only the access token
        } catch (BadCredentialsException e) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Make sure this method exists in the repository
    }

}