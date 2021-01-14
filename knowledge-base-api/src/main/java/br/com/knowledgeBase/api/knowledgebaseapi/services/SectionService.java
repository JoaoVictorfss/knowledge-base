package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;

import java.util.Optional;

public interface SectionService {
    /**
     * creates a new section in the database
     *
     * @param tag
     * @return Tag
     */
    Section persist(Section tag);

    /**
     *find section by id
     * @param id
     * @return Optional<Section>
     */
    Optional<Section> findById(Long id);

    /**
     * remove a section in the database
     *
     * @param id
     */
    void delete(Long id);
}
