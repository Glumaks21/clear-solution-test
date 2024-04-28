package clear.solution.practisetest.config;

import clear.solution.practisetest.model.Address;
import clear.solution.practisetest.model.User;
import clear.solution.practisetest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class InitConfig {

    private final UserRepository userRepo;

    @Bean
    CommandLineRunner init() {
        return args -> {
            var me = User.builder()
                    .email("glumaks21@gmail.com")
                    .firstName("Maks")
                    .lastName("Hlushchenko")
                    .birthDate(LocalDate.of(2004, 7, 21))
                    .phoneNumber("380967885962")
                    .address(Address.builder()
                            .country("Ukraine")
                            .state("Kyivska")
                            .city("Brovary")
                            .build())
                    .build();
            userRepo.save(me);
        };
    }

}
