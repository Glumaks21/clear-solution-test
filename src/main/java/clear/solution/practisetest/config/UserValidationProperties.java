package clear.solution.practisetest.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "user")
public record UserValidationProperties(
        @Positive(message = "Age must be positive")
        @NotNull(message = "Age is required")
        Integer age
) {
}
