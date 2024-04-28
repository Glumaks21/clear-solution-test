package clear.solution.practisetest;

import clear.solution.practisetest.config.UserValidationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(UserValidationProperties.class)
@SpringBootApplication
public class PractiseTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PractiseTestApplication.class, args);
    }

}
