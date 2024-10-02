package eu.trufchev.intargovishte.user.service;

import eu.trufchev.intargovishte.exception.APIException;
import eu.trufchev.intargovishte.user.dto.LoginDto;
import eu.trufchev.intargovishte.user.dto.RegisterDto;

public interface UserService {
    public String register(RegisterDto registerDto) throws APIException;
    public String login(LoginDto loginDto);
}