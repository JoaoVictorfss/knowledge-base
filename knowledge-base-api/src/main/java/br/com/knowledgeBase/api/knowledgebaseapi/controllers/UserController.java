package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.UserDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.User;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.ProfileEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
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
@RequestMapping("/knowledgeBase-api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private UserService userService;

    /**
     * Add a new admin
     *
     * @param userDto
     * @param result
     * @return ResponseEntity<Response<UserDto>>
     * @throws ParseException
     */
    @PostMapping("/create")
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

    /**
     *Convert User to UserDto
     *
     * @param user
     * @return UserDto
     */
    private UserDto convertUserToUserDto(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return  userDto;
    }

    /**
     * Convert DTO data to User
     *
     * @param userDto
     * @return User
     */
    private User convertDtoToUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(PasswordUtils.generateBCrypt(userDto.getPassword()));
        user.setProfile(ProfileEnum.ROLE_USER);

        return user;
    }

    /**
     * Checks if the email exists
     *
     * @param email
     * @param result
     */
    private void userValidation(String email, BindingResult result){
       if (!result.hasErrors()){
           this.userService.findByEmail(email)
                   .ifPresent(func -> result.addError(new ObjectError("email", "Existing email.")));
       }
    }
}
