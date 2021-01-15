package br.com.knowledgeBase.api.knowledgebaseapi.dtos;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.LikedType;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.StatusType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ArticleDto {
    private Long id;

    @NotBlank(message = "The title cannot be empty.")
    private String title;

    @NotBlank(message = "The subtitle cannot be empty.")
    private String subtitle;

    @NotBlank(message = "The name of the author who updated the tag cannot be empty.")
    private String content;

    @NotBlank(message = "The slug cannot be empty.")
    private String slug;

    @Positive(message = "The views cannot be negative.")
    @NotNull(message = "The views cannot be null.")
    private Long views;

    @NotBlank(message = "The name of the author who created the tag cannot be empty.")
    private String created_by;

    @NotBlank(message = "The name of the author who updated the tag cannot be empty.")
    private String updated_by;

    @NotNull
    private List<Long> categoriesId;

    //@NotNull
    //private List<Long> sectionsId;

    private Optional<LikedType> liked;

    private StatusType status = StatusType.DRAFT;

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

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
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

    public Optional<LikedType> getLiked() {
        return liked;
    }

    public void setLiked(Optional<LikedType> liked) {
        this.liked = liked;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
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

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", content='" + content + '\'' +
                ", slug='" + slug + '\'' +
                ", views=" + views +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", liked=" + liked +
                ", status=" + status +
                ", updated_at=" + updated_at +
                ", created_at=" + created_at +
                '}';
    }
}
