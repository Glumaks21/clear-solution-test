package clear.solution.practisetest.dto.mapper;

import clear.solution.practisetest.dto.AddressDTO;
import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.model.Address;
import clear.solution.practisetest.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final AddressMapper addressMapper = new AddressMapper();
    private final UserMapper userMapper = new UserMapper(addressMapper);

    @Test
    void should_map_to_dto() {
        var entity = User.builder()
                .id(1L).email("test").firstName("name").lastName("surname")
                .birthDate(LocalDate.of(1, 1, 1))
                .address(new Address("city", "country", null, null, null, null))
                .phoneNumber("1234567890")
                .build();
        var expected = new UserDTO(
                1L, "test", "name", "surname",
                LocalDate.of(1, 1, 1),
                new AddressDTO("city", "country", null, null, null, null),
                "1234567890");

        var actual = userMapper.toDTO(entity);

        assertEquals(expected, actual);
    }

    @Test
    void should_map_to_entity() {
        var dto = new UserDTO(
                1L, "test", "name", "surname",
                LocalDate.of(1, 1, 1),
                new AddressDTO("city", "country", null, null, null, null),
                "1234567890");
        var expectedAddress = new Address("city", "country", null, null, null, null);

        var actual = userMapper.toEntity(dto);

        assertEquals(actual.getId(), 1L);
        assertEquals(actual.getEmail(), "test");
        assertEquals(actual.getFirstName(), "name");
        assertEquals(actual.getLastName(), "surname");
        assertEquals(actual.getBirthDate(), LocalDate.of(1, 1, 1));
        assertEquals(actual.getPhoneNumber(), "1234567890");
        assertEquals(actual.getAddress(), expectedAddress);
    }

    @Test
    void fullUpdate() {
        var entity = User.builder()
                .id(1L).email("test").firstName("name").lastName("surname")
                .birthDate(LocalDate.of(1, 1, 1))
                .address(new Address("city", "country", null, null, null, null))
                .phoneNumber("1234567890")
                .build();
        var dto = new UserDTO(
                2L, "test2", null, "surname2",
                LocalDate.of(2, 2, 2),
                new AddressDTO(null, "country", null, null, null, null),
                "01234567890");

        userMapper.fullUpdate(dto, entity);

        assertEquals(entity.getId(), 1L);
        assertEquals(entity.getEmail(), "test2");
        assertNull(entity.getFirstName());
        assertEquals(entity.getLastName(), "surname2");
        assertEquals(entity.getBirthDate(), LocalDate.of(2, 2, 2));
        assertNull(entity.getAddress().getCity());
        assertEquals(entity.getPhoneNumber(), "01234567890");
    }

    @Test
    void partialUpdate() {
        var entity = User.builder()
                .id(1L).email("test").firstName("name").lastName("surname")
                .birthDate(LocalDate.of(1, 1, 1))
                .address(new Address("city", "country", null, null, null, null))
                .phoneNumber("1234567890")
                .build();
        var dto = new UserDTO(
                2L, "test2", null, "surname2",
                LocalDate.of(2, 2, 2),
                new AddressDTO(null, "country", null, null, null, null),
                "01234567890");

        userMapper.partialUpdate(dto, entity);

        assertEquals(1L, entity.getId());
        assertEquals("test2", entity.getEmail());
        assertEquals("name", entity.getFirstName());
        assertEquals("surname2", entity.getLastName());
        assertEquals(LocalDate.of(2, 2, 2), entity.getBirthDate());
        assertNull(entity.getAddress().getCity());
        assertEquals("01234567890", entity.getPhoneNumber());
    }
}