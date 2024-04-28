package clear.solution.practisetest.service;

import clear.solution.practisetest.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {
    Page<UserDTO> getAll(Pageable pageable);
    Page<UserDTO> getAllByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    UserDTO getById(Long id);
    UserDTO create(UserDTO dto);
    UserDTO fullUpdate(Long id, UserDTO dto);
    UserDTO partialUpdate(Long id, UserDTO dto);
    void deleteById(Long id);
}
