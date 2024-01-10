package pl.cottageconnect.photo.domain;

import lombok.*;
import pl.cottageconnect.photo.enums.PhotoableType;

@With
@Value
@Builder
@EqualsAndHashCode(of = "photoId")
@ToString(of = {"photoId", "type", "photoableId"})
public class Photo {

    Long photoId;
    PhotoableType type;
    Long photoableId;
    String path;
}
