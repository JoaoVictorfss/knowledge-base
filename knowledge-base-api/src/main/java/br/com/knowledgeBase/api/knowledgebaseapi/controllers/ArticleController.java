package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.ArticleDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Article;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.LikedType;
import br.com.knowledgeBase.api.knowledgebaseapi.enums.StatusType;
import br.com.knowledgeBase.api.knowledgebaseapi.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.ArticleService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
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
import javax.validation.ValidationException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/knowledgeBase-api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    /**
     * Returns a paginated list of published articles
     *
     * @return ResponseEntity<Response<ArticleDto>>
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Response<Page<ArticleDto>>> index(
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir)
    {
        LOG.info("Searching articles, page: {}", pag);
        Response<Page<ArticleDto>> response = new Response<Page<ArticleDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);

        Page<Article>articles = this.articleService.findAll(pageRequest);
        Page<ArticleDto> articlesDto = articles.map(this::convertArticleToArticleDto);

        response.setData(articlesDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns a paginated list of articles by category id
     *
     * @return ResponseEntity<Response<ArticleDto>>
     */
    @GetMapping(value = "/list/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<ArticleDto>>> listSectonsByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir)
    {

        LOG.info("Searching articles by category {}, page: {}", categoryId, pag);
        Response<Page<ArticleDto>> response = new Response<Page<ArticleDto>>();

        Optional<Category> category = this.categoryService.findById(categoryId);
        if(!category.isPresent()){
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }

        PageRequest pageRequest = PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);

        Page<Article>articles = this.articleService.findAllByCategoryId(categoryId, pageRequest);
        Page<ArticleDto> articlesDto = articles.map(this::convertArticleToArticleDto);

        response.setData(articlesDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new article
     *
     * @param articleDto
     * @param result
     * @return ResponseEntity<Response<ArticleDto>>
     * @throws ParseException
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<ArticleDto>> store(@Valid @RequestBody ArticleDto articleDto,
                                                      BindingResult result) throws ParseException {
        LOG.info("Adding article: {}", articleDto.toString());
        Response<ArticleDto> response = new Response<ArticleDto>();

       try {
           categoriesAndErrorsValidation(articleDto.getCategoriesId(), result);
           typeValidation(articleDto.getStatus(), articleDto.getLiked(),result);

           Article article = this.convertArticleDtoToArticle(articleDto);
           List<Category> categories = article.getCategories();

           articleDto.getCategoriesId().forEach(id -> {
               Category category = this.categoryService.findById(id).get();
               article.getCategories().add(category);
               category.getArticles().add(article);
           });

           this.articleService.persist(article);
           response.setData(this.convertArticleToArticleDto(article));

           return ResponseEntity.ok(response);
       }catch (ValidationException err){
           LOG.error("Error validating article: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

           return ResponseEntity.badRequest().body(response);
       }
    }

    /**
     * Update an article
     *
     * @param articleDto
     * @param id
     * @param result
     * @return ResponseEntity<Response<ArticleDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<ArticleDto>> update(@Valid @RequestBody ArticleDto articleDto,
                                                       BindingResult result, @PathVariable("id") Long id) throws NoSuchAlgorithmException {
        LOG.info("Updating article id {}, article: {}",id, articleDto.toString());
        Response<ArticleDto> response = new Response<ArticleDto>();

        Optional<Article>articleExists = this.articleService.findById(id);
        if(!articleExists.isPresent())
            result.addError(new ObjectError("article", "Nonexistent article."));

        try{
            this.updatedArticleDtoValidation(articleDto, result);

            articleExists.get().setTitle(articleDto.getTitle());
            articleExists.get().setSubtitle(articleDto.getSubtitle());
            articleExists.get().setUpdated_by(articleDto.getUpdated_by());
            articleExists.get().setContent(articleDto.getContent());
            articleExists.get().setStatus(StatusType.valueOf(articleDto.getStatus()));
            articleExists.get().setLiked(articleDto.getLiked());
            articleExists.get().setViewers(articleDto.getViewers());
            articleExists.get().setSlug(articleDto.getSlug());

            this.articleService.persist(articleExists.get());
            response.setData(this.convertArticleToArticleDto(articleExists.get()));

            return ResponseEntity.ok(response);
        }catch (ValidationException err){
            LOG.error("Error validating article: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete article by id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        LOG.info("Deleting article: {}", id);
        Response<String> response = new Response<String>();

        Optional<Article> article = this.articleService.findById(id);
        if (!article.isPresent()) {
            LOG.info("Error removing article ID: {} Nonexistent article.", id);
            response.getErrors().add("Error removing article. Nonexistent article!");

            return ResponseEntity.badRequest().body(response);
        }else{
            this.articleService.delete(id);

            return ResponseEntity.ok(new Response<String>());
        }
    }

    /**
     *Convert article to ArticleDto
     *
     * @param article
     * @return ArticleDto
     */
    private ArticleDto convertArticleToArticleDto(Article article){
        ArticleDto articleDto = new ArticleDto();

        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setSubtitle(article.getSubtitle());
        articleDto.setContent(article.getContent());
        articleDto.setStatus(article.getStatus().toString());
        articleDto.setLiked(article.getLiked());
        articleDto.setViewers(article.getViewers());
        articleDto.setSlug(article.getSlug());
        articleDto.setCreated_by(article.getCreated_by());
        articleDto.setUpdated_by(article.getUpdated_by());
        articleDto.setCreated_at(article.getCreated_at());
        articleDto.setUpdated_at(article.getUpdated_at());

        return  articleDto;
    }

    /**
     * Convert DTO data to Article
     *
     * @param articleDto
     * @return Article
     */
    private Article convertArticleDtoToArticle(ArticleDto articleDto){
        Article article = new Article();

        article.setTitle(articleDto.getTitle());
        article.setSubtitle(articleDto.getSubtitle());
        article.setContent(articleDto.getContent());
        article.setStatus(StatusType.valueOf(articleDto.getStatus()));
        article.setLiked(articleDto.getLiked());
        article.setViewers(articleDto.getViewers());
        article.setSlug(articleDto.getSlug());
        article.setCreated_by(articleDto.getCreated_by());
        article.setUpdated_by(articleDto.getCreated_by());

        return article;
    }

    /**
     * Search categories by id and check if it exists
     *
     * @param categories
     * @param result
     */
    private void categoriesAndErrorsValidation(List<Long> categories, BindingResult result){
        if(result.hasErrors()){
            throw new ValidationException();

        }else if(categories.isEmpty()){
            result.addError(new ObjectError("categories", "Categories cannot be empty"));
            throw new ValidationException();
        }else{
            categories.forEach(categoryId -> {
                Optional<Category> category = this.categoryService.findById(categoryId);

                if(!category.isPresent()){
                    result.addError(new ObjectError("category", "Nonexistent category id " + categoryId));
                    throw new ValidationException();
                }
            });
        }
    }


    /**
     * Check if the enum types are valid
     *
     * @param status
     * @param liked
     * @param result
     */
    private void typeValidation(String status, String liked, BindingResult result){
        try {
            StatusType statusType = StatusType.valueOf(status);

            if(liked != null){
                LikedType likedTypeType = LikedType.valueOf(liked);
            }

        } catch (java.lang.IllegalArgumentException err) {
            result.addError(new ObjectError("status", "Invalid status type or liked type. " +
                    "Accepted values for the liked field: POOR, AVERAGE and GREAT." +
                    "Accepted values for the status field: CANCEL, PUBLISH and DRAFT(Default)"));

            throw new ValidationException();
        }
    }

    /**
     * Check if the updated article has no errors and the status type and liked type are valid
     *
     * @param articleDto
     * @param result
     */
    private void updatedArticleDtoValidation(ArticleDto articleDto, BindingResult result){
        if (result.hasErrors()){
            throw new ValidationException();
        }

        this.typeValidation(articleDto.getStatus(), articleDto.getLiked(), result);
    }
}
