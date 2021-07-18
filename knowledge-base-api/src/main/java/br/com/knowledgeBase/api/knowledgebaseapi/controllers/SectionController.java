package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.GeneralConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.SectionDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.SectionResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.BindResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(SECTION_PATH)
@CrossOrigin(origins = ANYWHERE)
public class SectionController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    private final SectionService _sectionService;

    private final CategoryService _categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    public SectionController(SectionService sectionService, CategoryService categoryService) {
        this._sectionService = sectionService;
        this._categoryService = categoryService;
    }

    @GetMapping(value = LIST)
    public ResponseEntity<Response<Page<SectionResponse>>> index(
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "DESC") String dir) {

        LOG.info("Searching sections, page: {}", pag);
        Response<Page<SectionResponse>> response = new Response<>();

        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Section> sections = this._sectionService.findAll(pageRequest);
        Page<SectionResponse> sectionsResponse = sections.map(this::convertSectionToSectionResponse);

        response.setData(sectionsResponse);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = FIND_SECTIONS_BY_CATEGORY_ID)
    public ResponseEntity<Response<Page<SectionResponse>>> listSectionsByCategoryId(
            @PathVariable(CATEGORY_ID_PARAM) Long categoryId,
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "DESC") String dir) {

        LOG.info("Searching sections by category {}, page: {}", categoryId, pag);
        Response<Page<SectionResponse>> response = new Response<>();

        boolean isCategoryNotPresent = !this._categoryService.findById(categoryId).isPresent();
        if(isCategoryNotPresent) {
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }
        
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Section>sections = this._sectionService.findAllByCategoryId(categoryId, pageRequest);
        Page<SectionResponse> sectionsResponse = sections.map(this::convertSectionToSectionResponse);

        response.setData(sectionsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = FIND_SECTION_BY_ID)
    public ResponseEntity<Response<SectionResponse>> showById(@PathVariable(ID_PARAM) Long id) {

        LOG.info("Searching section id {}", id);
        Response<SectionResponse> response = new Response<>();

        Optional<Section> sectionExists = this._sectionService.findById(id);
        boolean isSectionNotPresent = !sectionExists.isPresent();
        if(isSectionNotPresent) {
            String errorLogMessage = "Error. Nonexistent section.";
            List<String> errors = Collections.singletonList(errorLogMessage);
            return badRequest(errors, response, errorLogMessage);
        }

        response.setData(this.convertSectionToSectionResponse(sectionExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_SECTION)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<SectionResponse>> store(@Valid @RequestBody SectionDto sectionDto,
                                                       BindingResult result, @PathVariable(CATEGORY_ID_PARAM) Long categoryId) throws ParseException {

        LOG.info("Adding section: {}, category id {}", sectionDto.toString(), categoryId);
        Response<SectionResponse> response = new Response<>();

        Optional<Category> category = this._categoryService.findById(categoryId);
        boolean isCategoryNotPresent = !category.isPresent();
        if(isCategoryNotPresent) {
            BindResultUtils.bindErrorMessage(result, "section", "Nonexistent category.");
        }
        if(result.hasErrors()) {
            String errorLogMessage = "Error validating section: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        }

        Section section = this.convertDtoToSection(sectionDto);
        section.getCategories().add(category.get());
        category.get().getSections().add(section);

        this._sectionService.persist(section);

        response.setData(this.convertSectionToSectionResponse(section));
        return ResponseEntity.status(CREATED_STATUS).body(response);
    }

    @PutMapping(UPDATE_SECTION)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<SectionResponse>> update(@Valid @RequestBody SectionDto sectionDto,
                                                      BindingResult result, @PathVariable(ID_PARAM) Long id, @PathVariable(CATEGORY_ID_PARAM) Long categoryId) throws NoSuchAlgorithmException {

        LOG.info("Updating section id {}, section: {}, category id {}",id, sectionDto.toString(), categoryId);
        Response<SectionResponse> response = new Response<>();

        Optional<Section> sectionExists = this._sectionService.findById(id);
        boolean isSectionNotPresent = !sectionExists.isPresent();
        if(isSectionNotPresent) {
            BindResultUtils.bindErrorMessage(result,"section", "Nonexistent section." );
        }else {
            this.belongValidation(categoryId, sectionExists.get(), result);
        }

        if(result.hasErrors()) {
            String errorLogMessage = "Error validating section: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        }

        Section sectionExistsOpt = sectionExists.get();
        Section updatedSection = this.updateSection(sectionExistsOpt, sectionDto);

        response.setData(this.convertSectionToSectionResponse(updatedSection));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<SectionResponse>> delete(@PathVariable(ID_PARAM) Long id) {

        LOG.info("Deleting section: {}", id);
        Response<SectionResponse> response = new Response<>();

        boolean isSectionNotPresent = !this._sectionService.findById(id).isPresent();
        if (isSectionNotPresent) {
            String errorLogMessage = "Error removing section ID: {} Nonexistent section " + id;
            List<String> errors = Collections.singletonList("Error removing section. Nonexistent section!");
            return badRequest(errors, response, errorLogMessage);
        }else {
            this._sectionService.delete(id);
            return ResponseEntity.ok(new Response<>());
        }
    }

    private ResponseEntity<Response<SectionResponse>> badRequest (List<String> errors, Response<SectionResponse> response, String errorLog) {
        LOG.error(errorLog);
        response.getErrors().addAll(errors);
        return ResponseEntity.badRequest().body(response);
    }

    private Section updateSection(Section sectionToBeUpdated, SectionDto updatedSection) {
        sectionToBeUpdated.setTitle(updatedSection.getTitle());
        sectionToBeUpdated.setSubtitle(updatedSection.getSubtitle());
        sectionToBeUpdated.setUpdated_by(updatedSection.getUpdatedBy());
        sectionToBeUpdated.setSlug(updatedSection.getSlug());

        return  this._sectionService.persist(sectionToBeUpdated);
    }

    private SectionResponse convertSectionToSectionResponse(Section section){
        SectionResponse newSectionResponse = SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .subtitle(section.getSubtitle())
                .slug(section.getSlug())
                .createdBy(section.getCreated_by())
                .updatedBy(section.getUpdated_by())
                .articlesQtt(section.getArticles().size())
                .createdAt(section.getCreated_at())
                .updatedAt(section.getUpdated_at())
                .build();

        return newSectionResponse;
    }

    private Section convertDtoToSection(SectionDto sectionDto) {
        Section newSection = new Section();
        newSection.setTitle(sectionDto.getTitle());
        newSection.setSubtitle(sectionDto.getSubtitle());
        newSection.setSlug(sectionDto.getSlug());
        newSection.setCreated_by(sectionDto.getCreatedBy());
        newSection.setUpdated_by(sectionDto.getCreatedBy());

        return newSection;
    }

    private void belongValidation(Long categoryId, Section section, BindingResult result) {
        Optional<Category> category = this._categoryService.findById(categoryId);
        boolean isCategoryNotPresent = !category.isPresent();
        if(isCategoryNotPresent) {
            BindResultUtils.bindErrorMessage(result, "category", "Nonexistent category.");
        }else {
            boolean shouldNotContainsSection = !category.get().getSections().contains(section);
            if(shouldNotContainsSection) {
                BindResultUtils.bindErrorMessage(result, "section", "Section does not belong to category.");
            }
        }
    }
}
