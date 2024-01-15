package pl.cottageconnect.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.reservation.controller.dto.ReservationDTO;
import pl.cottageconnect.reservation.controller.mapper.ReservationMapper;
import pl.cottageconnect.reservation.service.ReservationService;

import java.security.Principal;
import java.time.LocalDate;

import static pl.cottageconnect.reservation.controller.ReservationController.Routes.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "reservations", description = "Endpoints responsible for reservations (<b>OWNER</b>, <b>CUSTOMER</b>)")
public class ReservationController {

    private final ReservationMapper reservationMapper;
    private final ReservationService reservationService;

    @Operation(
            summary = "Retrieve Reservations by Customer ID (ONLY CUSTOMER)",
            description = "Allows customers to retrieve a list of their reservations for cottages."
    )
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(value = GET_RESERVATIONS_BY_CUSTOMER_ID)
    public ResponseEntity<Page<ReservationDTO>> getAllReservationsByCustomerId(
            @PathVariable(name = "customerId") Long customerId,
            @RequestParam(name = "status", required = false) Boolean status,
            Pageable pageable,
            Principal connectedUser) {

        Page<ReservationDTO> response = reservationService
                .getAllReservationsByCustomerId(customerId, status, connectedUser, pageable).map(reservationMapper::mapToDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve Reservations by Cottage ID (ONLY OWNER)",
            description = "Allows owners to retrieve a list of reservations cottage."
    )
    @PreAuthorize("hasAuthority('OWNER')")
    @GetMapping(value = GET_RESERVATIONS_BY_COTTAGE_ID)
    public ResponseEntity<Page<ReservationDTO>> getAllReservationsByCottageId(
            @PathVariable(name = "cottageId") Long cottageId,
            @RequestParam(name = "status", required = false) Boolean status,
            Pageable pageable,
            Principal connectedUser) {

        Page<ReservationDTO> response = reservationService
                .getAllReservationsByCottageId(cottageId, status, connectedUser, pageable).map(reservationMapper::mapToDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Add reservation for cottage (ONLY CUSTOMER)",
            description = "Allows customers to add reservations for cottages." +
                    " This endpoint is exclusively available to customers with the CUSTOMER authority."
    )
    @PostMapping(value = ADD_RESERVATION)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> addReservation(@RequestParam Long cottageId,
                                            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dayIn,
                                            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dayOut,
                                            Principal connectedUser) {

        reservationService.addReservation(cottageId, dayIn, dayOut, connectedUser);

        return ResponseEntity.ok("Reservation added successfully");
    }

    @Operation(
            summary = "Confirm Reservation (ONLY OWNER)",
            description = "Endpoint dedicated to confirming reservations for cottages. " +
                    "This operation is restricted to users with the 'OWNER' authority"
    )
    @PostMapping(value = CONFIRM_RESERVATION)
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> confirmReservation(@PathVariable Long reservationId, Principal connectedUser) {
        reservationService.confirmReservationById(reservationId, connectedUser);
        return ResponseEntity.ok("Reservation confirmed");
    }

    @Operation(
            summary = "Delete Reservation by ID (ONLY OWNER)",
            description = "Endpoint dedicated to deleting reservations by owner "
    )
    @PreAuthorize("hasAuthority('OWNER')")
    @DeleteMapping(value = DELETE_RESERVATION)
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId, Principal connectedUSer) {
        reservationService.deleteReservationById(reservationId, connectedUSer);
        return ResponseEntity.noContent().build();
    }


    static final class Routes {
        static final String ROOT = "/api/v1/reservations";
        static final String ADD_RESERVATION = ROOT;
        static final String CONFIRM_RESERVATION = ROOT + "/{reservationId}";
        static final String DELETE_RESERVATION = ROOT + "/{reservationId}";
        static final String GET_RESERVATIONS_BY_CUSTOMER_ID = ROOT + "/customers/{customerId}";
        static final String GET_RESERVATIONS_BY_COTTAGE_ID = ROOT + "/cottages/{cottageId}";
    }
}
