package clear.solution.practisetest.service.impl;

import clear.solution.practisetest.config.UserValidationProperties;
import clear.solution.practisetest.dto.AddressDTO;
import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.dto.mapper.UserMapper;
import clear.solution.practisetest.exception.ResourceInConflictException;
import clear.solution.practisetest.exception.ResourceNotFoundException;
import clear.solution.practisetest.model.Address;
import clear.solution.practisetest.model.User;
import clear.solution.practisetest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserMapper mapper;

    @Mock
    private UserRepository userRepo;

    @Mock
    private UserValidationProperties props;


    @Test
    void should_get_all() {
        var u1 = new User();
        u1.setId(1L);
        var u2 = new User();
        u1.setId(2L);
        var ud1 = new UserDTO(1L, null, null, null, null, null, null);
        var ud2 = new UserDTO(2L, null, null, null, null, null, null);

        var pageable = PageRequest.of(10, 10);
        when(userRepo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(u1, u2)));
        when(mapper.toDTO(u1)).thenReturn(ud1);
        when(mapper.toDTO(u2)).thenReturn(ud2);
        var expected = new PageImpl<>(List.of(ud1, ud2));

        var actual = service.getAll(pageable);

        verify(userRepo, times(1)).findAll(pageable);
        verify(mapper, times(2)).toDTO(any(User.class));

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void should_get_all_by_birth_date_between() {
        var u1 = new User();
        u1.setId(1L);
        var ud1 = new UserDTO(1L, null, null, null, null, null, null);

        var pageable = PageRequest.of(10, 10);
        var from = LocalDate.of(1, 1, 1);
        var to = from.plusDays(10);
        when(userRepo.findAllByBirthDateBetween(from, to, pageable))
                .thenReturn(new PageImpl<>(List.of(u1)));
        when(mapper.toDTO(u1)).thenReturn(ud1);
        var expected = new PageImpl<>(List.of(ud1));

        var actual = service.getAllByBirthDateBetween(from, to, pageable);

        verify(userRepo, times(1)).findAllByBirthDateBetween(from, to, pageable);
        verify(mapper, times(1)).toDTO(any(User.class));

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void should_throw_ex_if_age_when_get_all_by_birth_date_between() {
        var pageable = PageRequest.of(10, 10);
        var from = LocalDate.now();
        var to = from.minusDays(10);

        assertThrows(IllegalArgumentException.class, () -> service.getAllByBirthDateBetween(from, to, pageable));

        verify(userRepo, never()).findAllByBirthDateBetween(from, to, pageable);
    }

    @Test
    void should_get_by_id() {
        var u1 = new User();
        var ud1 = new UserDTO(1L, null, null, null, null, null, null);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u1));
        when(mapper.toDTO(u1)).thenReturn(ud1);

        var actual = service.getById(1L);

        verify(userRepo, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(u1);

        assertEquals(ud1, actual);
    }

    @Test
    void throw_not_found_when_get_by_d() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void should_create() {
        var u1 = new User();
        u1.setEmail("test");
        var birthDate = LocalDate.now().minusYears(18);
        u1.setBirthDate(birthDate);
        var ud1 = new UserDTO(null, "test", null, null, birthDate, null, null);
        var ud2 = new UserDTO(1L, "test", null, null, birthDate, null, null);

        when(props.age()).thenReturn(18);
        when(userRepo.existsByEmail("test")).thenReturn(false);
        when(mapper.toEntity(ud1)).thenReturn(u1);
        when(userRepo.save(u1)).thenReturn(u1);
        when(mapper.toDTO(u1)).thenReturn(ud2);

        var actual = service.create(ud1);

        verify(userRepo, times(1)).existsByEmail("test");
        verify(mapper, times(1)).toEntity(ud1);
        verify(userRepo, times(1)).save(u1);
        verify(mapper, times(1)).toDTO(u1);

        assertEquals(ud2, actual);
    }

    @Test
    void should_throw_conflict_if_exists_by_email_when_create() {
        var birthDate = LocalDate.now();
        var ud1 = new UserDTO(null, "test", null, null, birthDate, null, null);

        when(userRepo.existsByEmail("test")).thenReturn(false);
        when(props.age()).thenReturn(18);

        assertThrows(IllegalArgumentException.class, () -> service.create(ud1));
    }

    @Test
    void should_throw_if_exists_by_email_when_create() {
        var ud1 = new UserDTO(null, "test", null, null, null, null, null);

        when(userRepo.existsByEmail("test")).thenReturn(true);

        assertThrows(ResourceInConflictException.class, () -> service.create(ud1));
    }

    @Test
    void should_full_update() {
        var u1 = User.builder()
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

        when(userRepo.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepo.existsByEmail("test2")).thenReturn(false);
        when(userRepo.save(u1)).thenReturn(u1);
        when(mapper.toDTO(u1)).thenReturn(dto);

        var actual = service.fullUpdate(1L, dto);

        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).existsByEmail("test2");
        verify(mapper, times(1)).fullUpdate(dto, u1);
        verify(userRepo, times(1)).save(u1);
        verify(mapper, times(1)).toDTO(u1);

        assertEquals(dto, actual);
    }

    @Test
    void should_partial_update() {
        var u1 = User.builder()
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
        var updated = new UserDTO(
                1L, "test2", "name", "surname2",
                LocalDate.of(2, 2, 2),
                new AddressDTO(null, "country", null, null, null, null),
                "01234567890");

        when(userRepo.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepo.existsByEmail("test2")).thenReturn(false);
        when(userRepo.save(u1)).thenReturn(u1);
        when(mapper.toDTO(u1)).thenReturn(updated);

        var actual = service.partialUpdate(1L, dto);

        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).existsByEmail("test2");
        verify(mapper, times(1)).partialUpdate(dto, u1);
        verify(userRepo, times(1)).save(u1);
        verify(mapper, times(1)).toDTO(u1);

        assertEquals(updated, actual);
    }

    @Test
    void should_throw_conflict_if_email_exists_when_update() {
        var u1 = new User();
        u1.setId(1L);
        var ud1 = new UserDTO(null, "test", null, null, null, null, null);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepo.existsByEmail("test")).thenReturn(true);

        assertThrows(ResourceInConflictException.class, () -> service.fullUpdate(1L, ud1));
        assertThrows(ResourceInConflictException.class, () -> service.partialUpdate(1L, ud1));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        verify(userRepo, times(1)).deleteById(1L);
    }
}