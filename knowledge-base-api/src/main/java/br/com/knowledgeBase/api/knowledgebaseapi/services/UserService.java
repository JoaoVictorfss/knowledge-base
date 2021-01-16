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

    /**
     * creates a new user in the database
     *
     * @param user
     * @return user
     */
    User persist(User user);

    /**
     *find user by id
     *
     * @param id
     * @return Optional<User>
     */
    Optional<User> findById(Long id);

    /**
     * remove a user in the database
     *
     * @param id
     */
    void delete(Long id);
}
