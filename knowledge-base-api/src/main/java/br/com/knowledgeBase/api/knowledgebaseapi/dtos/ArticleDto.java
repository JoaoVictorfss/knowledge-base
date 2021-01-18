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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getAverageLiked() {
        return averageLiked;
    }

    public void setAverageLiked(Long averageLiked) {
        this.averageLiked = averageLiked;
    }

    public Long getGreatLiked() {
        return greatLiked;
    }

    public void setGreatLiked(Long greatLiked) {
        this.greatLiked = greatLiked;
    }

    public Long getPoorLiked() {
        return poorLiked;
    }

    public void setPoorLiked(Long poorLiked) {
        this.poorLiked = poorLiked;
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
                ", created_by='" + createdBy + '\'' +
                ", updated_by='" + updatedBy + '\'' +
                ", status='" + status + '\'' +
                ", average_liked=" + averageLiked +
                ", great_liked=" + greatLiked +
                ", poor_liked=" + poorLiked +
                ", updated_at=" + updatedAt +
                ", created_at=" + createdAt +
                '}';
    }
}
