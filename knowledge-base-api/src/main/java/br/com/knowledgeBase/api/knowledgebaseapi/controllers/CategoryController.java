package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.CategoryDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
import org.springframework.data.domain.Sort.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    /**
     * Returns a paginated list of categories
     *
     * @return ResponseEntity<Response<CategoryDto>>
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Response<Page<CategoryDto>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir)
    {
        LOG.info("Searching categories, page: {}", pag);
        Response<Page<CategoryDto>> response = new Response<Page<CategoryDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Direction.valueOf(dir), ord);

        Page<Category>categories = this.categoryService.findAll(pageRequest);
        Page<CategoryDto> categoriesDto = categories.map(this::convertCategoryToCategoryDto);

        response.setData(categoriesDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new category
     *
     * @param categoryDto
     * @param result
     * @return ResponseEntity<Response<CategoryDto>>
     * @throws ParseException
     */
    @PostMapping("/create")
    public ResponseEntity<Response<CategoryDto>> store(@Valid @RequestBody CategoryDto categoryDto,
                                                  BindingResult result) throws ParseException {
        LOG.info("Adding category: {}", categoryDto.toString());
        Response<CategoryDto> response = new Response<CategoryDto>();

        if (result.hasErrors()) {//errros de validação
            LOG.error("Error validating category: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }else {
            Category category = this.convertDtoToCategory(categoryDto);
            this.categoryService.persist(category);
            response.setData(this.convertCategoryToCategoryDto(category));

            return ResponseEntity.ok(response);
        }
    }

    /**
     * Update category data
     *
     * @param categoryDto
     * @param result
     * @return ResponseEntity<Response<CategoryDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping(value = "/update/{id}")
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
        }else{
            categoryExists.get().setTitle(categoryDto.getTitle());
            categoryExists.get().setSubtitle(categoryDto.getSubtitle());
            categoryExists.get().setSlug(categoryDto.getSlug());
            categoryExists.get().setCreated_by(categoryDto.getCreated_by());
            categoryExists.get().setUpdated_by(categoryDto.getCreated_by());

            this.categoryService.persist(categoryExists.get());
            response.setData(this.convertCategoryToCategoryDto(categoryExists.get()));

            return ResponseEntity.ok(response);
        }

    }

    /**
     * Delete category by id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "/delete/{id}")
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


    /**
     * Convert category to CategoryDto
     *
     * @param category
     * @return CategoryDto
     */
    private CategoryDto convertCategoryToCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        categoryDto.setSubtitle(category.getSubtitle());
        categoryDto.setSlug(category.getSlug());
        //categoryDto.setCreated_at(Optional.of(category.getCreated_at()));
        //categoryDto.setUpdated_at(Optional.of(category.getUpdated_at()));
        categoryDto.setCreated_by(category.getCreated_by());
        categoryDto.setUpdated_by(category.getUpdated_by());

        return  categoryDto;
    }

    /**
     * Convert DTO data to Category
     *
     * @param categoryDto
     * @return Category
     */
    private Category convertDtoToCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());

        category.setCreated_by(categoryDto.getCreated_by());
        category.setUpdated_by(categoryDto.getUpdated_by());
        category.setSubtitle(categoryDto.getSubtitle());
        category.setSlug(categoryDto.getSlug());

        return category;
    }
}
