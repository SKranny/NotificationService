package notificationService.mapper;

import dto.notification.SettingsDTO;
import notificationService.entities.Settings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingsMapper {
    Settings toSettings(SettingsDTO settingsDTO);

    SettingsDTO toSettingsDTO(Settings settings);
}
