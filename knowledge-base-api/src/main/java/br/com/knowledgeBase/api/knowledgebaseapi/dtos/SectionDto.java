package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Optional;

public class SectionDto {

    private Long id;

    @NotEmpty(message = "The title cannot be empty.")
    private String title;

    @NotEmpty(message = "The subtitle cannot be empty.")
    private String subtitle;

    @NotEmpty(message = "The slug cannot be empty.")
    private String slug;

    @NotEmpty(message = "The name of the author who created the tag cannot be empty.")
    private String created_by;

    @NotEmpty(message = "The name of the author who updated the tag cannot be empty.")
    private String updated_by;

    private Optional<LocalDate> updated_at;

    private Optional<LocalDate> created_at;

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

    public Optional<LocalDate> getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Optional<LocalDate> updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public Optional<LocalDate> getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Optional<LocalDate> created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "SectionDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", updated_at=" + updated_at +
                ", created_at=" + created_at +
                '}';
    }
}
