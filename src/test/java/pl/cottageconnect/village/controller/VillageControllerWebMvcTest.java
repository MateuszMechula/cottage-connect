package pl.cottageconnect.village.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.security.JwtAuthFilter;
import pl.cottageconnect.village.Village;
import pl.cottageconnect.village.VillageService;
import pl.cottageconnect.village.controller.dto.VillageDTO;
import pl.cottageconnect.village.controller.dto.mapper.VillageMapper;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.village.controller.VillageController.Routes.FIND_BY_ID;
import static pl.cottageconnect.village.util.TestDataFactoryVillage.testVillage;
import static pl.cottageconnect.village.util.TestDataFactoryVillage.testVillageDTO;

@WebMvcTest(value = VillageController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class VillageControllerWebMvcTest {

    private MockMvc mockMvc;
    @MockBean
    private VillageService villageService;
    @MockBean
    private VillageMapper villageMapper;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldGetVillage() throws Exception {
        //given
        Long villageId = 1L;
        VillageDTO villageDTO = testVillageDTO();
        Village village = testVillage();
        Principal principal = mock(Principal.class);

        when(villageService.getVillage(villageId, principal)).thenReturn(village);
        when(villageMapper.mapToDTO(village)).thenReturn(villageDTO);

        //when,then
        mockMvc.perform(get(FIND_BY_ID, villageId)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(villageDTO.name()))
                .andExpect(jsonPath("$.description").value(villageDTO.description()))
                .andExpect(jsonPath("$.addressDTO.street").value(villageDTO.addressDTO().street()))
                .andExpect(jsonPath("$.addressDTO.postalCode").value(villageDTO.addressDTO().postalCode()))
                .andExpect(jsonPath("$.addressDTO.city").value(villageDTO.addressDTO().city()))
                .andExpect(jsonPath("$.addressDTO.voivodeship").value(villageDTO.addressDTO().voivodeship()))
                .andExpect(jsonPath("$.addressDTO.country").value(villageDTO.addressDTO().country()));
    }

    @Test
    void updateVillage() {
    }

    @Test
    void addVillage() {
    }

    @Test
    void deleteVillage() {
    }
}