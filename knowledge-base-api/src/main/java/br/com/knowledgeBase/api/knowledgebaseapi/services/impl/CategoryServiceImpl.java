package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.CategoryRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category persist(Category category) {
        log.info("Persisting category: {}", category);

        return this.categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        log.info("searching category ID {}", id);

        return this.categoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing section ID {}", id);

        this.categoryRepository.deleteById(id);
    }
}
