package br.com.knowledgeBase.api.knowledgebaseapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "sections")
public class Section implements Serializable {
    private static final long serialVersionUID = -5754246207015712518L;

    public Section(){ }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle", nullable = false)
    private String subtitle;

    @Column(name = "slug", nullable = false)
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
    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "sections", cascade = {CascadeType.REMOVE})
    private List<Article> articles = new ArrayList<Article>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
           name = "section_categories",
           joinColumns = @JoinColumn(name = "section_id"),
           inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<Category>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name = "section_tags",
            joinColumns = @JoinColumn(name = "section_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )

    private Set<Tag> tags;

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
        final Date atual = new Date();
        created_at = atual;
        updated_at = atual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id.equals(section.id) && title.equals(section.title) && subtitle.equals(section.subtitle) && slug.equals(section.slug) && created_by.equals(section.created_by) && updated_by.equals(section.updated_by) && created_at.equals(section.created_at) && updated_at.equals(section.updated_at) && categories.equals(section.categories) && tags.equals(section.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subtitle, slug, created_by, updated_by, created_at, updated_at, categories, tags);
    }

    @Override
    public String toString() {
        return "Section{" +
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
