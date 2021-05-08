package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class SectionDto {
    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The subtitle cannot be empty.")
    private String subtitle;

    @NotBlank(message = "The slug cannot be empty.")
    private String slug;

    @NotBlank(message = "The name of the author who created the category cannot be empty.")
    private String createdBy;

    @NotBlank(message = "The name of the author who updated the category cannot be empty.")
    private String updatedBy;

    private int articlesQtt = 0;
}
