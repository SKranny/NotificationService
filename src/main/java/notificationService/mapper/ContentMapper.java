package notificationService.mapper;

import dto.notification.ContentDTO;
import notificationService.entities.Content;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = ListAttachMapper.class
)
public interface ContentMapper {
    Content toContent(ContentDTO contentDTO);

    ContentDTO toContentDTO(Content content);
}
