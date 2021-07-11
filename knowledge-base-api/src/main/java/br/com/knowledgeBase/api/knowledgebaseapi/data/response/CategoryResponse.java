package br.com.knowledgeBase.api.knowledgebaseapi.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class CategoryResponse {
    private Long id;

    private Date updatedAt;

    private Date createdAt;

    private String title;

    private String createdBy;

    private String updatedBy;

    private String subtitle;

    private String slug;

    private int articlesQtt;

    private int sectionsQtt;

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
