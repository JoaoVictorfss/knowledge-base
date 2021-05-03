package br.com.knowledgeBase.api.knowledgebaseapi.Data.entities;

import br.com.knowledgeBase.api.knowledgebaseapi.Data.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "articles")
public class Article implements Serializable {
    private static final long serialVersionUID = -5754246207015712518L;

    public Article(){ }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle", nullable = false)
    private String subtitle;

    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="enum('DRAFT', 'PUBLISH', 'CANCEL')", nullable = false)
    private StatusEnum status;

    @Column(name = "average_liked", nullable = false)
    private Long averageLiked;

    @Column(name = "great_liked", nullable = false)
    private Long greatLiked;

    @Column(name = "poor_liked", nullable = false)
    private Long poorLiked;

    @Column(name = "viewers", nullable = false)
    private Long viewers;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "created_by", nullable = false)
    private String created_by;

    @Column(name = "updated_by", nullable = false)
    private String updated_by;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Date created_at;

    @Column(name = "updated_at", nullable = false)
    private Date updated_at;

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name = "article_categories",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> articleCategories = new ArrayList<Category>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name = "article_sections",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<Section> sections = new ArrayList<Section>();

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

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getViewers() {
        return viewers;
    }

    public void setViewers(Long viewers) {
        this.viewers = viewers;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<Category> getCategories() {
        return articleCategories;
    }

    public void setCategories(List<Category> categories) {
        this.articleCategories = categories;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
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

    @PreUpdate
    public void preUpdate() {
        updated_at = new Date();
    }

    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        created_at = atual;
        updated_at = atual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(title, article.title)
                && Objects.equals(subtitle, article.subtitle) && status == article.status
                && Objects.equals(averageLiked, article.averageLiked) && Objects.equals(greatLiked, article.greatLiked)
                && Objects.equals(poorLiked, article.poorLiked) && Objects.equals(viewers, article.viewers)
                && Objects.equals(slug, article.slug) && Objects.equals(created_by, article.created_by)
                && Objects.equals(updated_by, article.updated_by) && Objects.equals(content, article.content)
                && Objects.equals(created_at, article.created_at) && Objects.equals(updated_at, article.updated_at)
                && Objects.equals(articleCategories, article.articleCategories) && Objects.equals(sections, article.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subtitle, status, averageLiked, greatLiked, poorLiked, viewers, slug, created_by, updated_by, content, created_at, updated_at, articleCategories, sections);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", status=" + status +
                ", averageLiked=" + averageLiked +
                ", greatLiked=" + greatLiked +
                ", poorLiked=" + poorLiked +
                ", viewers=" + viewers +
                ", slug='" + slug + '\'' +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", sections=" + sections +
                '}';
    }
}

