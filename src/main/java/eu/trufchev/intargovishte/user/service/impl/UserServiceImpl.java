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
import eu.trufchev.intargovishte.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
        User user = userMapper.toEntity(registerDto);
        Set<Role> roles = new HashSet<>();

        Optional<Role> optionalUserRole = Optional.ofNullable(roleRepository.findByName("ROLE_USER"));
        optionalUserRole.ifPresent(roles::add);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Encrypt password
        userRepository.save(user);
        return "User Registered Successfully!";
    }

    @Override
    public String login(LoginDto loginDto) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
            );

            // Set authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            return token;

        } catch (UsernameNotFoundException e) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid username or email");
        } catch (BadCredentialsException e) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid password");
        } catch (Exception e) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Authentication failed" + e);
        }
    }

}