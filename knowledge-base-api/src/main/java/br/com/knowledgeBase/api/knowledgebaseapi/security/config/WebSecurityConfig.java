package br.com.knowledgeBase.api.knowledgebaseapi.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.knowledgeBase.api.knowledgebaseapi.security.JwtAuthenticationEntryPoint;
import br.com.knowledgeBase.api.knowledgebaseapi.security.filters.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                //public access
                .antMatchers( "/v2/api-docs",
                        "/knowledgeBase-api/sections/list",
                        "/knowledgeBase-api/sections/list/{categoryId}",
                        "/knowledgeBase-api/sections/section/{id}",
                        "/knowledge-base/auth",
                        "/knowledgeBase-api/categories/list",
                        "/knowledgeBase-api/categories/category/{id}",
                        "/knowledgeBase-api/articles/search/{param}",
                        "/knowledgeBase-api/articles/list-by-category/{categoryId}",
                        "/knowledgeBase-api/articles/list-by-section/{categoryId}",
                        "/knowledgeBase-api/articles/article/{id}",
                        "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()

                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();
    }
}
