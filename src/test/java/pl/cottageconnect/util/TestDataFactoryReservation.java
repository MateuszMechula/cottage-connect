package pl.cottageconnect.util;

import pl.cottageconnect.cottage.controller.dto.CottageDTO;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.reservation.controller.dto.ReservationDTO;
import pl.cottageconnect.reservation.domain.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class TestDataFactoryReservation {

    public static Reservation testReservation() {
        return Reservation.builder()
                .reservationId(1L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 6, 15, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 6, 17, 11, 0), ZoneOffset.UTC))
                .status(TRUE)
                .cottage(testCottage())
                .build();
    }

    public static Reservation testReservation2() {
        return Reservation.builder()
                .reservationId(2L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 7, 18, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 7, 25, 11, 0), ZoneOffset.UTC))
                .status(FALSE)
                .cottage(testCottage2())
                .build();
    }

    public static Reservation testReservation3() {
        return Reservation.builder()
                .reservationId(3L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 7, 18, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 7, 25, 11, 0), ZoneOffset.UTC))
                .status(TRUE)
                .cottage(testCottage2())
                .build();
    }

    public static ReservationDTO testReservationDTO() {
        return ReservationDTO.builder()
                .reservationId(1L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 6, 15, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 6, 17, 11, 0), ZoneOffset.UTC))
                .status(TRUE)
                .cottage(testCottageDTO())
                .build();
    }

    public static ReservationDTO testReservationDTO2() {
        return ReservationDTO.builder()
                .reservationId(2L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 7, 18, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 7, 25, 11, 0), ZoneOffset.UTC))
                .status(FALSE)
                .cottage(testCottageDTO2())
                .build();
    }

    public static ReservationDTO testReservationDTO3() {
        return ReservationDTO.builder()
                .reservationId(3L)
                .dayIn(OffsetDateTime.of(LocalDateTime.of(2024, 7, 18, 15, 0), ZoneOffset.UTC))
                .dayOut(OffsetDateTime.of(LocalDateTime.of(2024, 7, 25, 11, 0), ZoneOffset.UTC))
                .status(TRUE)
                .cottage(testCottageDTO2())
                .build();
    }

    private static Cottage testCottage() {
        return Cottage.builder()
                .cottageId(1L)
                .cottageNumber(5)
                .cottageSize(3)
                .price(BigDecimal.valueOf(300))
                .description("The best cottage")
                .build();
    }

    private static Cottage testCottage2() {
        return Cottage.builder()
                .cottageId(2L)
                .cottageNumber(3)
                .cottageSize(6)
                .price(BigDecimal.valueOf(500))
                .description("The best big cottage")
                .build();
    }

    private static CottageDTO testCottageDTO() {
        return CottageDTO.builder()
                .cottageId(1L)
                .cottageNumber(5)
                .cottageSize(3)
                .price(BigDecimal.valueOf(300))
                .description("The best cottage")
                .build();
    }

    private static CottageDTO testCottageDTO2() {
        return CottageDTO.builder()
                .cottageId(2L)
                .cottageNumber(3)
                .cottageSize(6)
                .price(BigDecimal.valueOf(500))
                .description("The best big cottage")
                .build();
    }
}
