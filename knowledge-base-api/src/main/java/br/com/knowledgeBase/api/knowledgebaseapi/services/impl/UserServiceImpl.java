package br.com.knowledgeBase.api.knowledgebaseapi.services.impl;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.repositories.UserRepository;
import br.com.knowledgeBase.api.knowledgebaseapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        LOG.info("searching user email {}", email);

        return Optional.ofNullable(this.userRepository.findByEmail(email));
    }

    @Override
    public User persist(User user) {
        LOG.info("Persisting user {}", user);

        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        LOG.info("searching user ID {}", id);

        return this.userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.info("Removing user ID {}", id);

        this.userRepository.deleteById(id);
    }
}
