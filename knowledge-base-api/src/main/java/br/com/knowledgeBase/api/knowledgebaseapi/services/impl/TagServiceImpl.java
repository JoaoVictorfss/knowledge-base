package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.TagRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TagServiceImpl implements TagService {
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag persist(Tag tag) {
        log.info("Persisting tag\n: {}", tag);

        return this.tagRepository.save(tag);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing tag\n ID {}", id);

        this.tagRepository.deleteById(id);
    }
}
