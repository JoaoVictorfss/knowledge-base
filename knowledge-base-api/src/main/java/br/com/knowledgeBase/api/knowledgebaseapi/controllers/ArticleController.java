package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import static br.com.knowledgeBase.api.knowledgebaseapi.data.constants.PathConstants.*;
import br.com.knowledgeBase.api.knowledgebaseapi.data.dtos.ArticleDto;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Article;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.data.enums.StatusEnum;
import br.com.knowledgeBase.api.knowledgebaseapi.data.response.Response;
import br.com.knowledgeBase.api.knowledgebaseapi.services.ArticleService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ARTICLE_PATH)
@CrossOrigin(origins = "*")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CategoryService categoryService;

    @Value("${pagination.qtt_per_page}")
    private int qttPerPage;

    @GetMapping(value = LIST_BY_CATEGORY)
    public ResponseEntity<Response<Page<ArticleDto>>> listAllPublishedArticlesByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by category {}, page: {}", categoryId, pag);
        return this.listAllArticlesByCategory(categoryId, PageRequest.of(pag, 10, Sort.Direction.valueOf(dir), ord), false);
    }

    @GetMapping(value = LIST_BY_SECTION)
    public ResponseEntity<Response<Page<ArticleDto>>> listAllPublishedArticlesBySectionId(
            @PathVariable("sectionId") Long sectionId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        LOG.info("Searching articles by section {}, page: {}", sectionId, pag);
        return this.listAllArticlesBySection(sectionId, PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord), false);
    }

    @GetMapping(value = SEARCH)
    public ResponseEntity<Response<Page<ArticleDto>>> search(@PathVariable("param") String param) {

        LOG.info("Searching articles by {}", param);
        Response<Page<ArticleDto>> response = new Response<Page<ArticleDto>>();
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.Direction.ASC, "title");

        Page<Article> articles = this.articleService.findAllByParam(param, pageRequest);
        Page<ArticleDto> articlesDto = articles.map(this::convertArticleToArticleDto);

        response.setData(articlesDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = PRIVATE_LIST_BY_CATEGORY)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<ArticleDto>>> listAllArticlesByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by category {}, page: {}", categoryId, pag);
        return this.listAllArticlesByCategory(categoryId, PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord), true);
    }


    @GetMapping(value = PRIVATE_LIST_BY_SECTION )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<ArticleDto>>> listAllArticlesBySectionId(
            @PathVariable("sectionId") Long sectionId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "title") String ord,
            @RequestParam(value = "dir", defaultValue = "ASC") String dir) {

        LOG.info("Searching articles by section {}, page: {}", sectionId, pag);
        return this.listAllArticlesBySection(sectionId, PageRequest.of(pag, this.qttPerPage, Sort.Direction.valueOf(dir), ord), true);
    }


    @GetMapping(value = FIND_BY_ARTICLE_ID)
    public ResponseEntity<Response<ArticleDto>> showById(@PathVariable("id") Long id) {

        LOG.info("Searching article id {}", id);
        Response<ArticleDto> response = new Response<ArticleDto>();

        Optional<Article>articleExists = this.articleService.findById(id);
        if(!articleExists.isPresent()) {
            LOG.info("Error. Nonexistent article.");
            response.getErrors().add("Error. Nonexistent article.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertArticleToArticleDto(articleExists.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<ArticleDto>> store(@Valid @RequestBody ArticleDto articleDto,
                                                      BindingResult result) throws ParseException {

        LOG.info("Adding article: {}", articleDto.toString());
        Response<ArticleDto> response = new Response<ArticleDto>();

        articleDtoValidation(articleDto, result);
        categoriesValidation(articleDto.getCategoriesId(),result);
        if(result.hasErrors()){
           LOG.error("Error validating article: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

           return ResponseEntity.badRequest().body(response);
        }

        Article article = this.convertArticleDtoToArticle(articleDto);
        articleDto.getCategoriesId().forEach(id -> {
           Category category = this.categoryService.findById(id).get();
           article.getCategories().add(category);
           category.getArticles().add(article);
        });

        articleDto.getSectionId().ifPresent( id -> {
            Section s = this.sectionService.findById(id).get();
            s.getArticles().add(article);
            article.getSections().add(s);
        });

        this.articleService.persist(article);
        response.setData(this.convertArticleToArticleDto(article));

        return ResponseEntity.ok(response);
    }

    @PutMapping(UPDATE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<ArticleDto>> update(@Valid @RequestBody ArticleDto articleDto,
                                                       BindingResult result, @PathVariable("id") Long id) throws NoSuchAlgorithmException {

        LOG.info("Updating article id {}, article: {}",id, articleDto.toString());
        Response<ArticleDto> response = new Response<ArticleDto>();

        Optional<Article>articleExists = this.articleService.findById(id);
        if(!articleExists.isPresent()){
            result.addError(new ObjectError("article", "Nonexistent article."));
        }
        articleDtoValidation(articleDto, result);
        if(result.hasErrors()){
            LOG.error("Error validating article: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Article articleOpt = articleExists.get();
        articleOpt.setTitle(articleDto.getTitle());
        articleOpt.setSubtitle(articleDto.getSubtitle());
        articleOpt.setUpdated_by(articleDto.getUpdatedBy());
        articleOpt.setContent(articleDto.getContent());
        articleOpt.setStatus(StatusEnum.valueOf(articleDto.getStatus()));
        articleOpt.setAverageLiked(articleDto.getAverageLiked());
        articleOpt.setGreatLiked(articleDto.getGreatLiked());
        articleOpt.setPoorLiked(articleDto.getPoorLiked());
        articleOpt.setViewers(articleDto.getViewers());
        articleOpt.setSlug(articleDto.getSlug());

        this.articleService.persist(articleOpt);
        response.setData(this.convertArticleToArticleDto(articleOpt));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = DELETE)
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

    private ResponseEntity<Response<Page<ArticleDto>>> listAllArticlesByCategory(Long id, PageRequest pageRequest, boolean isPrivate) {

        Response<Page<ArticleDto>> response = new Response<Page<ArticleDto>>();

        Optional<Category> category = this.categoryService.findById(id);
        if(!category.isPresent()){
            LOG.info("Error. Nonexistent category.");
            response.getErrors().add("Error. Nonexistent category.");
            return  ResponseEntity.badRequest().body(response);
        }

        Page<Article> articles;
        if(isPrivate){
            articles = this.articleService.findAllByCategoryId(id, pageRequest);
        }else {
            articles = this.articleService.findAllPublishedByCategoryId(id, pageRequest);
        }

        response.setData(articles.map(this::convertArticleToArticleDto));
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Response<Page<ArticleDto>>> listAllArticlesBySection(Long id, PageRequest pageRequest, boolean isPrivate) {

        Response<Page<ArticleDto>> response = new Response<Page<ArticleDto>>();

        Optional<Section> section = this.sectionService.findById(id);
        if(!section.isPresent()){
            LOG.info("Error. Nonexistent section.");
            response.getErrors().add("Error. Nonexistent section.");
            return  ResponseEntity.badRequest().body(response);
        }

        Page<Article> articles;
        if(isPrivate){
            articles = this.articleService.findAllBySectionId(id, pageRequest);
        }else{
            articles = this.articleService.findAllPublishedBySectionId(id, pageRequest);
        }

        response.setData(articles.map(this::convertArticleToArticleDto));
        return ResponseEntity.ok(response);
    }

    private ArticleDto convertArticleToArticleDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setSubtitle(article.getSubtitle());
        articleDto.setContent(article.getContent());
        articleDto.setStatus(article.getStatus().toString());
        articleDto.setAverageLiked(article.getAverageLiked());
        articleDto.setGreatLiked(article.getGreatLiked());
        articleDto.setPoorLiked(article.getPoorLiked());
        articleDto.setViewers(article.getViewers());
        articleDto.setSlug(article.getSlug());
        articleDto.setCreatedBy(article.getCreated_by());
        articleDto.setUpdatedBy(article.getUpdated_by());
        articleDto.setCreatedAt(article.getCreated_at());
        articleDto.setUpdatedAt(article.getUpdated_at());

        return  articleDto;
    }

    private Article convertArticleDtoToArticle(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setSubtitle(articleDto.getSubtitle());
        article.setContent(articleDto.getContent());
        article.setStatus(StatusEnum.valueOf(articleDto.getStatus()));
        article.setAverageLiked(articleDto.getAverageLiked());
        article.setGreatLiked(articleDto.getGreatLiked());
        article.setPoorLiked(articleDto.getPoorLiked());
        article.setViewers(articleDto.getViewers());
        article.setSlug(articleDto.getSlug());
        article.setCreated_by(articleDto.getCreatedBy());
        article.setUpdated_by(articleDto.getCreatedBy());

        return article;
    }

    private void categoriesValidation(List<Long> categories, BindingResult result) {
        if (result.hasErrors()) return;
        if (categories.isEmpty()){
            result.addError(new ObjectError("categories", "Categories cannot be empty"));
        } else{
            categories.forEach(categoryId -> {
                Optional<Category> category = this.categoryService.findById(categoryId);
                if (!category.isPresent()) {
                    result.addError(new ObjectError("category", "Nonexistent category id " + categoryId));
                }
            });
        }
    }

    private void articleDtoValidation(ArticleDto articleDto, BindingResult result){
        if (result.hasErrors()) return;
        try {
            StatusEnum.valueOf(articleDto.getStatus());

            if (articleDto.getSectionId().isPresent() && !this.sectionService.findById(articleDto.getSectionId().get()).isPresent()){
                result.addError(new ObjectError("section", "Nonexistent section id " + articleDto.getSectionId().get()));
            }
        } catch (java.lang.IllegalArgumentException err) {
            result.addError(new ObjectError("status", "Invalid status type. " +
                    "Accepted values for the status field: CANCEL, PUBLISH and DRAFT(Default)"));
        }
    }
}
