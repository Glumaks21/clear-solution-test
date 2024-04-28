package clear.solution.practisetest.dto;

import clear.solution.practisetest.dto.validation.Groups.Create;
import clear.solution.practisetest.dto.validation.Groups.FullUpdate;
import clear.solution.practisetest.dto.validation.Groups.PartialUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record UserDTO(

        @JsonProperty(access = READ_ONLY)
        Long id,

        @Size(max = 320, groups = {Create.class, PartialUpdate.class, FullUpdate.class},
                message = "Email must be lower than 320 symbols")
        @Pattern( groups = {Create.class, PartialUpdate.class, FullUpdate.class},
                regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email address are not valid")
        @NotBlank(groups = {Create.class, FullUpdate.class}, message = "Email must not be blank")
        String email,

        @Size(min = 2, max = 30, groups = {Create.class, PartialUpdate.class, FullUpdate.class},
                message = "First name must be between 2 and 30 symbols")
        @NotBlank(groups = {Create.class, FullUpdate.class}, message = "Email must not be blank")
        String firstName,

        @Size(min = 2, max = 30, groups = {Create.class, PartialUpdate.class, FullUpdate.class},
                message = "Last name must be between 2 and 30 symbols")
        @NotBlank(groups = {Create.class, FullUpdate.class}, message = "Email must not be blank")
        String lastName,

        @Past(groups = {Create.class, PartialUpdate.class, FullUpdate.class},
                message = "Birth date must be in past")
        @NotNull(groups = {Create.class, FullUpdate.class}, message = "Birth date is required")
        LocalDate birthDate,

        @Valid
        AddressDTO address,

        @Size(groups = {Create.class, PartialUpdate.class, FullUpdate.class}, min = 12, max = 12,
                message = "Phone number must be 12 symbols")
        String phoneNumber
) {
    public UserDTO(Long id,
                   String email,
                   String firstName,
                   String lastName,
                   LocalDate birthDate,
                   AddressDTO address,
                   String phoneNumber) {
        this.id = id;
        this.email = email != null? email.trim(): null;
        this.firstName = firstName != null? firstName.trim(): null;
        this.lastName = lastName != null? lastName.trim(): null;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber != null? phoneNumber.trim(): null;
    }
}
