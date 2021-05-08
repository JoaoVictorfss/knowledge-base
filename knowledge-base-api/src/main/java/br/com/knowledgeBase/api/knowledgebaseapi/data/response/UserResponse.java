package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class UserResponse {
    private Long id;

    private String name;

    private String email;
}
