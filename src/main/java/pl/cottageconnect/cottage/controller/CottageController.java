package pl.cottageconnect.cottage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.cottage.controller.dto.CottageDTO;
import pl.cottageconnect.cottage.controller.mapper.CottageMapper;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.service.CottageService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static pl.cottageconnect.cottage.controller.CottageController.Routes.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "cottages", description = "Endpoints for <b>OWNER</b> to manage their cottages")
public class CottageController {

    private final CottageMapper cottageMapper;
    private final CottageService cottageService;

    @Operation(
            summary = "Get cottage by ID",
            description = "Retrieve information about a cottage based on its unique ID."
    )
    @GetMapping(value = FIND_COTTAGE_BY_ID)
    public ResponseEntity<CottageDTO> getCottage(@PathVariable Long cottageId, Principal connectedUser) {

        Cottage cottage = cottageService.getCottageWithCheck(cottageId, connectedUser);
        CottageDTO cottageDTO = cottageMapper.mapToDTO(cottage);
        return ResponseEntity.status(HttpStatus.OK).body(cottageDTO);
    }

    @Operation(
            summary = "Find All Cottages for Village by ID",
            description = "Retrieve information about all cottages by village ID"
    )
    @GetMapping(value = FIND_COTTAGES_BY_VILLAGE_ID)
    public ResponseEntity<List<CottageDTO>> findAllCottages(@PathVariable Long villageId, Principal connectedUser) {
        List<Cottage> cottages = cottageService.findAllCottages(villageId, connectedUser);
        List<CottageDTO> cottageDTOs = cottages.stream()
                .map(cottageMapper::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(cottageDTOs);
    }

    @Operation(
            summary = "Update cottage by ID",
            description = "Update information about a cottage based on its unique ID."
    )
    @PatchMapping(value = UPDATE_COTTAGE)
    public ResponseEntity<CottageDTO> updateCottage(
            @PathVariable Long cottageId,
            @RequestBody CottageDTO cottageDTO,
            Principal connectedUser) {

        Cottage toUpdate = cottageMapper.map(cottageDTO);
        Cottage updatedCottage = cottageService.updateCottage(cottageId, toUpdate, connectedUser);
        CottageDTO updatedCottageDTO = cottageMapper.mapToDTO(updatedCottage);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCottageDTO);
    }

    @Operation(
            summary = "Add a new cottage",
            description = "Create a new cottage with the provided information."
    )
    @PostMapping(value = SAVE_COTTAGE)
    public ResponseEntity<?> addCottage(
            @PathVariable Long villageId,
            @RequestBody CottageDTO cottageDTO,
            Principal connectedUser
    ) {
        Cottage cottage = cottageMapper.map(cottageDTO);
        cottageService.addCottage(villageId, cottage, connectedUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Delete cottage by ID",
            description = "Delete a cottage based on its unique ID."
    )
    @DeleteMapping(value = DELETE_BY_ID)
    public ResponseEntity<Void> deleteCottage(@PathVariable Long cottageId, Principal connectedUser) {

        cottageService.deleteCottage(cottageId, connectedUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    static final class Routes {
        static final String VILLAGE_ID = "/{villageId}";
        static final String COTTAGE_ID = "/{cottageId}";
        static final String ROOT = "/api/v1/cottages";
        static final String VILLAGE_ROOT = "/api/v1/villages";
        static final String FIND_COTTAGE_BY_ID = ROOT + COTTAGE_ID;
        static final String FIND_COTTAGES_BY_VILLAGE_ID = VILLAGE_ROOT + VILLAGE_ID + "/cottages";
        static final String UPDATE_COTTAGE = ROOT + COTTAGE_ID;
        static final String SAVE_COTTAGE = ROOT + VILLAGE_ID;
        static final String DELETE_BY_ID = ROOT + COTTAGE_ID;

    }

}
