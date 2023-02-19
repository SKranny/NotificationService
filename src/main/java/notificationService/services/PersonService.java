package notificationService.services;

import dto.userDto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient("person-service/api/v1/account")
public interface PersonService {
    @GetMapping("/{email}")
    PersonDTO getPersonDTOByEmail(@PathVariable(name = "email") String email);

    @GetMapping
    List<PersonDTO> getAllPersonsDTO();

    @GetMapping
    List<PersonDTO> getAllPersonsDTOByTimeBetween(LocalDate date1, LocalDate date2);
}
