package br.com.knowledgeBase.api.knowledgebaseapi.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.ProfileEnum;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    /**
     * Convert and generate a JwtUser based on a user's data.
     *
     * @param user
     * @return JwtUser
     */
    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getEmail(), user.getPassword(),
                mapToGrantedAuthorities(user.getProfile()));
    }

    /**
     * Convert the user's profile to the format used by Spring Security.
     *
     * @param profileEnum
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
        return authorities;
    }
}
