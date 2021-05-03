package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.Data.dtos.CategoryDto;
import br.com.knowledgeBase.api.knowledgebaseapi.Data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import org.springframework.data.domain.Sort.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/knowledgeBase-api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SectionService sectionService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    @GetMapping(value = "/list")
    public ResponseEntity<Response<Page<CategoryDto>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "ASC") String dir) {
        LOG.info("Searching categories, page: {}", pag);
        Response<Page<CategoryDto>> response = new Response<Page<CategoryDto>>();
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Direction.valueOf(dir), ord);

        Page<Category>categories = this.categoryService.findAll(pageRequest);
        Page<CategoryDto> categoriesDto = categories.map(this::convertCategoryToCategoryDto);

        response.setData(categoriesDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/category/{id}")
    public ResponseEntity<Response<CategoryDto>> showById(@PathVariable("id") Long id) {
        LOG.info("Searching category id {}", id);
        Response<CategoryDto> response = new Response<CategoryDto>();

        Optional<Category>categoryExists = this.categoryService.findById(id);
        if(!categoryExists.isPresent()) {
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertCategoryToCategoryDto(categoryExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<CategoryDto>> store(@Valid @RequestBody CategoryDto categoryDto,
                                                  BindingResult result) throws ParseException {
        LOG.info("Adding category: {}", categoryDto.toString());
        Response<CategoryDto> response = new Response<CategoryDto>();

        if (result.hasErrors()) {
            LOG.error("Error validating category: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        } else {
            Category category = this.convertDtoToCategory(categoryDto);
            this.categoryService.persist(category);
            response.setData(this.convertCategoryToCategoryDto(category));

            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<CategoryDto>> update(@PathVariable("id") Long id,
                                                   @Valid @RequestBody CategoryDto categoryDto, BindingResult result) throws NoSuchAlgorithmException {
        LOG.info("Updating category: {}", categoryDto.toString());
        Response<CategoryDto> response = new Response<CategoryDto>();

        Optional<Category>categoryExists = this.categoryService.findById(id);
        if (!categoryExists.isPresent()) {
            result.addError(new ObjectError("category", "Nonexistent category."));
        }
        if(result.hasErrors()){
            LOG.error("Error validating category: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Category categoryExistsOpt = categoryExists.get();
        categoryExistsOpt.setTitle(categoryDto.getTitle());
        categoryExistsOpt.setSubtitle(categoryDto.getSubtitle());
        categoryExistsOpt.setSlug(categoryDto.getSlug());
        categoryExistsOpt.setUpdated_by(categoryDto.getCreatedBy());

        this.categoryService.persist(categoryExistsOpt);
        response.setData(this.convertCategoryToCategoryDto(categoryExistsOpt));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        LOG.info("Deleting category: {}", id);
        Response<String> response = new Response<String>();

        Optional<Category> category = this.categoryService.findById(id);
        if (!category.isPresent()) {
            LOG.info("Error removing category ID: {} Nonexistent category.", id);
            response.getErrors().add("Error removing category. Nonexistent category!");
            return ResponseEntity.badRequest().body(response);
        }else{
            this.categoryService.delete(id);
            return ResponseEntity.ok(new Response<String>());
        }
    }


    private CategoryDto convertCategoryToCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        categoryDto.setSubtitle(category.getSubtitle());
        categoryDto.setSlug(category.getSlug());
        categoryDto.setCreatedAt(category.getCreated_at());
        categoryDto.setUpdatedAt(category.getUpdated_at());
        categoryDto.setCreatedBy(category.getCreated_by());
        categoryDto.setUpdatedBy(category.getUpdated_by());
        categoryDto.setArticlesQtt(category.getArticles().size());
        categoryDto.setSectionsQtt(category.getSections().size());

        return  categoryDto;
    }

    private Category convertDtoToCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());

        category.setCreated_by(categoryDto.getCreatedBy());
        category.setUpdated_by(categoryDto.getUpdatedBy());
        category.setSubtitle(categoryDto.getSubtitle());
        category.setSlug(categoryDto.getSlug());

        return category;
    }
}
