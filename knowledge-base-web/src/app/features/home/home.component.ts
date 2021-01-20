import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { debounceTime, finalize } from 'rxjs/operators';
import { ArticleService } from 'src/app/core/article/article-service';
import { CategoryService } from 'src/app/core/category/category-service';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { PaginatorModel } from 'src/app/shared/models/paginator.model';
import { ToastModel } from 'src/app/shared/models/toast.model';

@Component({
  selector: 'kb-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  filterForm!: FormGroup;

  toastParams: ToastModel = {
    message: '',
    type:'',
  };

  config: ConfigParamsModel = {
    page: 0,
  };

  paginatorParams: PaginatorModel = {
    pageIndex: 0,
    currentPage: 0,
    totalElements: 0,
  };

  categories: CategoryModel[] = [];
  articles: ArticleModel[] = [];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  loading: boolean = false;
  error: boolean = false;

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
      ?.valueChanges.pipe(debounceTime(500))
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
      .subscribe(
        ({ data }: any) => {
          const { content: categories, totalElements } = data;

          this.categories.push(...categories);
          this.paginatorParams.totalElements = totalElements;
        },
        (error) => {
          this.toastParams.type = 'error',
          this.toastParams.message ='Ops ...  Um erro ocorreu, tente novamente mais tarde!';
          this.error = true;
        }
      );
  }

  private searchArticles(value: string) {
    if (!value) this.articles = [];
    else {
      const exists = this.articles.some(
        (article) =>
          article.title.toUpperCase().indexOf(value.toUpperCase()) > -1 ||
          article.slug.toUpperCase().indexOf(value.toUpperCase()) > -1
      );
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

    this.paginatorParams.pageIndex = pageIndex;
    this.paginatorParams.currentPage = pageIndex;
    this.config.page = pageIndex;
    this.loadCategories();
  }

  handleLink(item: string, id: any, slug: any, title?: string):string {
    if (!slug && title) {
      slug = title.replace(' ', '-').toLocaleLowerCase();
    }
    return `${item}/${id}/${slug}`;
  }
}
