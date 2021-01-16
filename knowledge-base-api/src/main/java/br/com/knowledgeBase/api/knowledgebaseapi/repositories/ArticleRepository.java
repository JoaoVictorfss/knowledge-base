package br.com.knowledgeBase.api.knowledgebaseapi.repositories;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    @Query("SELECT a FROM Article a "
            + "JOIN a.articleCategories c "
            + "WHERE c.id = :categoryId")
    Page<Article> findAllByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT a FROM Article a "
            + "WHERE a.status = 'PUBLISH'")
    Page<Article>findAllPublished(Pageable pageable);
}
