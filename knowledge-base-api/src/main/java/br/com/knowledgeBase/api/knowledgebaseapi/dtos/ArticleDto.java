package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import br.com.knowledgeBase.api.knowledgebaseapi.enums.LikedType;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.StatusType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.Optional;

public class ArticleDto {
    private Long id;

    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The subtitle cannot be empty.")
    private String subtitle;

    @NotBlank(message = "The name of the author who updated the tag cannot be empty.")
    private String content;

    @NotBlank(message = "The slug cannot be empty.")
    private String slug;

    @Positive(message = "The views cannot be negative.")
    @NotNull(message = "The views cannot be null.")
    private Long views;

    @NotBlank(message = "The name of the author who created the tag cannot be empty.")
    private String created_by;

    @NotBlank(message = "The name of the author who updated the tag cannot be empty.")
    private String updated_by;

    private Optional<LikedType> liked;

    private StatusType status = StatusType.DRAFT;

    private Date updated_at;

    private Date created_at;
}
