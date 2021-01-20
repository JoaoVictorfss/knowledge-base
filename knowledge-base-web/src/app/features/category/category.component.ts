import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ArticleService } from 'src/app/core/article/article-service';
import { CategoryService } from 'src/app/core/category/category-service';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

@Component({
  selector: 'kb-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css'],
})
export class CategoryComponent implements OnInit {
  config: ConfigParamsModel = {
    page: 0,
  };

  articles: ArticleModel[] = [];
  category!: CategoryModel;

  error: boolean = false;
  categoryId!: number;

  loading: boolean = false;

  pageIndex: number = 0;
  currentPage: number = 0;
  totalElements: number = 0;

  constructor(
    private categoryService: CategoryService,
    private articleService: ArticleService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) =>
      this.loadCategory(Number(params['id']))
    );
  }

  private loadCategory(id: number) {
    this.categoryId = id;
    this.loading = true;
    
    this.categoryService
      .showById(id)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe(
        ({ data }) => {
          this.category = data;
          this.loadArticles();
        },
        (err) => {
          this.error = true;
        }
      );
  }

  private loadArticles() {
    this.articles = [];
    this.articleService
      .findByCategoryId(this.categoryId, this.config)
      .subscribe(({ data }) => {
        const { content:articles, totalElements } = data;
        this.articles.push(...articles);
        this.totalElements = totalElements;
      },
        (error) => {
          this.error = true;
        }
      );
  }

  handlePageEvent(event: PageEvent) {
    const { pageIndex } = event;
    this.pageIndex = pageIndex;
    this.currentPage = pageIndex;
    this.config.page = pageIndex;
    this.loadArticles();
  }
}
