package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.GeneralConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.ArticleDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Article;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.data.enums.StatusEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.ArticleResponse;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.ArticleService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import br.com.knowledgeBase.api.knowledgebaseapi.services.CategoryService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping(ARTICLE_PATH)
@CrossOrigin(origins = ANYWHERE)
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService _articleService;

    @Autowired
    private SectionService _sectionService;

    @Autowired
    private CategoryService _categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    @GetMapping(value = LIST_BY_CATEGORY)
    public void listAllPublishedArticlesByCategoryId(
            @PathVariable(CATEGORY_ID_PARAM) Long categoryId,
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by category {}, page: {}", categoryId, pag);

        PageRequest pageRequest = PageRequest.of(pag, 10, Sort.Direction.valueOf(dir), ord);
        Page<Article> articles = this._articleService.findAllPublishedByCategoryId(categoryId, pageRequest);

        this.listAllArticlesByCategory(categoryId, articles);
    }

    @GetMapping(value = LIST_BY_SECTION)
    public void listAllPublishedArticlesBySectionId(
            @PathVariable(SECTION_ID_PARAM) Long sectionId,
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "DESC") String dir) {

        LOG.info("Searching articles by section {}, page: {}", sectionId, pag);

        PageRequest pageRequest =  PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Article> articles = this._articleService.findAllPublishedBySectionId(sectionId, pageRequest);

        this.listAllArticlesBySection(sectionId, articles);
    }

    @GetMapping(value = SEARCH)
    public ResponseEntity<Response<Page<ArticleResponse>>> search(@PathVariable("param") String param) {

        LOG.info("Searching articles by {}", param);
        Response<Page<ArticleResponse>> response = new Response<>();

        PageRequest pageRequest = PageRequest.of(0, 8, Sort.Direction.ASC, "title");
        Page<Article> articles = this._articleService.findAllByParam(param, pageRequest);
        Page<ArticleResponse> articlesDto = articles.map(this::convertArticleToArticleResponse);

        response.setData(articlesDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = PRIVATE_LIST_BY_CATEGORY)
    @PreAuthorize(ONLY_ADMIN)
    public void listAllArticlesByCategoryId(
            @PathVariable(CATEGORY_ID_PARAM) Long categoryId,
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by category {}, page: {}", categoryId, pag);

        PageRequest pageRequest =  PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Article> articles = this._articleService.findAllByCategoryId(categoryId, pageRequest);

        this.listAllArticlesByCategory(categoryId, articles);
    }

    @GetMapping(value = PRIVATE_LIST_BY_SECTION )
    @PreAuthorize(ONLY_ADMIN)
    public void listAllArticlesBySectionId(
            @PathVariable(SECTION_ID_PARAM) Long sectionId,
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") int pag,
            @RequestParam(value = ORDERING_PARAM, defaultValue = "title") String ord,
            @RequestParam(value = DIRECTION_PARAM, defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by section {}, page: {}", sectionId, pag);

        PageRequest pageRequest =  PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Article> articles = this._articleService.findAllBySectionId(sectionId, pageRequest);

        this.listAllArticlesBySection(sectionId, articles);
    }

    @GetMapping(value = FIND_BY_ARTICLE_ID)
    public ResponseEntity<Response<ArticleResponse>> showById(@PathVariable(ID_PARAM) Long id) {

        LOG.info("Searching article id {}", id);
        Response<ArticleResponse> response = new Response<>();

        Optional<Article> articleExists = this._articleService.findById(id);
        boolean isArticleNotPresent = !articleExists.isPresent();
        if(isArticleNotPresent) {
            String errorLogMessage = "Error. Nonexistent article.";
            List<String> errors = Collections.singletonList(errorLogMessage);
            return badRequest(errors, response, errorLogMessage);
        }

        response.setData(this.convertArticleToArticleResponse(articleExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<ArticleResponse>> store(@Valid @RequestBody ArticleDto articleDto,
                                                      BindingResult result) throws ParseException {

        LOG.info("Adding article: {}", articleDto.toString());
        Response<ArticleResponse> response = new Response<>();

        articleDtoValidation(articleDto, result);
        categoriesValidation(articleDto.getCategoriesId(),result);
        if(result.hasErrors()) {
            String errorLogMessage = "Error validating article: " +  result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        }

        Article article = this.convertArticleDtoToArticle(articleDto);
        articleDto.getCategoriesId().forEach(id -> {
           Category category = this._categoryService.findById(id).get();
           article.getCategories().add(category);
           category.getArticles().add(article);
        });

        articleDto.getSectionId().ifPresent( id -> {
            Section section = this._sectionService.findById(id).get();
            section.getArticles().add(article);
            article.getSections().add(section);
        });

        this._articleService.persist(article);

        response.setData(this.convertArticleToArticleResponse(article));
        return ResponseEntity.status(CREATED_STATUS).body(response);
    }

    @PutMapping(UPDATE)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<ArticleResponse>> update(@Valid @RequestBody ArticleDto articleDto,
                                                       BindingResult result, @PathVariable(ID_PARAM) Long id) throws NoSuchAlgorithmException {

        LOG.info("Updating article id {}, article: {}",id, articleDto.toString());
        Response<ArticleResponse> response = new Response<ArticleResponse>();

        Optional<Article> articleExists = this._articleService.findById(id);
        boolean isArticleNotPresent = !articleExists.isPresent();
        if(isArticleNotPresent) {
            BindResultUtils.bindErrorMessage(result, "article", "Nonexistent article.");
        }
        articleDtoValidation(articleDto, result);
        if(result.hasErrors()) {
            String errorLogMessage = "Error validating article: " +  result.getAllErrors();
            List<String> errors = BindResultUtils.getAllErrorMessages(result);
            return badRequest(errors, response, errorLogMessage);
        }

        Article articleToBeUpdated = articleExists.get();
        Article savedArticle = this.updateArticle(articleToBeUpdated, articleDto);
        response.setData(this.convertArticleToArticleResponse(savedArticle));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
    @PreAuthorize(ONLY_ADMIN)
    public ResponseEntity<Response<ArticleResponse>> delete(@PathVariable(ID_PARAM) Long id) {

        LOG.info("Deleting article: {}", id);
        Response<ArticleResponse> response = new Response<>();

        boolean isArticleNotPresent = !this._articleService.findById(id).isPresent();
        if (isArticleNotPresent) {
            String errorLogMessage = "Error removing article ID: Nonexistent article " +  id;
            List<String> errors = Collections.singletonList(errorLogMessage);
            return badRequest(errors, response, errorLogMessage);
        }else {
            this._articleService.delete(id);
            return ResponseEntity.ok(new Response<>());
        }
    }

    private ResponseEntity<Response<ArticleResponse>> badRequest (List<String> errors, Response<ArticleResponse> response, String errorLog) {
        LOG.error(errorLog);
        response.getErrors().addAll(errors);
        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<Response<Page<ArticleResponse>>> listAllArticlesByCategory(Long id, Page<Article> articles) {

        Response<Page<ArticleResponse>> response = new Response<>();

        boolean isCategoryNotPresent = !this._categoryService.findById(id).isPresent();
        if(isCategoryNotPresent) {
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }

        response.setData(articles.map(this::convertArticleToArticleResponse));
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Response<Page<ArticleResponse>>> listAllArticlesBySection(Long id, Page<Article> articles) {

        Response<Page<ArticleResponse>> response = new Response<>();

        boolean isSectionNotPresent = !this._sectionService.findById(id).isPresent();
        if(isSectionNotPresent) {
            LOG.info("Error. Nonexistent section.");
            response.getErrors().add("Error. Nonexistent section.");
            return  ResponseEntity.badRequest().body(response);
        }

        response.setData(articles.map(this::convertArticleToArticleResponse));
        return ResponseEntity.ok(response);
    }

    private Article updateArticle(Article articleToBeUpdated, ArticleDto updatedArticle) {
        articleToBeUpdated.setTitle(updatedArticle.getTitle());
        articleToBeUpdated.setSubtitle(updatedArticle.getSubtitle());
        articleToBeUpdated.setUpdated_by(updatedArticle.getUpdatedBy());
        articleToBeUpdated.setContent(updatedArticle.getContent());
        articleToBeUpdated.setStatus(StatusEnum.valueOf(updatedArticle.getStatus()));
        articleToBeUpdated.setAverageLiked(updatedArticle.getAverageLiked());
        articleToBeUpdated.setGreatLiked(updatedArticle.getGreatLiked());
        articleToBeUpdated.setPoorLiked(updatedArticle.getPoorLiked());
        articleToBeUpdated.setViewers(updatedArticle.getViewers());
        articleToBeUpdated.setSlug(updatedArticle.getSlug());

        return this._articleService.persist(articleToBeUpdated);
    }

    private ArticleResponse convertArticleToArticleResponse(Article article) {
        ArticleResponse newArticleResponse = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .subtitle(article.getSubtitle())
                .content(article.getContent())
                .status(article.getStatus().toString())
                .averageLiked(article.getAverageLiked())
                .greatLiked(article.getGreatLiked())
                .poorLiked(article.getPoorLiked())
                .viewers(article.getViewers())
                .slug(article.getSlug())
                .categoriesId(article.getCategories()
                        .stream().map(Category::getId).collect(Collectors.toList()))
                .createdBy(article.getCreated_by())
                .updatedBy(article.getUpdated_by())
                .createdAt(article.getCreated_at())
                .updatedAt(article.getUpdated_at())
                .build();

        return newArticleResponse;
    }

    private Article convertArticleDtoToArticle(ArticleDto articleDto) {
        Article newArticle = new Article();
        newArticle.setTitle(articleDto.getTitle());
        newArticle.setSubtitle(articleDto.getSubtitle());
        newArticle.setContent(articleDto.getContent());
        newArticle.setStatus(StatusEnum.valueOf(articleDto.getStatus()));
        newArticle.setAverageLiked(articleDto.getAverageLiked());
        newArticle.setGreatLiked(articleDto.getGreatLiked());
        newArticle.setPoorLiked(articleDto.getPoorLiked());
        newArticle.setViewers(articleDto.getViewers());
        newArticle.setSlug(articleDto.getSlug());
        newArticle.setCreated_by(articleDto.getCreatedBy());
        newArticle.setUpdated_by(articleDto.getCreatedBy());

        return newArticle;
    }


    private void categoriesValidation(List<Long> categories, BindingResult result) {
        if (result.hasErrors()) {
            return;
        }
        if (categories.isEmpty()) {
            BindResultUtils.bindErrorMessage(result, "categories", "Categories cannot be empty");
        } else {
            categories.forEach(categoryId -> {
                boolean isCategoryNotPresent = !this._categoryService.findById(categoryId).isPresent();
                if (isCategoryNotPresent) {
                    BindResultUtils.bindErrorMessage(result, "category", "Nonexistent category id " + categoryId);
                }
            });
        }
    }

    private void articleDtoValidation(ArticleDto articleDto, BindingResult result) {
        if (result.hasErrors()) {
            return;
        }
        try {
            StatusEnum.valueOf(articleDto.getStatus());

            boolean isSectionIdPresent = articleDto.getSectionId().isPresent();
            boolean isSectionNotPresent = !this._sectionService.findById(articleDto.getSectionId().get()).isPresent();
            if (isSectionIdPresent && isSectionNotPresent) {
                Long sectionId = articleDto.getSectionId().get();
                BindResultUtils.bindErrorMessage(result, "section", "Nonexistent section id " + sectionId);
            }
        } catch (java.lang.IllegalArgumentException err) {
            BindResultUtils.bindErrorMessage(result,"status", "Invalid status type. " +
                    "Accepted values for the status field: CANCEL, PUBLISH and DRAFT(Default)");
        }
    }
}
