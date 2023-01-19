package notificationService.controller.advice;

import notificationService.exception.NotificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ErrorMessage> personExceptionHandler(NotificationException e, HttpServletRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .error(e.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(errorMessage, e.getHttpStatus());
    }
}
