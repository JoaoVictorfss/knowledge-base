package br.com.knowledgeBase.api.knowledgebaseapi.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = -5754246207015712518L;

    public Category(){ }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle", nullable = true)
    private String subtitle;

    @Column(name = "slug", nullable = true)
    private String slug;

    @Column(name = "created_by", nullable = false)
    private String created_by;

    @Column(name = "updated_by", nullable = false)
    private String updated_by;

    @Column(name = "created_at", nullable = false)
    private Date created_at;

    @Column(name = "updated_at", nullable = false)
    private Date updated_at;

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "categories", cascade = {CascadeType.REMOVE})
    private List<Section> sections = new ArrayList<Section>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "articleCategories", cascade = {CascadeType.REMOVE})
    private List<Article> articles = new ArrayList<Article>();

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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @PreUpdate
    public void preUpdate() {
        updated_at = new Date();
    }

    @PrePersist
    public void prePersist() {
        final Date currentDate = new Date();
        created_at = currentDate;
        updated_at = currentDate;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
