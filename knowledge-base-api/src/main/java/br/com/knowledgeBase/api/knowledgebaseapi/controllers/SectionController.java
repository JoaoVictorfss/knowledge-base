package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.CategoryDto;
import br.com.knowledgeBase.api.knowledgebaseapi.dtos.SectionDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
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
@RequestMapping("/knowledgeBase-api/sections")
@CrossOrigin(origins = "*")
public class SectionController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CategoryService categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    /**
     * Returns a paginated list of sections
     *
     * @return ResponseEntity<Response<SectionDto>>
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Response<Page<SectionDto>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir)
    {
        LOG.info("Searching sections, page: {}", pag);
        Response<Page<SectionDto>> response = new Response<Page<SectionDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);

        Page<Section>sections = this.sectionService.findAll(pageRequest);
        Page<SectionDto> sectionsDto = sections.map(this::convertSectionToSectionDto);

        response.setData(sectionsDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns a paginated list of sections by category id
     *
     * @return ResponseEntity<Response<SectionDto>>
     */
    @GetMapping(value = "/list/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<SectionDto>>> listSectonsByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir)
    {

        LOG.info("Searching sections by category {}, page: {}", categoryId, pag);
        Response<Page<SectionDto>> response = new Response<Page<SectionDto>>();

        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }
        
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);

        Page<Section>sections = this.sectionService.findAllByCategoryId(categoryId, pageRequest);
        Page<SectionDto> sectionsDto = sections.map(this::convertSectionToSectionDto);

        response.setData(sectionsDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Return a section by id
     *
     * @param id
     * @return ResponseEntity<Response<SectionDto>>
     */
    @GetMapping(value = "/section/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<SectionDto>> showById(@PathVariable("id") Long id){
        LOG.info("Searching section id {}", id);
        Response<SectionDto> response = new Response<SectionDto>();

        Optional<Section>sectionExists = this.sectionService.findById(id);
        if(!sectionExists.isPresent()) {
            LOG.info("Error. Nonexistent section.");
            response.getErrors().add("Error. Nonexistent section.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertSectionToSectionDto(sectionExists.get()));
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new section
     *
     * @param sectionDto
     * @param result
     * @param categoryId
     * @return ResponseEntity<Response<SectionDto>>
     * @throws ParseException
     */
    @PostMapping("/create/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<SectionDto>> store(@Valid @RequestBody SectionDto sectionDto,
                                                       BindingResult result, @PathVariable("categoryId") Long categoryId) throws ParseException {
        LOG.info("Adding section: {}, category id {}", sectionDto.toString(), categoryId);
        Response<SectionDto> response = new Response<SectionDto>();

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
        response.setData(this.convertSectionToSectionDto(section));

       return ResponseEntity.ok(response);
    }

    /**
     * Update a section
     *
     * @param sectionDto
     * @param id
     * @param result
     * @param categoryId
     * @return ResponseEntity<Response<SectionDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping("/update/{id}/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<SectionDto>> update(@Valid @RequestBody SectionDto sectionDto,
                                                      BindingResult result, @PathVariable("id") Long id, @PathVariable("categoryId") Long categoryId) throws NoSuchAlgorithmException {
        LOG.info("Updating section id {}, section: {}, category id {}",id, sectionDto.toString(), categoryId);
        Response<SectionDto> response = new Response<SectionDto>();

        Optional<Section>sectionExists = this.sectionService.findById(id);
        if(!sectionExists.isPresent())
            result.addError(new ObjectError("section", "Nonexistent section."));
        else
             this.belongValidation(categoryId, sectionExists.get(), result);


        if(result.hasErrors()){
            LOG.error("Error validating section: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        sectionExists.get().setTitle(sectionDto.getTitle());
        sectionExists.get().setSubtitle(sectionDto.getSubtitle());
        sectionExists.get().setUpdated_by(sectionDto.getUpdated_by());
        sectionExists.get().setSlug(sectionDto.getSlug());

        this.sectionService.persist(sectionExists.get());
        response.setData(this.convertSectionToSectionDto(sectionExists.get()));

        return ResponseEntity.ok(response);
    }

    /**
     * Delete section by id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "/delete/{id}")
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

    /**
     *Convert section to SectionDto
     *
     * @param section
     * @return SectionDto
     */
    private SectionDto convertSectionToSectionDto(Section section){
        SectionDto sectionDto = new SectionDto();

        sectionDto.setId(section.getId());
        sectionDto.setTitle(section.getTitle());
        sectionDto.setSubtitle(section.getSubtitle());
        sectionDto.setSlug(section.getSlug());
        sectionDto.setCreated_at(section.getCreated_at());
        sectionDto.setUpdated_at(section.getUpdated_at());
        sectionDto.setCreated_by(section.getCreated_by());
        sectionDto.setUpdated_by(section.getUpdated_by());

        return  sectionDto;
    }

    /**
     * Convert DTO data to Section
     *
     * @param sectionDto
     * @return Section
     */
    private Section convertDtoToSection(SectionDto sectionDto){
        Section section = new Section();
        section.setTitle(sectionDto.getTitle());
        section.setSubtitle(sectionDto.getSubtitle());
        section.setSlug(sectionDto.getSlug());
        section.setCreated_by(sectionDto.getCreated_by());
        section.setUpdated_by(sectionDto.getCreated_by());

        return section;
    }

    /**
     * Search category by id and check if the section belongs to it
     *
     * @param section
     * @param categoryId
     * @param result
     */
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
