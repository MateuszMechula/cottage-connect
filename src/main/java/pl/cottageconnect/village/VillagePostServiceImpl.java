package pl.cottageconnect.village;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pl.cottageconnect.village.VillagePostServiceImpl.ErrorMessages.VILLAGE_POST_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
class VillagePostServiceImpl implements VillagePostService {
    private final VillageServiceImpl villageServiceImpl;
    private final UserService userService;
    private final VillagePostDAO villagePostDAO;

    @Override
    public VillagePost findVillagePostById(Long villagePostId, Principal connectedUser) {
        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.userId();

        Optional<VillagePost> foundPost = villagePostDAO.findVillagePostById(villagePostId)
                .stream()
                .filter(villagePost -> villagePost.user().userId().equals(userId))
                .findFirst();

        if (foundPost.isEmpty()) {
            throw new NotFoundException(VILLAGE_POST_NOT_FOUND.formatted(villagePostId));
        }

        return foundPost.get();
    }

    @Override
    public List<VillagePost> findAllVillagePosts(Long villageId, Principal connectedUser) {
        villageServiceImpl.checkVillageId(villageId, connectedUser);
        return villagePostDAO.findAllVillagePostsByVillageId(villageId);
    }

    @Override
    @Transactional
    public VillagePost updateVillagePost(Long villagePostId, VillagePost toUpdate, Principal connectedUser) {
        VillagePost existingVillagePost = findVillagePostById(villagePostId, connectedUser);
        VillagePost updatedVillagePost = updateVillagePost(existingVillagePost, toUpdate);
        return villagePostDAO.saveVillagePost(updatedVillagePost);
    }

    @Override
    @Transactional
    public VillagePost addVillagePost(Long villageId, Principal connectedUser, VillagePost villagePost) {
        log.info("Adding VillagePost to Village with ID: {}", villageId);
        User user = userService.getConnectedUser(connectedUser);
        Village village = villageServiceImpl.getVillage(villageId, connectedUser);

        VillagePost toSave = buildVillagePost(villagePost, user, village);
        return villagePostDAO.saveVillagePost(toSave);
    }

    @Override
    @Transactional
    public void deleteVillagePost(Long villagePostId, Principal connectedUser) {
        findVillagePostById(villagePostId, connectedUser);
        villagePostDAO.deleteVillagePost(villagePostId);
    }

    private static VillagePost buildVillagePost(VillagePost villagePost, User user, Village village) {
        return VillagePost.builder()
                .title(villagePost.title())
                .content(villagePost.content())
                .createdAt(LocalDateTime.now())
                .user(user)
                .village(village)
                .build();
    }

    private VillagePost updateVillagePost(VillagePost existing, VillagePost update) {
        return VillagePost.builder()
                .villagePostId(existing.villagePostId())
                .title(update.title() != null ? update.title() : existing.title())
                .content(update.content() != null ? update.content() : existing.content())
                .createdAt(existing.createdAt())
                .user(existing.user())
                .village(existing.village())
                .build();
    }

    static final class ErrorMessages {
        static final String VILLAGE_POST_NOT_FOUND =
                "VillagePost with ID: [%s] not found or user does not have access";
    }

}
