package br.com.knowledgeBase.api.knowledgebaseapi.data.dtos;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class CategoryDto {
    private Long id;

    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The name of the author who created the category cannot be empty.")
    private String createdBy;

    @NotBlank(message = "The name of the author who updated the category cannot be empty.")
    private String updatedBy;

    private String subtitle;

    private String slug;

    private Date updatedAt;

    private Date createdAt;

    private int articlesQtt = 0;

    private int sectionsQtt = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created_by='" + createdBy + '\'' +
                ", updated_by='" + updatedBy + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                ", updated_at=" + updatedAt +
                ", created_at=" + createdAt +
                ", articlesQtt=" + articlesQtt +
                ", sectionsQtt=" + sectionsQtt +
                '}';
    }
}
