package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class CategoryResponse {
    private String title;

    private String createdBy;

    private String updatedBy;

    private String subtitle;

    private String slug;

    private int articlesQtt;

    private int sectionsQtt;

    private Long id;

    private Date updatedAt;

    private Date createdAt;
}
