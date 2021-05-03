package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(PageRequest pageRequest);

    Category persist(Category category);

    Optional<Category> findById(Long id);

    void delete(Long id);
}
