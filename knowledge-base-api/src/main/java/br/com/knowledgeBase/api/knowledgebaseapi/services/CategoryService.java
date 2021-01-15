package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
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
     * creates a new category in the database
     *
     * @param category
     * @return Tag
     */
    Category persist(Category category);

    /**
     *find category by id
     *
     * @param id
     * @return Optional<Category>
     */
    Optional<Category> findById(Long id);

    /**
     * remove a category in the database
     *
     * @param id
     */
    void delete(Long id);
}
