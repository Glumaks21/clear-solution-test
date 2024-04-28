package clear.solution.practisetest.controller;

import clear.solution.practisetest.dto.AddressDTO;
import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.exception.ResourceInConflictException;
import clear.solution.practisetest.exception.ResourceNotFoundException;
import clear.solution.practisetest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    UserDTO u1 = new UserDTO(
            1L, "user1@example.com", "John", "Doe",
            LocalDate.of(1990, 5, 15),
            new AddressDTO(
                    "City 1",
                    "Country 1",
                    "1234567890",
                    "State 1",
                    "Street 1",
                    "123"
            ),
            "123456789012");

    UserDTO u2 = new UserDTO(
            2L, "user2@example.com", "Jane", "Smith",
            LocalDate.of(1985, 10, 20),
            new AddressDTO(
                    "City 2",
                    "Country 2",
                    "0987654321",
                    "State 2",
                    "Street 2",
                    "456"
            ),
            "987654321098");

    UserDTO createDTO = new UserDTO(
            null, "user1@example.com", "John", "Doe",
            LocalDate.of(1990, 5, 15),
            null, "123456789012");
    UserDTO savedDTO = new UserDTO(
                1L, "user1@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15),
                null, "123456789012");

    @Test
    void should_get_all() throws Exception {
        when(userService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(u1, u2)));

        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void should_get_all_with_birthdate_between() throws Exception {
        var from = LocalDate.of(2022, 1, 1);
        var to = LocalDate.of(2022, 1, 31);

        when(userService.getAllByBirthDateBetween(eq(from), eq(to), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(u1, u2)));

        var request = get("/api/v1/users")
                .param("from", from.toString())
                .param("to", to.toString());
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1))
                .getAllByBirthDateBetween(eq(from), eq(to), any(Pageable.class));
    }

    @Test
    void should_get_by_id() throws Exception {
        when(userService.getById(1L)).thenReturn(u1);

        mockMvc.perform(get("/api/v1/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value(LocalDate.of(1990, 5, 15).toString()))
                .andExpect(jsonPath("$.address.city").value("City 1"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789012"));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    void should_response_not_found_when_get_by_id() throws Exception {
        when(userService.getById(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/users/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON_VALUE));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    void should_create() throws Exception {
        when(userService.create(createDTO)).thenReturn(savedDTO);

        var json = objectMapper.writeValueAsString(createDTO);
        var request = post("/api/v1/users")
                .contentType(APPLICATION_JSON_VALUE)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value(LocalDate.of(1990, 5, 15).toString()))
                .andExpect(jsonPath("$.phoneNumber").value("123456789012"));

        verify(userService, times(1)).create(createDTO);
    }

    @Test
    void should_response_conflict_when_create() throws Exception {
        when(userService.create(createDTO)).thenThrow(new ResourceInConflictException("Conflict"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).create(createDTO);
    }

    @Test
    void should_response_bad_request_when_create() throws Exception {
        when(userService.create(createDTO)).thenThrow(new IllegalArgumentException("Bad age"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).create(createDTO);
    }

    @Test
    void should_full_update() throws Exception {
        when(userService.fullUpdate(1L, createDTO)).thenReturn(u1);

        var request = put("/api/v1/users/{id}", 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value(LocalDate.of(1990, 5, 15).toString()))
                .andExpect(jsonPath("$.phoneNumber").value("123456789012"));

        verify(userService, times(1)).fullUpdate(1L, createDTO);
    }

    @Test
    void should_partial_update() throws Exception {
        when(userService.partialUpdate(1L, createDTO)).thenReturn(u1);

        var request = patch("/api/v1/users/{id}", 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value(LocalDate.of(1990, 5, 15).toString()))
                .andExpect(jsonPath("$.phoneNumber").value("123456789012"));

        verify(userService, times(1)).partialUpdate(1L, createDTO);
    }

    @Test
    void should_delete_by_id() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(1L);
    }
}