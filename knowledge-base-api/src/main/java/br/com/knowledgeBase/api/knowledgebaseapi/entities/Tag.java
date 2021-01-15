package br.com.knowledgeBase.api.knowledgebaseapi.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tags")
public class Tag {
    private static final long serialVersionUID = -5754246207015712518L;

    public Tag(){ }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

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
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", created_by='" + created_by + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
