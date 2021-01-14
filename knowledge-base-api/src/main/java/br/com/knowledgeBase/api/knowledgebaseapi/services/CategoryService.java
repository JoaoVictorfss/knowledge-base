package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;

import java.util.Optional;

public interface CategoryService {
    /**
     * creates a new category in the database
     *
     * @param category
     * @return Tag
     */
    Category persist(Category category);

    /**
     *find category by id
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
