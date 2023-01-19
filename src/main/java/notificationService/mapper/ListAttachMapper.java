package notificationService.mapper;

import dto.notification.AttachDTO;
import notificationService.entities.Attach;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AttachMapper.class)
public interface ListAttachMapper {
    List<Attach> toAttach(List<AttachDTO> attachDTOs);

    List<AttachDTO> toAttachDTO(List<Attach> attaches);
}
