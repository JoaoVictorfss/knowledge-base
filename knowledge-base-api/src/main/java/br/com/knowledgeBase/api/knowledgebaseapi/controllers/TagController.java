package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.GeneralConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.TagDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Tag;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.TagResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.services.TagService;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.BindResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(TAG_PATH)
@CrossOrigin(origins = ANYWHERE)
@Cacheable()
public class TagController {
    private static final Logger LOG = LoggerFactory.getLogger(TagController.class);

    private final TagService _tagService;

    public TagController(TagService tagService) {
        this._tagService = tagService;
    }

    @PostMapping(CREATE)
    @PreAuthorize(ONLY_ADMIN)
    @CachePut(TAG_CACHE)
    public ResponseEntity<Response<TagResponse>> store(@Valid @RequestBody TagDto tagDto,
                                                       BindingResult result) throws ParseException {
        LOG.info("Adding tag: {}", tagDto.toString());
        Response<TagResponse> response = new Response<>();

        this.tagValidation(tagDto, result);
        if(result.hasErrors()) {
            String errorLogMessage =  "Error validating tag\n: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return this.badRequest(errors, response, errorLogMessage);
        }

        Tag tag = this.convertDtoToTag(tagDto);
        this._tagService.persist(tag);

        response.setData(this.convertTagToTagResponse(tag));
        return ResponseEntity.status(CREATED_STATUS).body(response);
    }

    @PutMapping(value = UPDATE)
    @PreAuthorize(ONLY_ADMIN)
    @CacheEvict(value = TAG_CACHE, allEntries = true)
    public ResponseEntity<Response<TagResponse>> update(@PathVariable(ID_PARAM) Long id,
                                                        @Valid @RequestBody TagDto tagDto,  BindingResult result) throws NoSuchAlgorithmException {

        LOG.info("Updating tag id {}, {}", id, tagDto.toString());
        Response<TagResponse> response = new Response<>();

        Optional<Tag> tagExists = this._tagService.findById(id);
        boolean isTagNotPresent = !tagExists.isPresent();
        if(isTagNotPresent) {
            BindResultUtils.bindErrorMessage(result, "tag", "Nonexistent tag.");
        }

        tagValidation(tagDto, result);
        if(result.hasErrors()) {
            String errorLogMessage =  "Error validating tag\n: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return this.badRequest(errors, response, errorLogMessage);
        }

        Tag tagExistsOpt = tagExists.get();
        Tag updatedTag = updateTag(tagExistsOpt, tagDto);
        response.setData(this.convertTagToTagResponse(updatedTag));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
    @PreAuthorize(ONLY_ADMIN)
    @CacheEvict(value = TAG_CACHE, allEntries = true)
    public ResponseEntity<Response<TagResponse>> delete(@PathVariable(ID_PARAM) Long id) {

        LOG.info("Deleting tag: {}", id);
        Response<TagResponse> response = new Response<>();

        boolean isTagNotPresent = !this._tagService.findById(id).isPresent();
        if (isTagNotPresent) {
            String errorLogMessage = "Error removing tag ID: {} Nonexistent tag."+  id;
            List<String> errors = Collections.singletonList("Error removing tag. Nonexistent tag!");
            return badRequest(errors, response, errorLogMessage);
        }else {
            this._tagService.delete(id);
            return ResponseEntity.ok(new Response<>());
        }
    }

    private ResponseEntity<Response<TagResponse>> badRequest (List<String> errors, Response<TagResponse> response, String errorLog) {
        LOG.error(errorLog);
        response.getErrors().addAll(errors);
        return ResponseEntity.badRequest().body(response);
    }

    private Tag updateTag(Tag tagToBeUpdated, TagDto updatedTag) {
        tagToBeUpdated.setTitle(updatedTag.getTitle());
        tagToBeUpdated.setSlug(updatedTag.getSlug());
        tagToBeUpdated.setUpdated_by(updatedTag.getUpdatedBy());

        return this._tagService.persist(tagToBeUpdated);
    }

    private TagResponse convertTagToTagResponse(Tag tag) {
        TagResponse newTagResponse = TagResponse.builder()
                .id(tag.getId())
                .title(tag.getTitle())
                .slug(tag.getSlug())
                .createdBy(tag.getCreated_by())
                .updatedBy(tag.getCreated_by())
                .createdAt(tag.getCreated_at())
                .updatedAt(tag.getUpdated_at())
                .build();

        return newTagResponse;
    }

    private Tag convertDtoToTag(TagDto tagDto) {
        Tag newTag = new Tag();
        newTag.setTitle(tagDto.getTitle());
        newTag.setCreated_by(tagDto.getCreatedBy());
        newTag.setUpdated_by(tagDto.getCreatedBy());
        newTag.setSlug(tagDto.getSlug());

        return newTag;
    }

    private void tagValidation(TagDto tagDto, BindingResult result) {
        if (!result.hasErrors()) {
            String invalidTitleRegex = "[1-9A-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ' ']";
            Pattern notAllowed = Pattern.compile(invalidTitleRegex);
            Matcher invalidTitleRegexMatcher = notAllowed.matcher(tagDto.getTitle());
            boolean isTitleNotValid = invalidTitleRegexMatcher.find();

            if (isTitleNotValid) {
                BindResultUtils.bindErrorMessage(result, "tag", "Invalid title.");
            }
        }
    }
}
