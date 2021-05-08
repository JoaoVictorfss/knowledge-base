package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank(message = "The name cannot be empty.")
    private String name;

    @NotBlank(message = "The email cannot be empty.")
    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "The password cannot be empty.")
    @Length(min = 8, message = "The password must contain at least 8 characters.")
    private String password;
}
