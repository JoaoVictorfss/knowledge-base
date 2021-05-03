package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Article;
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
public class ArticleServiceImpl implements ArticleService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> findAllByCategoryId(Long id, PageRequest pageRequest) {
        LOG.info("Searching articles by category id {}", id);
        return this.articleRepository.findAllByCategoryId(id, pageRequest);
    }

    @Override
    public Page<Article> findAllBySectionId(Long id, PageRequest pageRequest) {
        LOG.info("Searching articles by section id {}", id);
        return this.articleRepository.findAllBySectionId(id, pageRequest);
    }

    @Override
    public Page<Article> findAllPublishedByCategoryId(Long id, PageRequest pageRequest) {
        LOG.info("Searching published articles by category id {}", id);
        return this.articleRepository.findAllPublishedByCategoryId(id, pageRequest);
    }

    @Override
    public Page<Article> findAllPublishedBySectionId(Long id, PageRequest pageRequest) {
        LOG.info("Searching published articles by section id {}", id);
        return this.articleRepository.findAllPublishedBySectionId(id, pageRequest);
    }

    @Override
    public Page<Article> findAllByParam(String param, PageRequest pageRequest) {
        LOG.info("Searching published articles by param {}", param);
        return this.articleRepository.findByParam(param, pageRequest);
    }

    @Override
    public Optional<Article> findById(Long id) {
        LOG.info("Searching article ID {}", id);
        return this.articleRepository.findById(id);
    }

    @Override
    public Article persist(Article article) {
        LOG.info("Persisting article: {}", article);
        return this.articleRepository.save(article);
    }


    @Override
    public void delete(Long id) {
        LOG.info("Removing article ID {}", id);
        this.articleRepository.deleteById(id);
    }
}
