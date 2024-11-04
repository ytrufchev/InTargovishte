package eu.trufchev.intargovishte.user.dto;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}