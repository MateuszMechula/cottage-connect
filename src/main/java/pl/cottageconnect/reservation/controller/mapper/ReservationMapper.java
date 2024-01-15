package pl.cottageconnect.reservation.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.reservation.controller.dto.ReservationDTO;
import pl.cottageconnect.reservation.domain.Reservation;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

    ReservationDTO mapToDTO(Reservation reservation);
}
