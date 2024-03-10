package pl.cottageconnect.reservation.controller.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.reservation.controller.dto.ReservationDTO;
import pl.cottageconnect.reservation.domain.Reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryReservation.testReservation;
import static pl.cottageconnect.util.TestDataFactoryReservation.testReservationDTO;

class ReservationMapperTest {
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);


    @Test
    @DisplayName("should map reservation to reservationDTO")
    void shouldMapReservationToDTO() {
        //given
        Reservation reservation = testReservation();
        ReservationDTO expectedDTO = testReservationDTO();
        //when
        ReservationDTO actualDTO = reservationMapper.mapToDTO(reservation);
        //then
        assertThat(expectedDTO).isEqualTo(actualDTO);
    }
}