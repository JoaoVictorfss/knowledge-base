package br.com.knowledgeBase.api.knowledgebaseapi.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class JwtAuthenticationDto {

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    public JwtAuthenticationDto() {
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
        return "JwtAuthenticationRequestDto [email=" + email + ", password=" + password + "]";
    }

}

