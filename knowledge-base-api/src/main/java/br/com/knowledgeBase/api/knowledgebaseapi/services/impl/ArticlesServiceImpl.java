package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Article;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.ArticleRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticlesServiceImpl implements ArticleService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> findAll(PageRequest pageRequest) {
        LOG.info("searching articles");

        return this.articleRepository.findAllPublished(pageRequest);
    }

    @Override
    public Page<Article> findAllByCategoryId(Long id, PageRequest pageRequest) {
        LOG.info("searching articles by category id {}", id);

        return this.articleRepository.findAllByCategoryId(id, pageRequest);
    }

    @Override
    public Article persist(Article article) {
        LOG.info("Persisting article: {}", article);

        return this.articleRepository.save(article);
    }

    @Override
    public Optional<Article> findById(Long id) {
        LOG.info("searching article ID {}", id);

        return this.articleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.info("Removing article ID {}", id);

        this.articleRepository.deleteById(id);
    }
}
