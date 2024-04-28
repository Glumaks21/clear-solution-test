package clear.solution.practisetest.dto;

import clear.solution.practisetest.dto.validation.Groups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDTO(

        @Size(groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class}, min = 2, max = 50,
                message = "City must be between 2 and 50 symbols")
        @NotBlank( groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "City must not be blank")
        String city,

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "Country must be between 2 and 50 symbols")
        @NotBlank( groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "Country must not be blank")
        String country,

        @Size(min = 10, max = 10, groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "Post code must 10 symbols")
        String postCode,

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "State must be between 2 and 50 symbols")
        String state,

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "Street name be between 2 and 50 symbols")
        String streetName,

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.PartialUpdate.class, Groups.FullUpdate.class},
                message = "Street number must be between 2 and 50 symbols")
        String streetNumber
) {
    public AddressDTO(String city,
                      String country,
                      String postCode,
                      String state,
                      String streetName,
                      String streetNumber) {
        this.city = city != null? city.trim(): null;
        this.country = country != null? country.trim(): null;
        this.postCode = postCode != null? postCode.trim(): null;
        this.state = state != null? state.trim(): null;
        this.streetName = streetName != null? streetName.trim(): null;
        this.streetNumber = streetNumber != null? streetNumber.trim(): null;
    }
}
