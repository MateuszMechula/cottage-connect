package pl.cottageconnect.photo.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.entity.PhotoEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhotoEntityMapper {

    PhotoEntity mapToEntity(Photo photo);

    Photo mapFromEntity(PhotoEntity entity);
}
