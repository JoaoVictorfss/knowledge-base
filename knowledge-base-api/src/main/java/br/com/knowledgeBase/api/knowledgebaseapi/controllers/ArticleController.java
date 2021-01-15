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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
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
     * Add a new article
     *
     * @param articleDto
     * @param result
     * @return ResponseEntity<Response<ArticleDto>>
     * @throws ParseException
     */
    @PostMapping("/create")
    public ResponseEntity<Response<ArticleDto>> store(@Valid @RequestBody ArticleDto articleDto,
                                                      BindingResult result) throws ParseException {
        LOG.info("Adding article: {}", articleDto.toString());
        Response<ArticleDto> response = new Response<ArticleDto>();

       try {
           categoriesValidation(articleDto.getCategoriesId(), result);
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

        articleDto.setViews(article.getViews());
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

        article.setViews(articleDto.getViews());
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
    private void categoriesValidation(List<Long> categories, BindingResult result){
        if(result.hasErrors()){
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

    private void typeValidation(String status, String liked, BindingResult result){
        try {
            StatusType statusType = StatusType.valueOf(status);

            if(liked != null){
                LikedType likedTypeType = LikedType.valueOf(liked);
            }

        } catch (java.lang.IllegalArgumentException ex) {
            result.addError(new ObjectError("status", "Invalid status type or liked type. " +
                    "Accepted values for the liked field: POOR, AVERAGE and GREAT." +
                    "Accepted values for the status field: CANCEL, PUBLISH and DRAFT(Default)"));
            throw new ValidationException();
        }
    }
}
