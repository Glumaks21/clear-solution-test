package clear.solution.practisetest.controller;

import clear.solution.practisetest.dto.UserDTO;
import clear.solution.practisetest.dto.validation.Groups.Create;
import clear.solution.practisetest.dto.validation.Groups.FullUpdate;
import clear.solution.practisetest.dto.validation.Groups.PartialUpdate;
import clear.solution.practisetest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<UserDTO>> getAll(@RequestParam(required = false) LocalDate from,
                                               @RequestParam(required = false) LocalDate to,
                                               @PageableDefault Pageable pageable) {
        var page = from != null && to != null
                ? userService.getAllByBirthDateBetween(from, to, pageable)
                : userService.getAll(pageable);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    UserDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    UserDTO create(@RequestBody @Validated(Create.class) UserDTO dto) {
        log.info("Received user creation request with dto: {}", dto);
        return userService.create(dto);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    UserDTO fullUpdate(@PathVariable Long id,
                       @RequestBody @Validated(FullUpdate.class) UserDTO dto) {
        log.info("Received full updating request with dto: {}", dto);
        return userService.fullUpdate(id, dto);
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    UserDTO partialUpdate(@PathVariable Long id,
                          @RequestBody @Validated(PartialUpdate.class) UserDTO dto) {
        log.info("Received partial updating request with dto: {}", dto);
        return userService.partialUpdate(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    void deleteById(@PathVariable Long id) {
        log.info("Received deleting request for product with id {}", id);
        userService.deleteById(id);
    }
}
