package clear.solution.practisetest.dto.mapper;

import clear.solution.practisetest.dto.AddressDTO;
import clear.solution.practisetest.model.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    private final AddressMapper mapper = new AddressMapper();

    @Test
    void should_map_to_dto() {
        var entity = new Address("city", "country", "postcode", "state", "streetName", "streetNumber");
        var expected = new AddressDTO("city", "country", "postcode", "state", "streetName", "streetNumber");

        var actual = mapper.toDTO(entity);

        assertEquals(expected, actual);
    }

    @Test
    void should_map_to_entity() {
        var dto = new AddressDTO("city", "country", "postcode", "state", "streetName", "streetNumber");

        var actual = mapper.toEntity(dto);

        assertEquals("city", actual.getCity());
        assertEquals("country", actual.getCountry());
        assertEquals("postcode", actual.getPostCode());
        assertEquals("state", actual.getState());
        assertEquals("streetName", actual.getStreetName());
        assertEquals("streetNumber", actual.getStreetNumber());
    }

    @Test
    void should_update() {
        var entity = new Address("city", "country", "postcode", "state", "streetName", "streetNumber");
        var dto = new AddressDTO("city1", null, "postcode1", null, "streetName", null);

        mapper.update(dto, entity);

        assertEquals("city1", entity.getCity());
        assertEquals("postcode1", entity.getPostCode());
        assertEquals("streetName", entity.getStreetName());
        assertNull(entity.getCountry());
        assertNull(entity.getState());
        assertNull(entity.getStreetNumber());
    }
}