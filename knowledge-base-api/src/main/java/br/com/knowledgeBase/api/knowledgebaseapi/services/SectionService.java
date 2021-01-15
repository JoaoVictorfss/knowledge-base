package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface SectionService {
    /**
     * Returns a paginated list of sections
     *
     * @param pageRequest
     * @return Page<Section>
     */
    Page<Section> findAll(PageRequest pageRequest);

    /**
     * Returns a paginated list of sections by category
     *
     * @param pageRequest
     * @param id
     * @return Page<Section>
     */
    Page<Section> findAllByCategoryId(Long id, PageRequest pageRequest);

    /**
     * creates a new section in the database
     *
     * @param tag
     * @return Tag
     */
    Section persist(Section tag);

    /**
     *find section by id
     *
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
