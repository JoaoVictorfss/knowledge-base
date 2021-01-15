package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ArticleService {
    /**
     * Returns a paginated list of articles
     *
     * @param pageRequest
     * @return Page<Article>
     */
    Page<Article> findAll(PageRequest pageRequest);

    /**
     * Returns a paginated list of articles by category
     *
     * @param pageRequest
     * @param id
     * @return Page<Article>
     */
    Page<Article> findAllByCategoryId(Long id, PageRequest pageRequest);

    /**
     * creates a new article in the database
     *
     * @param article
     * @return Article
     */
    Article persist(Article article);

    /**
     *find article by id
     *
     * @param id
     * @return Optional<Article>
     */
    Optional<Article> findById(Long id);

    /**
     * remove a article in the database
     *
     * @param id
     */
    void delete(Long id);
}
