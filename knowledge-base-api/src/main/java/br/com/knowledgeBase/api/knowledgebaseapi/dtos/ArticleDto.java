package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ArticleDto {
    private Long id;

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
    private String created_by;

    @NotBlank(message = "The name of the author who updated the article cannot be empty.")
    private String updated_by;

    @NotBlank(message = "The status cannot be empty.")
    private String status;

    @NotNull(message = "The categories id cannot be null")
    private List<Long> categoriesId;

    private Optional<Long> sectionId = Optional.empty();

    @PositiveOrZero(message = "The average_like field cannot be negative.")
    @NotNull(message = "The average_like cannot be null.")
    private Long average_liked;

    @PositiveOrZero(message = "The great_like field cannot be negative.")
    @NotNull(message = "The great_like cannot be null.")
    private Long great_liked;

    @PositiveOrZero(message = "The poor_liked field cannot be negative.")
    @NotNull(message = "The poor_like cannot be null.")
    private Long poor_liked;

    private Date updated_at;

    private Date created_at;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getViewers() {
        return viewers;
    }

    public void setViewers(Long viewers) {
        this.viewers = viewers;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Long> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Long> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public Optional<Long> getSectionId() {
        return sectionId;
    }

    public void setSectionId(Optional<Long> sectionId) {
        this.sectionId = sectionId;
    }

    public Long getAverage_liked() {
        return average_liked;
    }

    public void setAverage_liked(Long average_liked) {
        this.average_liked = average_liked;
    }

    public Long getGreat_liked() {
        return great_liked;
    }

    public void setGreat_liked(Long great_liked) {
        this.great_liked = great_liked;
    }

    public Long getPoor_liked() {
        return poor_liked;
    }

    public void setPoor_liked(Long poor_liked) {
        this.poor_liked = poor_liked;
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", content='" + content + '\'' +
                ", slug='" + slug + '\'' +
                ", viewers=" + viewers +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", status='" + status + '\'' +
                ", average_liked=" + average_liked +
                ", great_liked=" + great_liked +
                ", poor_liked=" + poor_liked +
                ", updated_at=" + updated_at +
                ", created_at=" + created_at +
                '}';
    }
}
