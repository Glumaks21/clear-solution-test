package clear.solution.practisetest.dto.mapper;

import clear.solution.practisetest.dto.AddressDTO;
import clear.solution.practisetest.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        if (address == null) return null;
        return new AddressDTO(
                address.getCity(),
                address.getCountry(),
                address.getPostCode(),
                address.getState(),
                address.getStreetName(),
                address.getStreetNumber());
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .city(dto.city())
                .country(dto.country())
                .postCode(dto.postCode())
                .state(dto.state())
                .streetName(dto.streetName())
                .streetNumber(dto.streetNumber())
                .build();
    }

    public void update(AddressDTO dto, Address address) {
        address.setCity(dto.city());
        address.setCountry(dto.country());
        address.setState(dto.state());
        address.setPostCode(dto.postCode());
        address.setStreetName(dto.streetName());
        address.setStreetNumber(dto.streetNumber());
    }
}
