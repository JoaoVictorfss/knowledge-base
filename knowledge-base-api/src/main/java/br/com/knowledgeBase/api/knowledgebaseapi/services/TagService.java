package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;

import java.util.Optional;

public interface TagService {
    /**
     * creates a new tag in the database
     *
     * @param tag
     * @return Tag
     */
    Tag persist(Tag tag);

    /**
     *find tag by id
     *
     * @param id
     * @return Optional<Tag>
     */
    Optional<Tag> findById(Long id);

    /**
     * remove a tag in the database
     *
     * @param id
     */
    void delete(Long id);
}
