package br.com.knowledgeBase.api.knowledgebaseapi.security.dto;

public class AuthenticationDto {
    private String userName;
    private String token;

    public AuthenticationDto(){}

    public AuthenticationDto(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
