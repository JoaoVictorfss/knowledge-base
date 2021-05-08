package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class ArticleResponse {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String slug;

    private Long viewers;

    private String createdBy;

    private String updatedBy;

    private String status;

    private List<Long> categoriesId;

    private Long averageLiked;

    private Long greatLiked;

    private Long poorLiked;

    private Date updatedAt;

    private Date createdAt;
}
