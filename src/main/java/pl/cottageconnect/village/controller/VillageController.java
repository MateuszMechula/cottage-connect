package pl.cottageconnect.village.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.village.controller.dto.VillageDTO;
import pl.cottageconnect.village.controller.dto.mapper.VillageMapper;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.service.VillageService;

import java.security.Principal;
import java.util.List;

import static pl.cottageconnect.village.controller.VillageController.Routes.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "manage villages", description = "Endpoints for <b>OWNER</b> to manage their villages")
public class VillageController {

    private final VillageService villageService;
    private final VillageMapper villageMapper;

    @Operation(
            summary = "Get village by ID",
            description = "Retrieve information about a village based on its unique ID."
    )
    @GetMapping(value = FIND_BY_ID)
    public ResponseEntity<VillageDTO> getVillage(@PathVariable Long villageId, Principal connectedUser) {

        Village village = villageService.getVillage(villageId, connectedUser);
        VillageDTO villageDTO = villageMapper.mapToDTO(village);

        return ResponseEntity.status(HttpStatus.OK).body(villageDTO);
    }

    @Operation(
            summary = "Find All Villages",
            description = "Retrieve information about all Villages"
    )
    @GetMapping(value = ROOT)
    public ResponseEntity<List<VillageDTO>> findAllVillages(Principal connectedUser) {

        List<VillageDTO> allVillages = villageService.findAllVillage(connectedUser).stream()
                .map(villageMapper::mapToDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allVillages);
    }

    @Operation(
            summary = "Update village by ID",
            description = "Update information about a village based on its unique ID."
    )
    @PatchMapping(value = UPDATE)
    public ResponseEntity<VillageDTO> updateVillage(
            @PathVariable Long villageId,
            @RequestBody VillageDTO villageDTO,
            Principal connectedUser) {

        Village toUpdate = villageMapper.map(villageDTO);
        Village updatedVillage = villageService.updateVillage(villageId, toUpdate, connectedUser);
        log.info("Village with ID: {} is updated", villageId);
        VillageDTO updatedVillageDTO = villageMapper.mapToDTO(updatedVillage);

        return ResponseEntity.status(HttpStatus.OK).body(updatedVillageDTO);

    }

    @Operation(
            summary = "Add a new village",
            description = "Create a new village with the provided information."
    )
    @PostMapping(value = SAVE)
    public ResponseEntity<VillageDTO> addVillage(
            @RequestBody VillageDTO villageDTO,
            Principal connectedUser) {

        Village village = villageMapper.map(villageDTO);
        Village saved = villageService.addVillage(village, connectedUser);
        log.info("Village saved: {}", saved);
        VillageDTO dto = villageMapper.mapToDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Delete village by ID",
            description = "Delete a village based on its unique ID."
    )
    @DeleteMapping(value = DELETE_BY_ID)
    public ResponseEntity<String> deleteVillage(@PathVariable Long villageId) {

        villageService.deleteVillage(villageId);
        log.info("Village with ID: {} is deleted", villageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    static final class Routes {
        static final String VILLAGE_ID = "/{villageId}";
        static final String ROOT = "/api/v1/villages";
        static final String SAVE = ROOT;
        static final String UPDATE = ROOT + VILLAGE_ID;
        static final String FIND_BY_ID = ROOT + VILLAGE_ID;
        static final String DELETE_BY_ID = ROOT + VILLAGE_ID;
    }
}

