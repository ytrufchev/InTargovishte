package eu.trufchev.intargovishte.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String username;
    private String repeatPassword;
    private String avatarBase64;
}