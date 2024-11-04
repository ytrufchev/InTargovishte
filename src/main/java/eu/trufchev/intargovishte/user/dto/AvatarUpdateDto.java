package eu.trufchev.intargovishte.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AvatarUpdateDto {
    @Size(max = 50000, message = "Avatar image data must be 50000 characters or less")
    private String avatarBase64;
}
