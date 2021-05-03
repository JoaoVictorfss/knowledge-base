package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {
    private Long id;

    @NotBlank(message = "The name cannot be empty.")
    private String name;

    @NotBlank(message = "The email cannot be empty.")
    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "The password cannot be empty.")
    @Length(min = 8, message = "The password must contain at least 8 characters.")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
