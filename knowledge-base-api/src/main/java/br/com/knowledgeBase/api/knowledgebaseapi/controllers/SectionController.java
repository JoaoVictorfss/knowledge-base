package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.SectionDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.SectionResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(SECTION_PATH)
@CrossOrigin(origins = "*")
public class SectionController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CategoryService categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    @GetMapping(value = LIST)
    public ResponseEntity<Response<Page<SectionResponse>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        LOG.info("Searching sections, page: {}", pag);
        Response<Page<SectionResponse>> response = new Response<>();
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);

        Page<Section>sections = this.sectionService.findAll(pageRequest);
        Page<SectionResponse> sectionsResponse = sections.map(this::convertSectionToSectionResponse);

        response.setData(sectionsResponse);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = FIND_SECTIONS_BY_CATEGORY_ID)
    public ResponseEntity<Response<Page<SectionResponse>>> listSectionsByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        LOG.info("Searching sections by category {}, page: {}", categoryId, pag);
        Response<Page<SectionResponse>> response = new Response<>();

        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }
        
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Section>sections = this.sectionService.findAllByCategoryId(categoryId, pageRequest);
        Page<SectionResponse> sectionsResponse = sections.map(this::convertSectionToSectionResponse);
        response.setData(sectionsResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = FIND_SECTION_BY_ID)
    public ResponseEntity<Response<SectionResponse>> showById(@PathVariable("id") Long id) {

        LOG.info("Searching section id {}", id);
        Response<SectionResponse> response = new Response<>();

        Optional<Section>sectionExists = this.sectionService.findById(id);
        if(!sectionExists.isPresent()) {
            LOG.info("Error. Nonexistent section.");
            response.getErrors().add("Error. Nonexistent section.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertSectionToSectionResponse(sectionExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_SECTION)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<SectionResponse>> store(@Valid @RequestBody SectionDto sectionDto,
                                                       BindingResult result, @PathVariable("categoryId") Long categoryId) throws ParseException {

        LOG.info("Adding section: {}, category id {}", sectionDto.toString(), categoryId);
        Response<SectionResponse> response = new Response<>();

        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
           result.addError(new ObjectError("section", "Nonexistent category."));
        }
        if(result.hasErrors()){
           LOG.error("Error validating section: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

           return ResponseEntity.badRequest().body(response);
        }

        Section section = this.convertDtoToSection(sectionDto);
        section.getCategories().add(category.get());
        category.get().getSections().add(section);

        this.sectionService.persist(section);
        response.setData(this.convertSectionToSectionResponse(section));

       return ResponseEntity.status(201).body(response);
    }

    @PutMapping(UPDATE_SECTION)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<SectionResponse>> update(@Valid @RequestBody SectionDto sectionDto,
                                                      BindingResult result, @PathVariable("id") Long id, @PathVariable("categoryId") Long categoryId) throws NoSuchAlgorithmException {

        LOG.info("Updating section id {}, section: {}, category id {}",id, sectionDto.toString(), categoryId);
        Response<SectionResponse> response = new Response<>();

        Optional<Section>sectionExists = this.sectionService.findById(id);
        if(!sectionExists.isPresent()){
            result.addError(new ObjectError("section", "Nonexistent section."));
        }else{
            this.belongValidation(categoryId, sectionExists.get(), result);
        }

        if(result.hasErrors()){
            LOG.error("Error validating section: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Section sectionExistsOpt = sectionExists.get();
        sectionExistsOpt.setTitle(sectionDto.getTitle());
        sectionExistsOpt.setSubtitle(sectionDto.getSubtitle());
        sectionExistsOpt.setUpdated_by(sectionDto.getUpdatedBy());
        sectionExistsOpt.setSlug(sectionDto.getSlug());

        this.sectionService.persist(sectionExistsOpt);
        response.setData(this.convertSectionToSectionResponse(sectionExistsOpt));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {

        LOG.info("Deleting section: {}", id);
        Response<String> response = new Response<String>();

        Optional<Section> section = this.sectionService.findById(id);
        if (!section.isPresent()) {
            LOG.info("Error removing section ID: {} Nonexistent section.", id);
            response.getErrors().add("Error removing section. Nonexistent section!");

            return ResponseEntity.badRequest().body(response);
        }else{
            this.sectionService.delete(id);

            return ResponseEntity.ok(new Response<String>());
        }
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

    private Section convertDtoToSection(SectionDto sectionDto){
        Section section = new Section();
        section.setTitle(sectionDto.getTitle());
        section.setSubtitle(sectionDto.getSubtitle());
        section.setSlug(sectionDto.getSlug());
        section.setCreated_by(sectionDto.getCreatedBy());
        section.setUpdated_by(sectionDto.getCreatedBy());

        return section;
    }

    private void belongValidation(Long categoryId, Section section, BindingResult result){
        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
            result.addError(new ObjectError("category", "Nonexistent category."));
        }else{
            if(!category.get().getSections().contains(section)){
                result.addError(new ObjectError("section", "Section does not belong to category."));
            }
        }
    }
}
