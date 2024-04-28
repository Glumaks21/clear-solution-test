package clear.solution.practisetest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Embeddable
public class Address {

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(length = 10)
    private String postCode;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String streetName;

    @Column(length = 50)
    private String streetNumber;

}
