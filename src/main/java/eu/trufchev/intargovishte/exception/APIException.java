package eu.trufchev.intargovishte.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class APIException extends RuntimeException{
    final private HttpStatus status;
    final private String message;
}
