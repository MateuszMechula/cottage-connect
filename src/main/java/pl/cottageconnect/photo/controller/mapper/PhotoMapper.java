package pl.cottageconnect.photo.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.photo.Photo;
import pl.cottageconnect.photo.controller.dto.PhotoDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDTO mapToDTO(Photo photo);
}
