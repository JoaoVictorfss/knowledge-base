package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class CategoryDto {
    private Long id;

    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The name of the author who created the category cannot be empty.")
    private String created_by;

    @NotBlank(message = "The name of the author who updated the category cannot be empty.")
    private String updated_by;

    private String subtitle;

    private String slug;

    private Date updated_at;

    private Date created_at;

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

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
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
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                ", updated_at=" + updated_at +
                ", created_at=" + created_at +
                ", articlesQtt=" + articlesQtt +
                ", sectionsQtt=" + sectionsQtt +
                '}';
    }
}
