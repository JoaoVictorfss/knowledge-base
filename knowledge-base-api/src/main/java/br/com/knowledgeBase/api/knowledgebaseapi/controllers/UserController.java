package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.UserDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.data.enums.ProfileEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.UserResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.services.UserService;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping(USER_PATH)
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private UserService userService;

    @PostMapping(CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<UserResponse>> add(@Valid @RequestBody UserDto userDto,
                                                 BindingResult result) throws ParseException {

        LOG.info("Adding user: {}", userDto.toString());
        Response<UserResponse> response = new Response<>();

        userValidation(userDto.getEmail(), result);
        if (result.hasErrors()) {
            LOG.error("Error validating user: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        User user = this.convertDtoToUser(userDto);

        this.userService.persist(user);
        response.setData(this.convertUserToUserResponse(user));

        return ResponseEntity.status(201).body(response);
    }

    private UserResponse convertUserToUserResponse(User user){
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        return userResponse;
    }

    private User convertDtoToUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(PasswordUtils.generateBCrypt(userDto.getPassword()));
        user.setProfile(ProfileEnum.ROLE_USER);

        return user;
    }

    private void userValidation(String email, BindingResult result){
       if (!result.hasErrors()){
           this.userService
                   .findByEmail(email)
                   .ifPresent(func -> result.addError(new ObjectError("email", "Existing email.")));
       }
    }
}
