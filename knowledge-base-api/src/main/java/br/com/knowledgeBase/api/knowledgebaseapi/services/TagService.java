package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;

import java.util.Optional;

public interface TagService {
    /**
     * Creates a new tag in the database
     *
     * @param tag
     * @return Tag
     */
    Tag persist(Tag tag);

    /**
     * Find tag by id
     *
     * @param id
     * @return Optional<Tag>
     */
    Optional<Tag> findById(Long id);

    /**
     * Remove a tag in the database
     *
     * @param id
     */
    void delete(Long id);
}
