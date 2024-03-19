package pl.cottageconnect.photo;

import lombok.Builder;
import lombok.With;

@With
@Builder
public record Photo(Long photoId,
                    PhotoableType type,
                    Long photoableId,
                    String path) {
}
