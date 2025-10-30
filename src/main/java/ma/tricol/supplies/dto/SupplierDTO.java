package ma.tricol.supplies.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String company;

    private String address;

    private String contact;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String city;

    private String ice;
}
