package pl.cottageconnect.village.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.village.controller.dto.VillagePostRequestDTO;
import pl.cottageconnect.village.controller.dto.VillagePostResponseDTO;
import pl.cottageconnect.village.controller.dto.mapper.VillagePostRequestMapper;
import pl.cottageconnect.village.controller.dto.mapper.VillagePostResponseMapper;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.service.VillagePostService;

import java.security.Principal;
import java.util.List;

import static pl.cottageconnect.village.controller.VillagePostController.BASE_PATH;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BASE_PATH)
@SecurityRequirement(name = "bearer-token")
@Tag(name = "manage village posts", description = "Endpoints for <b>OWNER</b> to manage their villages posts")
public class VillagePostController {
    public static final String BASE_PATH = "/api/v1/village-posts";
    public static final String VILLAGE_ID = "/village/{villageId}";
    public static final String VILLAGE_POST_ID = "/{villagePostId}";
    private final VillagePostService villagePostService;
    private final VillagePostRequestMapper villagePostRequestMapper;
    private final VillagePostResponseMapper villagePostResponseMapper;

    @Operation(
            summary = "Find All Village Posts by ID",
            description = "Retrieve information about all Village Posts based on a unique village ID."
    )
    @GetMapping(value = VILLAGE_ID)
    public List<VillagePostResponseDTO> findAllVillagePosts(@PathVariable Long villageId) {
        return villagePostService.findAllVillagePosts(villageId).stream()
                .map(villagePostResponseMapper::mapToDTO)
                .toList();
    }

    @Operation(
            summary = "Get Village Post by ID",
            description = "Get information about a Village Post based on its unique ID."
    )
    @GetMapping(value = VILLAGE_POST_ID)
    public VillagePostResponseDTO findVillagePostById(@PathVariable Long villagePostId) {
        VillagePost villagePost = villagePostService.findVillagePostById(villagePostId);
        return villagePostResponseMapper.mapToDTO(villagePost);
    }

    @Operation(
            summary = "Update village post by ID",
            description = "Update information about a village post based on its unique ID."
    )
    @PatchMapping(value = VILLAGE_POST_ID)
    public VillagePostResponseDTO updateVillagePost(
            @PathVariable Long villagePostId,
            @RequestBody VillagePostRequestDTO villagePostRequestDTO) {

        VillagePost toUpdate = villagePostRequestMapper.map(villagePostRequestDTO);
        VillagePost updatedVillage = villagePostService.updateVillagePost(villagePostId, toUpdate);
        log.info("Village Post with ID: {} is updated", villagePostId);
        return villagePostResponseMapper.mapToDTO(updatedVillage);
    }

    @Operation(
            summary = "Add Village Post",
            description = "Add a new Village Post to a specific village."
    )
    @PostMapping(value = VILLAGE_ID)
    public VillagePostResponseDTO addVillagePost(
            @RequestBody VillagePostRequestDTO villagePostRequestDTO,
            @PathVariable Long villageId,
            Principal connectedUser) {
        VillagePost villagePost = villagePostRequestMapper.map(villagePostRequestDTO);
        VillagePost villagePostSaved = villagePostService.addVillagePost(villageId, connectedUser, villagePost);
        return villagePostResponseMapper.mapToDTO(villagePostSaved);
    }

    @Operation(
            summary = "Delete Village Post",
            description = "Delete a Village Post based on its unique ID."
    )
    @DeleteMapping(value = VILLAGE_POST_ID)
    public void deleteVillagePost(@PathVariable Long villagePostId) {
        villagePostService.deleteVillagePost(villagePostId);
    }
}
