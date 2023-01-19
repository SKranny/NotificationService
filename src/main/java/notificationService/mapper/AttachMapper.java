package notificationService.mapper;

import dto.notification.AttachDTO;
import notificationService.entities.Attach;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachMapper {
    Attach toAttach(AttachDTO attachDTO);

    AttachDTO toAttachDTO(Attach attach);
}
