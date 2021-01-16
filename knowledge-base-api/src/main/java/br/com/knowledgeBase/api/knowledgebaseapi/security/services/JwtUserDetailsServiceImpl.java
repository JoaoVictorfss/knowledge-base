package br.com.knowledgeBase.api.knowledgebaseapi.security.services;

import java.util.Optional;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.security.JwtUserFactory;
import br.com.knowledgeBase.api.knowledgebaseapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(username);

        if (user.isPresent()) {
            return JwtUserFactory.create(user.get());
        }

        throw new UsernameNotFoundException("Email not found.");
    }

}
