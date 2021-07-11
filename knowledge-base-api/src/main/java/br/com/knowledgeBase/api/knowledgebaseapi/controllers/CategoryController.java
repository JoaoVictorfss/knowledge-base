package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.CategoryDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.CategoryResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import br.com.knowledgeBase.api.knowledgebaseapi.utils.BindResultUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(CATEGORY_PATH)
@CrossOrigin(origins = "*")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService _categoryService;

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
        Page<Category> categories = this._categoryService.findAll(pageRequest);
        Page<CategoryResponse> categoriesResponse = categories.map(this::convertCategoryToCategoryResponse);

        response.setData(categoriesResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = FIND_CATEGORY_BY_ID)
    public ResponseEntity<Response<CategoryResponse>> showById(@PathVariable("id") Long id) {

        LOG.info("Searching category id {}", id);
        Response<CategoryResponse> response = new Response<>();

        Optional<Category> categoryExists = this._categoryService.findById(id);
        boolean isCategoryNotPresent = !categoryExists.isPresent();
        if(isCategoryNotPresent) {
            String errorLogMessage = "Error. Nonexistent category.";
            List<String> errors = Arrays.asList(errorLogMessage);
            return badRequest(errors, response, errorLogMessage);
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
            String errorLogMessage = "Error validating category: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        } else {
            Category category = this.convertDtoToCategory(categoryDto);
            this._categoryService.persist(category);
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

        Optional<Category> categoryExists = this._categoryService.findById(id);
        boolean isCategoryNoPresent = !categoryExists.isPresent();
        if (isCategoryNoPresent) {
            BindResultUtils.bindErrorMessage(result, "category", "Nonexistent category.");
        }
        if(result.hasErrors()) {
            String errorLogMessage = "Error validating category: " + result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        }

        Category categoryExistsOpt = categoryExists.get();
        Category updatedCategory = this.updateCategory(categoryExistsOpt, categoryDto);
        response.setData(this.convertCategoryToCategoryResponse(updatedCategory));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<CategoryResponse>> delete(@PathVariable("id") Long id) {

        LOG.info("Deleting category: {}", id);
        Response<CategoryResponse> response = new Response<>();

        Optional<Category> category = this._categoryService.findById(id);
        if (!category.isPresent()) {
            String errorLogMessage = "Error removing category ID: {} Nonexistent category " + id;
            List<String> errors = Arrays.asList(errorLogMessage);
            return badRequest(errors, response, errorLogMessage);
        }else {
            this._categoryService.delete(id);
            return ResponseEntity.ok(new Response<>());
        }
    }

    private ResponseEntity<Response<CategoryResponse>> badRequest (List<String> errors, Response<CategoryResponse> response, String errorLog) {
        LOG.error(errorLog);
        response.getErrors().addAll(errors);
        return ResponseEntity.badRequest().body(response);
    }

    private Category updateCategory(Category categorytoBeUpdated, CategoryDto updatedCategory) {
        categorytoBeUpdated.setTitle(updatedCategory.getTitle());
        categorytoBeUpdated.setSubtitle(updatedCategory.getSubtitle());
        categorytoBeUpdated.setSlug(updatedCategory.getSlug());
        categorytoBeUpdated.setUpdated_by(updatedCategory.getCreatedBy());

        return this._categoryService.persist(categorytoBeUpdated);
    }

    private CategoryResponse convertCategoryToCategoryResponse(Category category) {
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

    private Category convertDtoToCategory(CategoryDto categoryDto) {
        Category newCategory = new Category();
        newCategory.setTitle(categoryDto.getTitle());
        newCategory.setCreated_by(categoryDto.getCreatedBy());
        newCategory.setUpdated_by(categoryDto.getUpdatedBy());
        newCategory.setSubtitle(categoryDto.getSubtitle());
        newCategory.setSlug(categoryDto.getSlug());

        return newCategory;
    }
}
