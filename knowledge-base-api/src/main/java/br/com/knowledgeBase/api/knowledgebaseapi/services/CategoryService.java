package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.Data.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CategoryService {
    /**
     * Returns a paginated list of categories
     *
     * @param pageRequest
     * @return Page<Category>
     */
    Page<Category> findAll(PageRequest pageRequest);

    /**
     * Creates a new category in the database
     *
     * @param category
     * @return Tag
     */
    Category persist(Category category);

    /**
     * Find category by id
     *
     * @param id
     * @return Optional<Category>
     */
    Optional<Category> findById(Long id);

    /**
     * Remove a category in the database
     *
     * @param id
     */
    void delete(Long id);
}
