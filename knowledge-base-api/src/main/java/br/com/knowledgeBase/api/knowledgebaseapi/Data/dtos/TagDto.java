package br.com.knowledgeBase.api.knowledgebaseapi.Data.dtos;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class TagDto {
    private Long id;

    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The slug cannot be empty.")
    private String slug;

    @NotBlank(message = "The name of the author who created the tag cannot be empty.")
    private String createdBy;

    @NotBlank(message = "The name of the author who updated the tag cannot be empty.")
    private String updatedBy;

    private Date updatedAt;

    private Date createdAt;

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

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug=" + slug +
                ", created_by='" + createdBy + '\'' +
                ", updated_by='" + updatedBy + '\'' +
                ", updated_at=" + updatedAt +
                ", created_at=" + createdAt +
                '}';
    }
}
