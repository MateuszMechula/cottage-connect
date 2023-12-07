package pl.cottageconnect.address.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String street;
    private String postalCode;
    private String city;
    private String voivodeship;
    private String country;
}
