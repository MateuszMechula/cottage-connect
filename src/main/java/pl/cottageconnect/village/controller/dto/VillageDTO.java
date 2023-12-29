package pl.cottageconnect.village.controller.dto;

import lombok.Builder;
import pl.cottageconnect.address.controller.dto.AddressDTO;

@Builder
public record VillageDTO(Long villageId, String name, String description, AddressDTO addressDTO) {
}
