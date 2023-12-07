package pl.cottageconnect.village.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.cottageconnect.address.controller.dto.AddressDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VillageDTO {

    private String name;
    private String description;
    private AddressDTO addressDTO;
}
