package pl.cottageconnect.photo;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhotoEntityMapper {

    PhotoEntity mapToEntity(Photo photo);

    Photo mapFromEntity(PhotoEntity entity);
}
