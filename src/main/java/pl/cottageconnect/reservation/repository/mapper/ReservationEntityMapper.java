package pl.cottageconnect.reservation.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.reservation.domain.Reservation;
import pl.cottageconnect.reservation.entity.ReservationEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReservationEntityMapper {

    ReservationEntity mapToEntity(Reservation reservation);

    @Mapping(target = "customer.reservations", ignore = true)
    @Mapping(target = "cottage.reservations", ignore = true)
    @Mapping(target = "cottage.village.address", ignore = true)
    @Mapping(target = "cottage.village.cottages", ignore = true)
    @Mapping(target = "cottage.village.posts", ignore = true)
    @Mapping(target = "cottage.village.owner.village", ignore = true)
    Reservation mapFromEntity(ReservationEntity reservation);

}
