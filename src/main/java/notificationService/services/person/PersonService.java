package notificationService.services.person;

import dto.userDto.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("person-service/api/v1/account")
public interface PersonService {
    @Operation(summary = "Получить пользователя по email")
    @GetMapping("/{email}")
    PersonDTO getPersonDTOByEmail(@PathVariable(name = "email") String email);

}
