package clear.solution.practisetest.service.impl;

import clear.solution.practisetest.config.UserValidationProperties;
import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.dto.mapper.UserMapper;
import clear.solution.practisetest.exception.ResourceInConflictException;
import clear.solution.practisetest.exception.ResourceNotFoundException;
import clear.solution.practisetest.repository.UserRepository;
import clear.solution.practisetest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper mapper;
    private final UserValidationProperties userProps;


    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAll(Pageable pageable) {
        return userRepo.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable) {
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("From date must be before to date");
        }
        return userRepo.findAllByBirthDateBetween(from, to, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        return userRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %d not found".formatted(id)
                ));
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new ResourceInConflictException(
                    "User with email %s already exists".formatted(dto.email())
            );
        }

        var years = ChronoUnit.YEARS.between(dto.birthDate(), LocalDate.now());
        if (years < userProps.age()) {
            throw new IllegalArgumentException("User must be over 18 years of age");
        }

        var user = mapper.toEntity(dto);
        log.info("Saving user to db: {}", user);
        return mapper.toDTO(userRepo.save(user));
    }

    @Override
    @Transactional
    public UserDTO fullUpdate(Long id, UserDTO dto) {
        var user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %d not found".formatted(id))
                );
        if (userRepo.existsByEmail(dto.email())) {
            throw new ResourceInConflictException(
                    "User with email %s already registered".formatted(dto.email()));
        }

        mapper.fullUpdate(dto, user);
        log.info("Updating user to db: {}", user);
        return mapper.toDTO(userRepo.save(user));
    }

    @Override
    @Transactional
    public UserDTO partialUpdate(Long id, UserDTO dto) {
        var user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %d not found".formatted(id))
                );
        if (userRepo.existsByEmail(dto.email())) {
            throw new ResourceInConflictException(
                    "User with email %s already registered".formatted(dto.email()));
        }

        mapper.partialUpdate(dto, user);
        log.info("Updating user to db: {}", user);
        return mapper.toDTO(userRepo.save(user));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting user with id {}", id);
        userRepo.deleteById(id);
    }
}