package pl.cottageconnect.reservation.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.reservation.Reservation;
import pl.cottageconnect.reservation.controller.dto.ReservationDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

    ReservationDTO mapToDTO(Reservation reservation);
}
