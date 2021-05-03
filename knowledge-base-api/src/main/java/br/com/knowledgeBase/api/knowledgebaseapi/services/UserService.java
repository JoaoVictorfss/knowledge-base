package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    User persist(User user);
}
