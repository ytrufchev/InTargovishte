package eu.trufchev.intargovishte.user.service;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;
import eu.trufchev.intargovishte.user.entity.User;

import java.util.Optional;

public interface UserService {
    public String register(RegisterDto registerDto) throws APIException;
    public String login(LoginDto loginDto);
    public User findByUsername(String username);
}