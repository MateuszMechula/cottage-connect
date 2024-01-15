package pl.cottageconnect.village.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.village.controller.dto.VillagePostRequestDTO;
import pl.cottageconnect.village.controller.dto.VillagePostResponseDTO;
import pl.cottageconnect.village.controller.dto.mapper.VillagePostRequestMapper;
import pl.cottageconnect.village.controller.dto.mapper.VillagePostResponseMapper;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.service.VillagePostService;

import java.security.Principal;
import java.util.List;

import static pl.cottageconnect.village.controller.VillagePostController.Routes.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "village posts", description = "Endpoints for <b>OWNER</b> to manage their villages posts")
public class VillagePostController {

    private final VillagePostService villagePostService;
    private final VillagePostRequestMapper villagePostRequestMapper;
    private final VillagePostResponseMapper villagePostResponseMapper;

    @Operation(
            summary = "Get Village Post by ID",
            description = "Get information about a Village Post based on its unique ID."
    )
    @GetMapping(value = FIND_VILLAGE_POST_BY_ID)
    public ResponseEntity<VillagePostResponseDTO> findVillagePostById(
            @PathVariable Long villagePostId, Principal connectedUser) {

        VillagePost villagePost = villagePostService.findVillagePostById(villagePostId, connectedUser);
        VillagePostResponseDTO villagePostResponseDTO = villagePostResponseMapper.mapToDTO(villagePost);

        return ResponseEntity.status(HttpStatus.OK).body(villagePostResponseDTO);
    }

    @Operation(
            summary = "Find All Village Posts by Village ID",
            description = "Retrieve information about all Village Posts based on a unique village ID."
    )
    @GetMapping(value = FIND_ALL_BY_VILLAGE_ID)
    public ResponseEntity<List<VillagePostResponseDTO>> findAllVillagePosts(
            @PathVariable Long villageId, Principal connectedUser) {

        List<VillagePostResponseDTO> allVillagePosts = villagePostService.findAllVillagePosts(villageId, connectedUser)
                .stream()
                .map(villagePostResponseMapper::mapToDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(allVillagePosts);
    }

    @Operation(
            summary = "Update Village Post by ID",
            description = "Update information about a Village Post based on its unique ID."
    )
    @PatchMapping(value = UPDATE)
    public ResponseEntity<VillagePostResponseDTO> updateVillagePost(
            @PathVariable Long villagePostId,
            @RequestBody VillagePostRequestDTO villagePostRequestDTO,
            Principal connectedUser) {

        VillagePost toUpdate = villagePostRequestMapper.map(villagePostRequestDTO);
        VillagePost updatedVillage = villagePostService.updateVillagePost(villagePostId, toUpdate, connectedUser);
        log.info("Village Post with ID: {} is updated", villagePostId);
        VillagePostResponseDTO villagePostResponseDTO = villagePostResponseMapper.mapToDTO(updatedVillage);

        return ResponseEntity.status(HttpStatus.OK).body(villagePostResponseDTO);
    }

    @Operation(
            summary = "Add Village Post to Village by Village ID",
            description = "Add a new Village Post to a specific village."
    )
    @PostMapping(value = SAVE)
    public ResponseEntity<VillagePostResponseDTO> addVillagePost(
            @RequestBody VillagePostRequestDTO villagePostRequestDTO,
            @PathVariable Long villageId,
            Principal connectedUser) {

        VillagePost villagePost = villagePostRequestMapper.map(villagePostRequestDTO);
        VillagePost villagePostSaved = villagePostService.addVillagePost(villageId, connectedUser, villagePost);
        VillagePostResponseDTO villagePostResponseDTO = villagePostResponseMapper.mapToDTO(villagePostSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(villagePostResponseDTO);
    }

    @Operation(
            summary = "Delete Village Post",
            description = "Delete a Village Post based on its unique ID."
    )
    @DeleteMapping(value = DELETE_BY_ID)
    public ResponseEntity<String> deleteVillagePost(@PathVariable Long villagePostId, Principal principal) {

        villagePostService.deleteVillagePost(villagePostId, principal);

        return ResponseEntity.status(HttpStatus.OK).body("Village post deleted successfully");
    }

    static final class Routes {
        static final String ROOT = "/api/v1/village-posts";
        static final String ROOT_BY_VILLAGE = "/api/v1/villages/{villageId}/village-posts";
        static final String SAVE = ROOT_BY_VILLAGE;
        static final String FIND_ALL_BY_VILLAGE_ID = ROOT_BY_VILLAGE;
        static final String UPDATE = ROOT + "/{villagePostId}";
        static final String FIND_VILLAGE_POST_BY_ID = ROOT + "/{villagePostId}";
        static final String DELETE_BY_ID = ROOT + "/{villagePostId}";
    }
}
