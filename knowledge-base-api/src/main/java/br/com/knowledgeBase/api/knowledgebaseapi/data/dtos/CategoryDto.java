package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class CategoryDto {
    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The name of the author who created the category cannot be empty.")
    private String createdBy;

    @NotBlank(message = "The name of the author who updated the category cannot be empty.")
    private String updatedBy;

    private String subtitle;

    private String slug;

    private int articlesQtt = 0;

    private int sectionsQtt = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getArticlesQtt() {
        return articlesQtt;
    }

    public void setArticlesQtt(int articlesQtt) {
        this.articlesQtt = articlesQtt;
    }

    public int getSectionsQtt() {
        return sectionsQtt;
    }

    public void setSectionsQtt(int sectionsQtt) {
        this.sectionsQtt = sectionsQtt;
    }
}
