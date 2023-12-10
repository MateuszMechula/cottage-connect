package pl.cottageconnect.village.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.exception.MissingUserException;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.exception.VillagePostNotFoundException;
import pl.cottageconnect.village.service.dao.VillagePostDAO;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VillagePostService {
    private final VillageService villageService;
    private final UserService userService;
    private final VillagePostDAO villagePostDAO;

    public VillagePost findVillagePostById(Long villagePostId) {
        return villagePostDAO.findVillagePostById(villagePostId)
                .orElseThrow(() -> new VillagePostNotFoundException("VillagePost with ID: [%s] not found"
                        .formatted(villagePostId)));
    }

    public List<VillagePost> findAllVillagePosts(Long villageId) {
        return villagePostDAO.findAllVillagePostsByVillageId(villageId);
    }

    @Transactional
    public VillagePost updateVillagePost(Long villagePostId, VillagePost toUpdate) {
        VillagePost existingVillagePost = findVillagePostById(villagePostId);
        VillagePost updatedVillagePost = updateVillagePost(existingVillagePost, toUpdate);
        return villagePostDAO.saveVillagePost(updatedVillagePost);
    }

    @Transactional
    public VillagePost addVillagePost(Long villageId, Principal connectedUser, VillagePost villagePost) {
        log.info("Adding VillagePost to Village with ID: {}", villageId);
        Village village = villageService.getVillage(villageId);
        if (connectedUser == null) {
            throw new MissingUserException("Connected user is null");
        }
        String email = Objects.requireNonNull(connectedUser.getName());
        User user = userService.getUserByUsername(email);
        VillagePost toSave = buildVillagePost(villagePost, user, village);
        return villagePostDAO.saveVillagePost(toSave);
    }

    @Transactional
    public void deleteVillagePost(Long villageId) {
        villagePostDAO.deleteVillagePost(villageId);
    }

    private static VillagePost buildVillagePost(VillagePost villagePost, User user, Village village) {
        return VillagePost.builder()
                .title(villagePost.getTitle())
                .content(villagePost.getContent())
                .createdAt(LocalDateTime.now())
                .user(user)
                .village(village)
                .build();
    }

    private VillagePost updateVillagePost(VillagePost existing, VillagePost update) {
        return VillagePost.builder()
                .villagePostId(existing.getVillagePostId())
                .title(update.getTitle() != null ? update.getTitle() : existing.getTitle())
                .content(update.getContent() != null ? update.getContent() : existing.getContent())
                .createdAt(existing.getCreatedAt())
                .user(existing.getUser())
                .village(existing.getVillage())
                .build();
    }
}
