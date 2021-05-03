package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ArticleService {
    Page<Article> findAllByCategoryId(Long id, PageRequest pageRequest);

    Page<Article> findAllBySectionId(Long id, PageRequest pageRequest);

    Page<Article> findAllPublishedByCategoryId(Long id, PageRequest pageRequest);

    Page<Article> findAllPublishedBySectionId(Long id, PageRequest pageRequest);

    Page<Article>findAllByParam(String param, PageRequest pageRequest);

    Optional<Article> findById(Long id);

    Article persist(Article article);

    void delete(Long id);
}
