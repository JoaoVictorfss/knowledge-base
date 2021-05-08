package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.CategoryDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.CategoryResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
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
@RequestMapping(CATEGORY_PATH)
@CrossOrigin(origins = "*")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SectionService sectionService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    @GetMapping(value = LIST)
    public ResponseEntity<Response<Page<CategoryResponse>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "ASC") String dir) {

        LOG.info("Searching categories, page: {}", pag);
        Response<Page<CategoryResponse>> response = new Response<>();
        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Direction.valueOf(dir), ord);

        Page<Category>categories = this.categoryService.findAll(pageRequest);
        Page<CategoryResponse> categoriesResponse = categories.map(this::convertCategoryToCategoryResponse);
        response.setData(categoriesResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = FIND_CATEGORY_BY_ID)
    public ResponseEntity<Response<CategoryResponse>> showById(@PathVariable("id") Long id) {

        LOG.info("Searching category id {}", id);
        Response<CategoryResponse> response = new Response<>();

        Optional<Category>categoryExists = this.categoryService.findById(id);
        if(!categoryExists.isPresent()) {
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertCategoryToCategoryResponse(categoryExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<CategoryResponse>> store(@Valid @RequestBody CategoryDto categoryDto,
                                                  BindingResult result) throws ParseException {

        LOG.info("Adding category: {}", categoryDto.toString());
        Response<CategoryResponse> response = new Response<>();

        if (result.hasErrors()) {
            LOG.error("Error validating category: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        } else {
            Category category = this.convertDtoToCategory(categoryDto);
            this.categoryService.persist(category);
            response.setData(this.convertCategoryToCategoryResponse(category));

            return ResponseEntity.status(201).body(response);
        }
    }

    @PutMapping(value = UPDATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<CategoryResponse>> update(@PathVariable("id") Long id,
                                                   @Valid @RequestBody CategoryDto categoryDto, BindingResult result) throws NoSuchAlgorithmException {

        LOG.info("Updating category: {}", categoryDto.toString());
        Response<CategoryResponse> response = new Response<>();

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
        response.setData(this.convertCategoryToCategoryResponse(categoryExistsOpt));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
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
            return ResponseEntity.ok(new Response<>());
        }
    }

    private CategoryResponse convertCategoryToCategoryResponse(Category category){
        CategoryResponse newCategoryResponse = CategoryResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .subtitle(category.getSubtitle())
                .slug(category.getSlug())
                .createdBy(category.getCreated_by())
                .updatedBy(category.getUpdated_by())
                .articlesQtt(category.getArticles().size())
                .sectionsQtt(category.getSections().size())
                .createdAt(category.getCreated_at())
                .updatedAt(category.getUpdated_at())
                .build();

        return newCategoryResponse;
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
