package pl.cottageconnect.photo.controller.dto;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.photo.PhotoableType;

@With
@Builder
public record PhotoDTO(Long photoId,
                       PhotoableType type,
                       Long photoableId,
                       String path) {
}
