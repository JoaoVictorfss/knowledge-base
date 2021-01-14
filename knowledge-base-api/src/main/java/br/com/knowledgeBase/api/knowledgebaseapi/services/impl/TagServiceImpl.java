package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.TagRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag persist(Tag tag) {
        LOG.info("Persisting tag: {}", tag);

        return this.tagRepository.save(tag);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        LOG.info("searching tag ID {}", id);

        return this.tagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.info("Removing tag ID {}", id);

        this.tagRepository.deleteById(id);
    }
}
