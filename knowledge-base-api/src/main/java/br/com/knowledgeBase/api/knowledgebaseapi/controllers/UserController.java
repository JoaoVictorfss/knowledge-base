package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.UserDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.data.enums.ProfileEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
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
@RequestMapping(PathConstants.USER_PATH)
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private UserService userService;

    @PostMapping(PathConstants.CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<UserDto>> add(@Valid @RequestBody UserDto userDto,
                                                 BindingResult result) throws ParseException {
        LOG.info("Adding user: {}", userDto.toString());
        Response<UserDto> response = new Response<UserDto>();

        userValidation(userDto.getEmail(), result);
        if (result.hasErrors()) {
            LOG.error("Error validating user: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        User user = this.convertDtoToUser(userDto);

        this.userService.persist(user);
        response.setData(this.convertUserToUserDto(user));

        return ResponseEntity.ok(response);
    }

    private UserDto convertUserToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return  userDto;
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
