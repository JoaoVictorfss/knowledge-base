package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Builder
@Getter
public class SectionResponse {
    private Long id;

    private String title;

    private String subtitle;

    private String slug;

    private String createdBy;

    private String updatedBy;

    private int articlesQtt = 0;

    private Date updatedAt;

    private Date createdAt;
}
