package pl.cottageconnect.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.CommentService;
import pl.cottageconnect.comment.CommentableType;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.controller.mapper.CommentRequestMapper;
import pl.cottageconnect.comment.controller.mapper.CommentResponseMapper;
import pl.cottageconnect.security.JwtAuthFilter;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.comment.TestDataFactoryComment.*;
import static pl.cottageconnect.comment.controller.CommentController.Routes.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CommentControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockBean
    private CommentService commentServiceImpl;
    @MockBean
    private CommentRequestMapper commentRequestMapper;
    @MockBean
    private CommentResponseMapper commentResponseMapper;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldGetAllCommentsSuccessfully() throws Exception {
        // given
        Long commentableId = 1L;
        CommentableType type = CommentableType.VILLAGE;
        Pageable pageable = PageRequest.of(0, 1);
        Principal connectedUser = mock(Principal.class);

        Comment comment = Comment.builder()
                .commentableId(commentableId)
                .content(testComment().content())
                .type(testComment().type())
                .rating(testComment().rating())
                .build();

        CommentResponseDTO commentResponseDTO = testCommentResponseDTO();

        List<CommentResponseDTO> commentResponseDTOs = List.of(commentResponseDTO);
        Page<CommentResponseDTO> commentResponseDTOPage = new PageImpl<>(commentResponseDTOs, pageable, 1);


        when(commentServiceImpl.getCommentsByCommentableId(anyLong(), any(CommentableType.class), any(Principal.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(List.of(comment), pageable, 1));
        when(commentResponseMapper.mapToDTO(comment)).thenReturn(commentResponseDTO);

        // when & then
        mockMvc.perform(get(GET_ALL_COMMENTS_BY_COMMENTABLE_ID)
                        .param("commentableId", String.valueOf(commentableId))
                        .param("type", type.name())
                        .principal(connectedUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value(commentResponseDTO.content()))
                .andExpect(jsonPath("$.content[0].rating").value(commentResponseDTO.rating()))
                .andExpect(jsonPath("$.content[0].type").value(commentResponseDTO.type().toString()))
                .andExpect(jsonPath("$.pageable.pageNumber").value(pageable.getPageNumber()))
                .andExpect(jsonPath("$.pageable.pageSize").value(pageable.getPageSize()))
                .andExpect(jsonPath("$.totalPages").value(commentResponseDTOPage.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value((int) commentResponseDTOPage.getTotalElements()));
    }

    @Test
    void shouldAddCommentSuccessfully() throws Exception {
        //given
        Long commentableId = 1L;
        CommentRequestDTO commentRequestDTO = testCommentRequestDTO();
        CommentResponseDTO commentResponseDTO = testCommentResponseDTO();
        CommentableType type = CommentableType.VILLAGE;
        Comment comment = testComment();
        Principal connectedUser = mock(Principal.class);

        when(commentRequestMapper.map(commentRequestDTO)).thenReturn(comment);
        when(commentServiceImpl.addCommentToCommentable(commentableId, type, comment, connectedUser))
                .thenReturn(comment);
        when(commentResponseMapper.mapToDTO(comment)).thenReturn(commentResponseDTO);

        //when, then
        mockMvc.perform(post(ADD_COMMENT)
                        .param("commentableId", String.valueOf(commentableId))
                        .param("type", String.valueOf(type))
                        .principal(connectedUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(commentResponseDTO.content()))
                .andExpect(jsonPath("$.type").value(commentResponseDTO.type().toString()))
                .andExpect(jsonPath("$.rating").value(commentResponseDTO.rating()));
    }

    @Test
    void shouldUpdateCommentSuccessfully() throws Exception {
        //given
        Long commentId = 1L;
        CommentRequestDTO commentRequestDTO = testCommentRequestDTO();
        CommentResponseDTO commentResponseDTO = testCommentResponseDTO();
        Comment commentToUpdate = testComment();
        Principal connectedUser = mock(Principal.class);

        when(commentRequestMapper.map(commentRequestDTO)).thenReturn(commentToUpdate);
        when(commentServiceImpl.updateComment(commentId, commentToUpdate, connectedUser)).thenReturn(commentToUpdate);
        when(commentResponseMapper.mapToDTO(commentToUpdate)).thenReturn(commentResponseDTO);

        //when, then
        mockMvc.perform(patch(UPDATE_COMMENT_BY_ID.replace("{commentId}", commentId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDTO))
                        .principal(connectedUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(commentResponseDTO.content()))
                .andExpect(jsonPath("$.type").value(commentResponseDTO.type().toString()))
                .andExpect(jsonPath("$.rating").value(commentResponseDTO.rating()));

    }

    @Test
    void shouldDeleteCommentSuccessfully() throws Exception {
        //given
        Long commentId = 1L;
        Principal connectedUser = mock(Principal.class);

        doNothing().when(commentServiceImpl).deleteCommentById(commentId, connectedUser);
        //when, then
        mockMvc.perform(delete(DELETE_COMMENT_BY_ID.replace("{commentId}", commentId.toString()))
                        .principal(connectedUser))
                .andExpect(status().isNoContent());
    }
}