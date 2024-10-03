package eu.trufchev.intargovishte.user.mapper;

import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.apache.commons.io.IOUtils.skip;

@Component
@AllArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public User toEntity(RegisterDto registerDto) {
        if (registerDto == null) {
            return null;
        }

        User user = modelMapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return user;
    }

    public RegisterDto toDto(User user) {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setName(user.getName());
        registerDto.setEmail(user.getEmail());
        return registerDto;
    }
}