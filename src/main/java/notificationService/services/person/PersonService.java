package notificationService.services;

import dto.userDto.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import notificationService.dto.Statistic.constant.BetweenDataRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("person-service/api/v1/account")
public interface PersonService {
    @Operation(summary = "Получить пользователя по email")
    @GetMapping("/{email}")
    PersonDTO getPersonDTOByEmail(@PathVariable(name = "email") String email);

    @GetMapping
    List<PersonDTO> getAllPersonsDTO();

    @GetMapping("/allBetween")
    List<PersonDTO> getAllPersonsDTOByTimeBetween(@RequestParam BetweenDataRequest request);

    @GetMapping("/active")
    public List<PersonDTO> getAllActivePersons();

    @GetMapping("/blocked")
    public List<PersonDTO> getAllBlockedPersons();
}
