package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface SectionService {
    Page<Section> findAll(PageRequest pageRequest);

    Page<Section> findAllByCategoryId(Long id, PageRequest pageRequest);

    Section persist(Section section);

    Optional<Section> findById(Long id);

    void delete(Long id);
}
