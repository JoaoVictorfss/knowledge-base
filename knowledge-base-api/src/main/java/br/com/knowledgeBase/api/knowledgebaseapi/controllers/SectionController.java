package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
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
     * @return ResponseEntity<Response<CategoryDto>>
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
     * Add a new section
     *
     * @param sectionDto
     * @param result
     * @param categoryId
     * @return ResponseEntity<Response<SectionDto>>
     * @throws ParseException
     */
    @PostMapping("/create/category/{categoryId}")
    public ResponseEntity<Response<SectionDto>> store(@Valid @RequestBody SectionDto sectionDto,
                                                       BindingResult result, @PathVariable("categoryId") Long categoryId) throws ParseException {
        LOG.info("Adding section: {}, category id {}", sectionDto.toString(), categoryId);
        Response<SectionDto> response = new Response<SectionDto>();

       try{
            this.sectionValidation(categoryId, result);
            Section section = this.convertDtoToSection(sectionDto);

            this.sectionService.persist(section);
           //TODO adicionar sessão à categoria
           response.setData(this.convertSectionToSectionDto(section));

           return ResponseEntity.ok(response);

       }catch (ValidationException err){
           LOG.error("Error validating section: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

           return ResponseEntity.badRequest().body(response);
       }
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
    public ResponseEntity<Response<SectionDto>> update(@Valid @RequestBody SectionDto sectionDto,
                                                      BindingResult result, @PathVariable("id") Long id, @PathVariable("categoryId") Long categoryId) throws NoSuchAlgorithmException {
        LOG.info("Updating section id {}, section: {}, category id {}",id, sectionDto.toString(), categoryId);
        Response<SectionDto> response = new Response<SectionDto>();
         //TODO veriifcar se a seção pertene a categoria
        try{
            Optional<Section>sectionExists = this.sectionService.findById(id);

            if(!sectionExists.isPresent()){
                result.addError(new ObjectError("section", "Nonexistent section."));
            }

            this.sectionValidation(categoryId, result);

            sectionExists.get().setTitle(sectionDto.getTitle());
            sectionExists.get().setSubtitle(sectionDto.getSubtitle());
            sectionExists.get().setUpdated_by(sectionDto.getUpdated_by());
            sectionExists.get().setCreated_by(sectionDto.getCreated_by());
            sectionExists.get().setSlug(sectionDto.getSlug());

            this.sectionService.persist(sectionExists.get());
            response.setData(this.convertSectionToSectionDto(sectionExists.get()));

            return ResponseEntity.ok(response);
        }catch (ValidationException err){
            LOG.error("Error validating section: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete section by id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "/delete/{id}")
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
        //sectionDto.setCreated_at(Optional.of(section.getCreated_at()));
        //sectionDto.setUpdated_at(Optional.of(section.getUpdated_at()));
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
     * Validate a section. Checks if that there are no errors or that the category exists.
     *
     * @param categoryId
     * @param result
     */
    private void sectionValidation(Long categoryId, BindingResult result){

        if(result.hasErrors()){
            throw new ValidationException();
        }

        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
            result.addError(new ObjectError("section", "Nonexistent category."));
            throw new ValidationException();
        }

    }
}
