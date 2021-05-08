package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class TagResponse {
    private Long id;

    private String title;

    private String slug;

    private String createdBy;

    private String updatedBy;

    private Date updatedAt;

    private Date createdAt;
}
