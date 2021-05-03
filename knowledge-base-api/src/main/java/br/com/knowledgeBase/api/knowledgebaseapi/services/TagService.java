package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Tag;

import java.util.Optional;

public interface TagService {
    Tag persist(Tag tag);

    Optional<Tag> findById(Long id);

    void delete(Long id);
}
