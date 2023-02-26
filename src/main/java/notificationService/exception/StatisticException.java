package notificationService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class StatisticException extends RuntimeException {
    private final HttpStatus httpStatus;

    public StatisticException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public StatisticException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
