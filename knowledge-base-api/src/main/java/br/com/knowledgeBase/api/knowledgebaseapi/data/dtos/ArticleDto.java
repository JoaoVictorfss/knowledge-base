package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
public class ArticleDto {
    @NotBlank(message = "The title cannot be empty.")
    @Length(min = 5, message = "The title must contain at least 5 characters.")
    private String title;

    @NotBlank(message = "The subtitle cannot be empty.")
    private String subtitle;

    @NotBlank(message = "The content cannot be empty.")
    @Length(min = 15, message = "The content must contain at least 15 characters.")
    private String content;

    @NotBlank(message = "The slug cannot be empty.")
    private String slug;

    @PositiveOrZero(message = "The viewers field cannot be negative.")
    @NotNull(message = "The viewers cannot be null.")
    private Long viewers;

    @NotBlank(message = "The name of the author who created the article cannot be empty.")
    private String createdBy;

    @NotBlank(message = "The name of the author who updated the article cannot be empty.")
    private String updatedBy;

    @NotBlank(message = "The status cannot be empty.")
    private String status;

    @NotNull(message = "The categories id cannot be null")
    private List<Long> categoriesId;

    @PositiveOrZero(message = "The average_like field cannot be negative.")
    @NotNull(message = "The average_like cannot be null.")
    private Long averageLiked;

    @PositiveOrZero(message = "The great_like field cannot be negative.")
    @NotNull(message = "The great_like cannot be null.")
    private Long greatLiked;

    @PositiveOrZero(message = "The poor_liked field cannot be negative.")
    @NotNull(message = "The poor_like cannot be null.")
    private Long poorLiked;

    private Optional<Long> sectionId = Optional.empty();
}
