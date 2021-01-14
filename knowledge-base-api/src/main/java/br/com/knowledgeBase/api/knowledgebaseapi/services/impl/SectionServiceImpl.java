package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.SectionRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SectionServiceImpl implements SectionService {
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public Section persist(Section section) {
        log.info("Persisting section: {}", section);

        return this.sectionRepository.save(section);
    }

    @Override
    public Optional<Section> findById(Long id) {
        log.info("searching section ID {}", id);

        return this.sectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing section ID {}", id);

        this.sectionRepository.deleteById(id);
    }
}
