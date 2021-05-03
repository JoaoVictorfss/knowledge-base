package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.Data.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.SectionRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public Page<Section> findAll(PageRequest pageRequest) {
        LOG.info("searching sections");
        return  this.sectionRepository.findAll(pageRequest);
    }

    @Override
    public Page<Section> findAllByCategoryId(Long id, PageRequest pageRequest) {
        LOG.info("searching sections by category id {}", id);
        return  this.sectionRepository.findAllByCategoryId(id, pageRequest);
    }

    @Override
    public Section persist(Section section) {
        LOG.info("Persisting section: {}", section);
        return this.sectionRepository.save(section);
    }

    @Override
    public Optional<Section> findById(Long id) {
        LOG.info("searching section ID {}", id);
        return this.sectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.info("Removing section ID {}", id);
        this.sectionRepository.deleteById(id);
    }
}
