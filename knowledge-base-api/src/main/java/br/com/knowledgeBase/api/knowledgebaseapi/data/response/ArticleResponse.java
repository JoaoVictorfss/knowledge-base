package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class ArticleResponse {
    private Long id;

    private Date updatedAt;

    private Date createdAt;

    private String title;

    private String subtitle;

    private String content;

    private String slug;

    private Long viewers;

    private String createdBy;

    private String updatedBy;

    private String status;

    private List<Long> categoriesId;

    private Long averageLiked;

    private Long greatLiked;

    private Long poorLiked;

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

    public List<Long> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Long> categoriesId) {
        this.categoriesId = categoriesId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
