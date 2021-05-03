package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ArticleService {

    /**
     * Returns a paginated list of articles by category
     *
     * @param pageRequest
     * @param id
     * @return Page<Article>
     */
    Page<Article> findAllByCategoryId(Long id, PageRequest pageRequest);

    /**
     * Returns a paginated list of articles by section
     *
     * @param pageRequest
     * @param id
     * @return Page<Article>
     */
    Page<Article> findAllBySectionId(Long id, PageRequest pageRequest);

    /**
     * Returns a paginated list of published articles by category id
     *
     * @param pageRequest
     * @param id
     * @return Page<Article>
     */
    Page<Article> findAllPublishedByCategoryId(Long id, PageRequest pageRequest);

    /**
     * Returns a paginated list of published articles by section id
     *
     * @param pageRequest
     * @param id
     * @return Page<Article>
     */
    Page<Article> findAllPublishedBySectionId(Long id, PageRequest pageRequest);

    /**
     *Return articles by param
     *
     * @param param
     * @return Optional<Article>
     */
    Page<Article>findAllByParam(String param, PageRequest pageRequest);

    /**
     *Find article by id
     *
     * @param id
     * @return Optional<Article>
     */
    Optional<Article> findById(Long id);

    /**
     * Creates a new article in the database
     *
     * @param article
     * @return Article
     */
    Article persist(Article article);

    /**
     * Remove a article in the database
     *
     * @param id
     */
    void delete(Long id);
}
