package br.com.knowledgeBase.api.knowledgebaseapi.services;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.User;

import java.util.Optional;

public interface UserService {
    /**
     * Find user by email and returns an user
     *
     * @param email
     * @return Optional(User)
     */
    Optional<User> findByEmail(String email);
}