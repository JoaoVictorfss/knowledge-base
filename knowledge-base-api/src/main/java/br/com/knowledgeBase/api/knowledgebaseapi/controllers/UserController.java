package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.UserDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.data.enums.ProfileEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.UserResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.services.UserService;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.BindResultUtils;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(USER_PATH)
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private UserService _userService;

    @PostMapping(CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<UserResponse>> add(@Valid @RequestBody UserDto userDto,
                                                 BindingResult result) throws ParseException {

        LOG.info("Adding user: {}", userDto.toString());
        Response<UserResponse> response = new Response<>();

        userValidation(userDto.getEmail(), result);
        if (result.hasErrors()) {
          this.badRequest(result, response);
        }

        User user = this.convertDtoToUser(userDto);
        this._userService.persist(user);

        response.setData(this.convertUserToUserResponse(user));
        return ResponseEntity.status(201).body(response);
    }

    private ResponseEntity<Response<UserResponse>> badRequest(BindingResult result, Response<UserResponse> response) {
        LOG.error("Error validating user: {}", result.getAllErrors());
        List<String> errors = BindResultUtils.getAllErrorMessages(result);
        response.getErrors().addAll(errors);
        return ResponseEntity.badRequest().body(response);
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
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setPassword(PasswordUtils.generateBCrypt(userDto.getPassword()));
        newUser.setProfile(ProfileEnum.ROLE_USER);

        return newUser;
    }

    private void userValidation(String email, BindingResult result){
       if (!result.hasErrors()){
           this._userService.findByEmail(email)
                   .ifPresent(func -> BindResultUtils.bindErrorMessage(result, "email", "Existing email."));
       }
    }
}
