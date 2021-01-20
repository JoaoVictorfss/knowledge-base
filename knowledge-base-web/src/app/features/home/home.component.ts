import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { debounceTime, finalize } from 'rxjs/operators';
import { ArticleService } from 'src/app/core/article/article-service';
import { CategoryService } from 'src/app/core/category/category-service';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

@Component({
  selector: 'kb-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  filterForm!: FormGroup;

  config: ConfigParamsModel = {
    page: 0,
  };

  categories: CategoryModel[] = [];
  articles: ArticleModel[] = [];

  loading: boolean = false;
  pageIndex: number = 0;
  currentPage: number = 0;
  totalElements: number = 0;

  error: boolean = false;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  constructor(
    private categoryService: CategoryService,
    private articleService: ArticleService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.filterForm = this.fb.group({
      filterValue: [''],
    });

    this.filterForm
      .get('filterValue')
      ?.valueChanges.pipe(debounceTime(600))
      .subscribe((val: string) => {
        this.searchArticles(val);
      });

    this.loadCategories();
  }

  private loadCategories(): void {
    this.loading = true;
    this.categories = [];

    this.categoryService
      .list(this.config)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe(({ data }: any) => {
          const { content: categories, totalElements } = data;
      
          this.categories.push(...categories);
          this.totalElements = totalElements;
      }, (error) => {
          this.error = true;
      });
  }

  private searchArticles(value: string) {
    if (!value) this.articles = [];
    else {
      const exists = this.articles.some((article) => article.title.indexOf(value) > -1 || article.slug.indexOf(value) > -1); 
      if (!exists) {
        this.articles = [];
        this.config.search = value;
       
        this.articleService.search(this.config).subscribe(({ data }: any) => {
          const { content } = data;
          this.articles.push(...content);
        });
      }
    }
  }

  handlePageEvent(event: PageEvent) {
    const { pageIndex } = event;
    this.pageIndex = pageIndex;
    this.currentPage = pageIndex;
    this.config.page = pageIndex;
    this.loadCategories();
  }

  handleLink(item: string, id: any, slug: any, title?: string) {
    if (!slug && title) {
      slug = title.replace(' ', '-').toLocaleLowerCase();
    }
    return `${item}/${id}/${slug}`;
  }
}
