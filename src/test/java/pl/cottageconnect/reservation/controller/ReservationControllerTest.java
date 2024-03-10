package pl.cottageconnect.reservation.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.reservation.controller.mapper.ReservationMapper;
import pl.cottageconnect.reservation.domain.Reservation;
import pl.cottageconnect.reservation.service.ReservationService;
import pl.cottageconnect.security.configuration.JwtAuthFilter;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.cottageconnect.reservation.controller.ReservationController.RESERVATION_CONFIRMED;
import static pl.cottageconnect.reservation.controller.ReservationController.RESERVATION_SUCCESS_MESSAGE;
import static pl.cottageconnect.reservation.controller.ReservationController.Routes.*;
import static pl.cottageconnect.util.TestDataFactoryReservation.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ReservationControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private final ReservationMapper reservationMapper;
    @MockBean
    private final ReservationService reservationService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("should get all reservations by customer ID")
    void shouldGetAllReservationsByCustomerId() throws Exception {
        //given
        Long customerId = 1L;
        Principal principal = mock(Principal.class);
        PageRequest pageable = PageRequest.of(0, 5);

        List<Reservation> reservations = List.of(testReservation(), testReservation2());
        Page<Reservation> reservationPage = new PageImpl<>(reservations, pageable, 1);

        when(reservationService.getAllReservationsByCustomerId(customerId, principal, pageable))
                .thenReturn(reservationPage);
        when(reservationMapper.mapToDTO(testReservation())).thenReturn(testReservationDTO());
        when(reservationMapper.mapToDTO(testReservation2())).thenReturn(testReservationDTO2());
        //when,then
        mockMvc.perform(get(GET_RESERVATIONS_BY_CUSTOMER_ID, customerId)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(reservations.size())))
                .andExpect(jsonPath("$.content[*].status", both(hasItem(true)).and(hasItem(false))))
                .andReturn();
    }

    @Test
    @DisplayName("should get all reservations by customer ID and status")
    void shouldGetAllReservationsByCustomerIdWithStatus() throws Exception {
        //given
        Long customerId = 1L;
        boolean status = true;
        Principal principal = mock(Principal.class);
        PageRequest pageable = PageRequest.of(0, 5);

        List<Reservation> reservations = List.of(testReservation());
        Page<Reservation> reservationPage = new PageImpl<>(reservations, pageable, 1);

        when(reservationService.getAllReservationsByCustomerIdAndStatus(customerId, status, principal, pageable))
                .thenReturn(reservationPage);
        when(reservationMapper.mapToDTO(testReservation())).thenReturn(testReservationDTO());
        //when,then
        mockMvc.perform(get(GET_RESERVATIONS_BY_CUSTOMER_ID, customerId)
                        .queryParam("status", Boolean.toString(status))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(reservations.size())))
                .andExpect(jsonPath("$.content[*].status", everyItem(is(true))))
                .andReturn();
    }


    @Test
    @DisplayName("should get all reservations by cottage ID")
    void shouldGetAllReservationsByCottageId() throws Exception {
        //given
        Long cottageId = 2L;
        Principal principal = mock(Principal.class);
        PageRequest pageable = PageRequest.of(0, 5);

        List<Reservation> reservations = List.of(testReservation2(), testReservation3());
        Page<Reservation> reservationPage = new PageImpl<>(reservations, pageable, 1);

        when(reservationService.getAllReservationsByCottageId(cottageId, principal, pageable))
                .thenReturn(reservationPage);
        when(reservationMapper.mapToDTO(testReservation2())).thenReturn(testReservationDTO2());
        when(reservationMapper.mapToDTO(testReservation3())).thenReturn(testReservationDTO3());
        //when,then
        mockMvc.perform(get(GET_RESERVATIONS_BY_COTTAGE_ID, cottageId)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(reservations.size())))
                .andExpect(jsonPath("$.content[*].status", both(hasItem(true)).and(hasItem(false))))
                .andExpect(jsonPath("$.content[*].cottage.cottageId", everyItem(is(Math.toIntExact(cottageId)))))
                .andReturn();
    }

    @Test
    @DisplayName("should get all reservations by cottage ID and status")
    void shouldGetAllReservationsByCottageIdAndStatus() throws Exception {
        //given
        Long cottageId = 1L;
        boolean status = true;
        Principal principal = mock(Principal.class);
        PageRequest pageable = PageRequest.of(0, 5);

        List<Reservation> reservations = List.of(testReservation(), testReservation3());
        Page<Reservation> reservationPage = new PageImpl<>(reservations, pageable, 1);

        when(reservationService.getAllReservationsByCottageIdAndStatus(cottageId, status, principal, pageable))
                .thenReturn(reservationPage);
        when(reservationMapper.mapToDTO(testReservation())).thenReturn(testReservationDTO());
        when(reservationMapper.mapToDTO(testReservation3())).thenReturn(testReservationDTO3());
        //when,then
        mockMvc.perform(get(GET_RESERVATIONS_BY_COTTAGE_ID, cottageId)
                        .param("status", Boolean.toString(status))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(reservations.size())))
                .andExpect(jsonPath("$.content[*].status", everyItem(is(true))))
                .andReturn();
    }

    @Test
    @DisplayName("should add reservation")
    void shouldAddReservation() throws Exception {
        //given
        Long cottageId = 1L;
        LocalDate dayIn = LocalDate.parse("2023-03-01");
        LocalDate dayOut = LocalDate.parse("2023-04-01");
        Principal principal = mock(Principal.class);
        //when, then
        mockMvc.perform(post(ADD_RESERVATION)
                        .param("cottageId", String.valueOf(cottageId))
                        .param("dayIn", String.valueOf(dayIn))
                        .param("dayOut", String.valueOf(dayOut))
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(RESERVATION_SUCCESS_MESSAGE));
    }

    @Test
    @DisplayName("should confirm reservation by reservation ID")
    void shouldConfirmReservationById() throws Exception {
        //given
        Long reservationId = 1L;
        Principal principal = mock(Principal.class);

        doNothing().when(reservationService).confirmReservationById(reservationId, principal);
        //when,then
        mockMvc.perform(post(CONFIRM_RESERVATION, reservationId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(RESERVATION_CONFIRMED));
        verify(reservationService, times(1)).confirmReservationById(reservationId, principal);
    }

    @Test
    @DisplayName("should delete reservation by reservation ID")
    void shouldDeleteReservationById() throws Exception {
        //given
        Long reservationId = 1L;
        Principal principal = mock(Principal.class);

        doNothing().when(reservationService).deleteReservationById(reservationId, principal);
        //when,then
        mockMvc.perform(delete(DELETE_RESERVATION, reservationId)
                        .principal(principal))
                .andExpect(status().isNoContent());
        verify(reservationService, times(1)).deleteReservationById(reservationId, principal);
    }
}