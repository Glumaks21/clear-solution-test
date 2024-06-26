package clear.solution.practisetest.repository;

import clear.solution.practisetest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Page<User> findAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}
