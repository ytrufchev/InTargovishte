package eu.trufchev.intargovishte.user.service.impl;

import eu.trufchev.intargovishte.exception.APIException;
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
        if (optionalUserRole.isPresent()) {
            roles.add(optionalUserRole.get());
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "User Registered Successfully!";
    }
    //change after JWT token is added
    @Override
    public String login(LoginDto loginDto) {
        String usernameOrEmail = loginDto.getUsernameOrEmail();
        String password = loginDto.getPassword();
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (!userOptional.isPresent()) {
            return "Invalid username or email";
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid password";
        }
        return "Login successful";
    }
}