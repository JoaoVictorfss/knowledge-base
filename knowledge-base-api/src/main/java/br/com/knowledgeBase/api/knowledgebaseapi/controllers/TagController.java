package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.data.contants.PathConstants;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.TagDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Tag;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(PathConstants.TAG_PATH)
@CrossOrigin(origins = "*")
@Cacheable("tags")
public class TagController {
    private static final Logger LOG = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    @PostMapping(PathConstants.CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CachePut("tags")
    public ResponseEntity<Response<TagDto>> store(@Valid @RequestBody TagDto tagDto,
                                                  BindingResult result) throws ParseException {
        LOG.info("Adding tag: {}", tagDto.toString());
        Response<TagDto> response = new Response<TagDto>();

        this.tagValidation(tagDto, result);
        if(result.hasErrors()) {
            LOG.error("Error validating tag\n: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Tag tag = this.convertDtoToTag(tagDto);
        this.tagService.persist(tag);
        response.setData(this.convertTagToTagDto(tag));

        return ResponseEntity.ok(response);

    }

    @PutMapping(value = PathConstants.UPDATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CacheEvict(value = "tafs", allEntries = true)
    public ResponseEntity<Response<TagDto>> update(@PathVariable("id") Long id,
                                                        @Valid @RequestBody TagDto tagDto,  BindingResult result) throws NoSuchAlgorithmException {
        LOG.info("Updating tag id {}, {}", id, tagDto.toString());
        Response<TagDto> response = new Response<TagDto>();

        Optional<Tag>tagExists = this.tagService.findById(id);
        if(!tagExists.isPresent()){
            result.addError(new ObjectError("tag", "Nonexistent tag."));
        }

        tagValidation(tagDto, result);
        if(result.hasErrors()) {
            LOG.error("Error validating tag\n: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Tag tagExistsOpt = tagExists.get();
        tagExistsOpt.setTitle(tagDto.getTitle());
        tagExistsOpt.setSlug(tagDto.getSlug());
        tagExistsOpt.setUpdated_by(tagDto.getUpdatedBy());

        this.tagService.persist(tagExistsOpt);
        response.setData(this.convertTagToTagDto(tagExistsOpt));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = PathConstants.DELETE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CacheEvict(value = "tags", allEntries = true)
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        LOG.info("Deleting tag: {}", id);
        Response<String> response = new Response<String>();

        Optional<Tag> tag = this.tagService.findById(id);
        if (!tag.isPresent()) {
            LOG.info("Error removing tag ID: {} Nonexistent tag.", id);
            response.getErrors().add("Error removing tag. Nonexistent tag!");

            return ResponseEntity.badRequest().body(response);
        }else{
            this.tagService.delete(id);

            return ResponseEntity.ok(new Response<String>());
        }
    }

    private TagDto convertTagToTagDto(Tag tag){
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setTitle(tag.getTitle());
        tagDto.setSlug(tag.getSlug());
        tagDto.setCreatedAt(tag.getCreated_at());
        tagDto.setUpdatedAt(tag.getUpdated_at());
        tagDto.setCreatedBy(tag.getCreated_by());
        tagDto.setUpdatedBy(tag.getUpdated_by());

        return  tagDto;
    }

    private Tag convertDtoToTag(TagDto tagDto){
        Tag tag = new Tag();
        tag.setTitle(tagDto.getTitle());
        tag.setCreated_by(tagDto.getCreatedBy());
        tag.setUpdated_by(tagDto.getCreatedBy());
        tag.setSlug(tagDto.getSlug());

        return tag;
    }

    private void tagValidation(TagDto tagDto, BindingResult result) {
        if (!result.hasErrors()) {
            Pattern notAllowed = Pattern.compile("[1-9A-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ' ']");
            Matcher m = notAllowed.matcher(tagDto.getTitle());

            if (m.find())
                result.addError(new ObjectError("tag", "Invalid title."));
        }
    }
}
