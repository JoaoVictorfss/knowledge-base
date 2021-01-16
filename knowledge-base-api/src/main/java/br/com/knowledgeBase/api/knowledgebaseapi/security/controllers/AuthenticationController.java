package br.com.knowledgeBase.api.knowledgebaseapi.security.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.knowledgeBase.api.knowledgebaseapi.security.dto.AuthenticationDto;
import br.com.knowledgeBase.api.knowledgebaseapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.security.dto.JwtAuthenticationDto;
import br.com.knowledgeBase.api.knowledgebaseapi.security.dto.TokenDto;
import br.com.knowledgeBase.api.knowledgebaseapi.security.utils.JwtTokenUtil;

@RestController
@RequestMapping("/knowledge-base/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    /**
     * Generate and returns a new JWT token.
     *
     * @param authenticationDto
     * @param result
     * @return ResponseEntity<Response<AuthenticationDto>>
     * @throws AuthenticationException
     */
    @PostMapping
    public ResponseEntity<Response<AuthenticationDto>> generateTokenJwt(
            @Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result)
            throws AuthenticationException {
        Response<AuthenticationDto> response = new Response<AuthenticationDto>();

        if (result.hasErrors()) {
            LOG.error("Error validating user\n: {}", result.getAllErrors());

            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        try{
            LOG.info("Generating token for email {}.", authenticationDto.getEmail());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationDto.getEmail(), authenticationDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationDto.getEmail());
            String token = jwtTokenUtil.getToken(userDetails);
            String userName = this.userService.findByEmail(authenticationDto.getEmail()).get().getName();
            response.setData(new AuthenticationDto(userName, token));

            return ResponseEntity.ok(response);
        }catch (BadCredentialsException err){
            response.getErrors().add("Error. User or password does not exist");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Generate a new token with a new expiration date
     *
     * @param request
     * @return ResponseEntity<Response<TokenDto>>
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> generateRefreshTokenJwt(HttpServletRequest request) {
        LOG.info("Generating refresh token JWT.");
        Response<TokenDto> response = new Response<TokenDto>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if (!token.isPresent()) {
            response.getErrors().add("Token not informed\n.");
        } else if (!jwtTokenUtil.isTokenValid(token.get())) {
            response.getErrors().add("Invalid token.");
        }

        if (!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = jwtTokenUtil.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));
        return ResponseEntity.ok(response);
    }

}

