package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import javax.validation.constraints.NotEmpty;

public class CategoryDto {
    private Long id;

    @NotEmpty(message = "The title cannot be empty.")
    private String title;

    @NotEmpty(message = "The name of the author who created the tag cannot be empty.")
    private String created_by;

    @NotEmpty(message = "The name of the author who updated the tag cannot be empty.")
    private String updated_by;

    private String subtitle;

    private String slug;

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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", subtitle=" + subtitle +
                ", slug=" + slug +
                '}';
    }
}
