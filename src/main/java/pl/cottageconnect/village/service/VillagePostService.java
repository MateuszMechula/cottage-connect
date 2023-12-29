package pl.cottageconnect.village.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.service.dao.VillagePostDAO;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pl.cottageconnect.village.service.VillagePostService.ErrorMessages.VILLAGE_POST_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class VillagePostService {
    private final VillageService villageService;
    private final UserService userService;
    private final VillagePostDAO villagePostDAO;

    public VillagePost findVillagePostById(Long villagePostId, Principal connectedUser) {
        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.getUserId();

        Optional<VillagePost> foundPost = villagePostDAO.findVillagePostById(villagePostId)
                .stream()
                .filter(villagePost -> villagePost.getUser().getUserId().equals(userId))
                .findFirst();

        if (foundPost.isEmpty()) {
            throw new NotFoundException(VILLAGE_POST_NOT_FOUND.formatted(villagePostId));
        }
        return foundPost.get();
    }


    public List<VillagePost> findAllVillagePosts(Long villageId, Principal connectedUser) {
        villageService.checkVillageId(villageId, connectedUser);
        return villagePostDAO.findAllVillagePostsByVillageId(villageId);
    }

    @Transactional
    public VillagePost updateVillagePost(Long villagePostId, VillagePost toUpdate, Principal connectedUser) {
        VillagePost existingVillagePost = findVillagePostById(villagePostId, connectedUser);
        VillagePost updatedVillagePost = updateVillagePost(existingVillagePost, toUpdate);
        return villagePostDAO.saveVillagePost(updatedVillagePost);
    }

    @Transactional
    public VillagePost addVillagePost(Long villageId, Principal connectedUser, VillagePost villagePost) {
        log.info("Adding VillagePost to Village with ID: {}", villageId);
        User user = userService.getConnectedUser(connectedUser);
        Village village = villageService.getVillage(villageId, connectedUser);

        VillagePost toSave = buildVillagePost(villagePost, user, village);
        return villagePostDAO.saveVillagePost(toSave);
    }

    @Transactional
    public void deleteVillagePost(Long villagePostId, Principal connectedUser) {
        findVillagePostById(villagePostId, connectedUser);
        villagePostDAO.deleteVillagePost(villagePostId);
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

    static final class ErrorMessages {
        static final String VILLAGE_POST_NOT_FOUND =
                "VillagePost with ID: [%s] not found or user does not have access";
    }

}
