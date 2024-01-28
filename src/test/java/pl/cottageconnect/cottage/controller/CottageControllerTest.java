package pl.cottageconnect.cottage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.cottage.controller.dto.CottageDTO;
import pl.cottageconnect.cottage.controller.mapper.CottageMapper;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.security.configuration.JwtAuthFilter;

import java.security.Principal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.cottage.controller.CottageController.Routes.*;
import static pl.cottageconnect.util.TestDataFactoryCottage.*;

@WebMvcTest(CottageController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CottageControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockBean
    private CottageMapper cottageMapper;
    @MockBean
    private CottageService cottageService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldGetCottageSuccessfully() throws Exception {
        //given
        Long cottageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Cottage cottage = testCottage();
        CottageDTO cottageDTO = testCottageDTO();

        when(cottageService.getCottageWithCheck(cottageId, connectedUser)).thenReturn(cottage);
        when(cottageMapper.mapToDTO(cottage)).thenReturn(cottageDTO);
        //when, then
        mockMvc.perform(get(FIND_COTTAGE_BY_ID, cottageId)
                        .principal(connectedUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cottageId").value(cottage.getCottageId()))
                .andExpect(jsonPath("$.cottageNumber").value(cottage.getCottageNumber()))
                .andExpect(jsonPath("$.cottageSize").value(cottage.getCottageSize()))
                .andExpect(jsonPath("$.price").value(cottage.getPrice()))
                .andExpect(jsonPath("$.description").value(cottage.getDescription()));
    }

    @Test
    void shouldFindAllCottagesSuccessfully() throws Exception {
        //given
        Long villageId = 1L;
        Principal connectedUser = mock(Principal.class);
        List<Cottage> cottages = List.of(testCottage2(), testCottage3());


        when(cottageService.findAllCottages(villageId, connectedUser)).thenReturn(cottages);
        when(cottageMapper.mapToDTO(cottages.get(0))).thenReturn(testCottageDTO());
        when(cottageMapper.mapToDTO(cottages.get(1))).thenReturn(testCottageDTO());
        //when, then
        mockMvc.perform(get(FIND_COTTAGES_BY_VILLAGE_ID, villageId)
                        .principal(connectedUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(cottages.size())));
    }

    @Test
    void shouldUpdateCottageSuccessfully() throws Exception {
        //given
        Long cottageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Cottage cottage = testCottage();
        CottageDTO cottageDTO = testCottageDTO();

        when(cottageMapper.map(cottageDTO)).thenReturn(cottage);
        when(cottageService.updateCottage(cottageId, cottage, connectedUser)).thenReturn(cottage);
        when(cottageMapper.mapToDTO(cottage)).thenReturn(cottageDTO);
        //when,then
        mockMvc.perform(patch(UPDATE_COTTAGE, cottageId)
                        .principal(connectedUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cottageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cottageId").value(cottage.getCottageId()))
                .andExpect(jsonPath("$.cottageNumber").value(cottage.getCottageNumber()))
                .andExpect(jsonPath("$.cottageSize").value(cottage.getCottageSize()))
                .andExpect(jsonPath("$.price").value(cottage.getPrice()))
                .andExpect(jsonPath("$.description").value(cottage.getDescription()));
    }

    @Test
    void shouldAddCottageSuccessfully() throws Exception {
        //given
        Long villageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Cottage cottage = testCottage();
        CottageDTO cottageDTO = testCottageDTO();

        when(cottageMapper.map(cottageDTO)).thenReturn(cottage);
        doNothing().when(cottageService).addCottage(any(Long.class), any(Cottage.class), any(Principal.class));
        when(cottageMapper.mapToDTO(cottage)).thenReturn(cottageDTO);
        //when,then
        mockMvc.perform(post(SAVE_COTTAGE, villageId)
                        .principal(connectedUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cottageDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteCottageSuccessfully() throws Exception {
        //given
        Long cottageId = 1L;
        Principal connectedUser = mock(Principal.class);

        doNothing().when(cottageService).deleteCottage(anyLong(), any(Principal.class));
        //when,then
        mockMvc.perform(delete(DELETE_BY_ID, cottageId)
                        .principal(connectedUser))
                .andExpect(status().isNoContent());

        verify(cottageService, times(1))
                .deleteCottage(cottageId, connectedUser);
    }
}