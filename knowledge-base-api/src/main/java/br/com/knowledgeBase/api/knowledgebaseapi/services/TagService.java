package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;

public interface TagService {
    /**
     * creates a new tag in the database
     * @param tag
     * @return Tag
     */
    Tag persist(Tag tag);

    /**
     * remove a tag in the database
     * @param id
     */
    void delete(Long id);
}
