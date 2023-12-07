package pl.cottageconnect.village.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.village.controller.dto.VillageDTO;
import pl.cottageconnect.village.controller.dto.mapper.VillageMapper;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.service.VillageService;

import java.security.Principal;

import static pl.cottageconnect.village.controller.VillageController.BASE_PATH;

@Slf4j
@RestController
@RequestMapping(value = BASE_PATH)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "owner", description = "Endpoints for owner to manage their villages")
public class VillageController {
    public static final String BASE_PATH = "/api/v1/villages";
    public static final String VILLAGE_ID = "/{villageId}";
    private final VillageService villageService;
    private final VillageMapper villageMapper;


    @Operation(
            summary = "Get village by ID",
            description = "Retrieve information about a village based on its unique ID."
    )
    @GetMapping(value = VILLAGE_ID)
    public VillageDTO getVillage(@PathVariable Long villageId) {
        Village village = villageService.getVillage(villageId);
        return villageMapper.mapToDTO(village);
    }

    @Operation(
            summary = "Update village by ID",
            description = "Update information about a village based on its unique ID."
    )
    @PatchMapping(value = VILLAGE_ID)
    public VillageDTO updateVillage(@PathVariable Long villageId, @RequestBody VillageDTO villageDTO) {
        Village toUpdate = villageMapper.map(villageDTO);
        Village updatedVillage = villageService.updateVillage(villageId, toUpdate);
        log.info("Village with ID: {} is updated", villageId);
        return villageMapper.mapToDTO(updatedVillage);
    }

    @Operation(
            summary = "Add a new village",
            description = "Create a new village with the provided information."
    )
    @PostMapping
    public VillageDTO addVillage(
            @RequestBody VillageDTO villageDTO,
            Principal connectedUser) {
        Village village = villageMapper.map(villageDTO);
        Village saved = villageService.addVillage(village, connectedUser);
        log.info("Village saved: {}", saved);
        return villageMapper.mapToDTO(saved);
    }

    @Operation(
            summary = "Delete village by ID",
            description = "Delete a village based on its unique ID."
    )
    @DeleteMapping(value = VILLAGE_ID)
    public void deleteVillage(@PathVariable Long villageId) {
        villageService.deleteVillage(villageId);
        log.info("Village with ID: {} is deleted", villageId);
    }
}

