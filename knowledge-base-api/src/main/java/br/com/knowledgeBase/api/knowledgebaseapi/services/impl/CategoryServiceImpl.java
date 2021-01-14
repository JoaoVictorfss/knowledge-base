package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.CategoryRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(PageRequest pageRequest) {
        LOG.info("Searching categories");

        return this.categoryRepository.findAll(pageRequest);
    }

    @Override
    public Category persist(Category category) {
        LOG.info("Persisting category: {}", category);

        return this.categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        LOG.info("searching category ID {}", id);

        return this.categoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.info("Removing section ID {}", id);

        this.categoryRepository.deleteById(id);
    }
}
