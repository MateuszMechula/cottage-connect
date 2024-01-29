package pl.cottageconnect.like.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.like.controller.dto.LikeDTO;
import pl.cottageconnect.like.controller.dto.mapper.LikeMapper;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.like.service.LikeService;
import pl.cottageconnect.security.configuration.JwtAuthFilter;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.like.controller.LikeController.Routes.ADD_LIKE;
import static pl.cottageconnect.like.controller.LikeController.Routes.DELETE_LIKE;
import static pl.cottageconnect.util.TestDataFactoryLike.testLike;
import static pl.cottageconnect.util.TestDataFactoryLike.testLikeDTO;

@WebMvcTest(LikeController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class LikeControllerTest {
    private final MockMvc mockMvc;
    @MockBean
    private final LikeService likeService;
    @MockBean
    private final LikeMapper likeMapper;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldAddLikeSuccessfully() throws Exception {
        //given
        Long likeableId = 1L;
        LikeableType type = LikeableType.COMMENT;
        Principal connectedUser = mock(Principal.class);
        Like like = testLike();
        LikeDTO likeDTO = testLikeDTO();

        when(likeService.addLike(likeableId, type, connectedUser)).thenReturn(like);
        when(likeMapper.mapToDTO(like)).thenReturn(likeDTO);
        //when,then
        mockMvc.perform(post(ADD_LIKE)
                        .principal(connectedUser)
                        .param("type", String.valueOf(type))
                        .param("likeableId", String.valueOf(likeableId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(likeDTO.type().toString()))
                .andExpect(jsonPath("$.likeableId").value(likeDTO.likeableId()));
    }

    @Test
    void shouldDeleteLikeSuccessfully() throws Exception {
        //given
        Long likeId = 1L;
        Principal connectedUser = mock(Principal.class);

        doNothing().when(likeService).deleteLike(likeId, connectedUser);
        //when,then
        mockMvc.perform(delete(DELETE_LIKE, likeId)
                        .principal(connectedUser))
                .andExpect(status().isNoContent());
    }
}