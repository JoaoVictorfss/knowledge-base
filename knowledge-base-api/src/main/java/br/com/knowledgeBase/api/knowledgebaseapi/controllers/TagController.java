package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.TagDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Tag;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/knowledgeBase-api/tags")
@CrossOrigin(origins = "*")
public class TagController {
    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    /**
     * Add a new tag
     *
     * @param tagDto
     * @param result
     * @return ResponseEntity<Response<TagDto>>
     * @throws ParseException
     */
    @PostMapping("/create")
    public ResponseEntity<Response<TagDto>> store(@Valid @RequestBody TagDto tagDto,
                                                  BindingResult result) throws ParseException {
        log.info("Adding tag: {}", tagDto.toString());
        Response<TagDto> response = new Response<TagDto>();


        try {
            this.tagValidation(tagDto, result);
            Tag tag = this.convertDtoToTag(tagDto);

            this.tagService.persist(tag);
            response.setData(this.convertTagToTagDto(tag));
            return ResponseEntity.ok(response);
        }catch (ValidationException err){
            log.error("Error validating tag: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

    }

    /**
     * Update tag data
     *
     * @param tagDto
     * @param result
     * @return ResponseEntity<Response<TagDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Response<TagDto>> update(@PathVariable("id") Long id,
                                                        @Valid @RequestBody TagDto tagDto,  BindingResult result) throws NoSuchAlgorithmException {
        log.info("Updating tag: {}", tagDto.toString());
        Response<TagDto> response = new Response<TagDto>();

        try{
            Optional<Tag>tagExists = this.tagService.findById(id);

            if(!tagExists.isPresent()){
                result.addError(new ObjectError("tag", "Nonexistent tag."));
            }

            tagValidation(tagDto, result);

            //Todo informar a quantidade de artigos afetados

            tagExists.get().setTitle(tagDto.getTitle());
            tagExists.get().setSlug(tagDto.getSlug());
            tagExists.get().setCreated_by(tagDto.getCreated_by());
            tagExists.get().setUpdated_by(tagDto.getUpdated_by());

            this.tagService.persist(tagExists.get());
            response.setData(this.convertTagToTagDto(tagExists.get()));
            return ResponseEntity.ok(response);
        }catch (ValidationException err){
            log.error("Error validating tag\n: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete tag by id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        log.info("Deleting tag: {}", id);
        Response<String> response = new Response<String>();

        Optional<Tag> tag = this.tagService.findById(id);

        if (!tag.isPresent()) {
            log.info("Error removing tag ID: {} Nonexistent tag.", id);
            response.getErrors().add("Error removing tag. Nonexistent tag!");
            return ResponseEntity.badRequest().body(response);
        }else{
            this.tagService.delete(id);
            return ResponseEntity.ok(new Response<String>());
        }
    }

    /**
     *Convert Tag to TagDto
     *
     * @param tag
     * @return TagDto
     */
    private TagDto convertTagToTagDto(Tag tag){
        TagDto tagDto = new TagDto();

        tagDto.setId(tag.getId());
        tagDto.setTitle(tag.getTitle());
        tagDto.setSlug(tag.getSlug());
        //tagDto.setCreated_at(Optional.of(tag.getCreated_at()));
        //tagDto.setUpdated_at(Optional.of(tag.getUpdated_at()));
        tagDto.setCreated_by(tag.getCreated_by());
        tagDto.setUpdated_by(tag.getUpdated_by());

        return  tagDto;
    }

    /**
     * Convert DTO data to Tag
     *
     * @param tagDto
     * @return Tag
     */
    private Tag convertDtoToTag(TagDto tagDto){
        Tag tag = new Tag();
        tag.setTitle(tagDto.getTitle());
        tag.setCreated_by(tagDto.getCreated_by());
        tag.setUpdated_by(tagDto.getCreated_by());
        tag.setSlug(tagDto.getSlug());

        return tag;
    }

    /**
     * Validate a tag. Check if the name has unique words and no special characters and has no errors
     *
     * @param tagDto
     * @param result
     */
    private void tagValidation(TagDto tagDto, BindingResult result){

        if(result.hasErrors()){
            throw new ValidationException();
        }

        Pattern notAllowed = Pattern.compile("[1-9A-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ' ']");
        Matcher m = notAllowed.matcher(tagDto.getTitle());

        if(m.find()){
            result.addError(new ObjectError("tag", "Invalid title."));
            throw new ValidationException();
        };

    }
}
