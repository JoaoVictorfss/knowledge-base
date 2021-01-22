import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleService } from 'src/app/core/article/article-service';
import { TokenStorageService } from 'src/app/core/auth/token-storage-service';
import { SectionService } from 'src/app/core/section/section-service';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { SectionModel } from 'src/app/shared/models/section.model';

interface ICategoryData {
  id: number;
  title: string;
  subtitle: string;
}

@Component({
  selector: 'kb-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css'],
})
export class ManageComponent implements OnInit {
  item!: string;

  config: ConfigParamsModel = {
    page: 0,
  };

  showForm: boolean = false;
  id: number = 0;

  sections: SectionModel[] = [];
  articles: ArticleModel[] = [];

  categoryData: ICategoryData = {
    id: 0,
    title: '',
    subtitle: '',
  };

  constructor(
    private articleService: ArticleService,
    private sectionService: SectionService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  add(item: string) {
    this.item = item;
    this.showForm = true;
  }

  editSection(section: SectionModel) {
    this.item = 'seção';
    section.showMore = !section.showMore;
    this.id = section.id || 0;
    this.showForm = true;
  }

  buildMain({ id, title, subtitle }: ICategoryData) {
    this.sections = [];
    this.categoryData.id = id;
    this.categoryData.title = title;
    this.categoryData.subtitle = subtitle;

    this.sectionService
      .findByCategoryId(id, this.config)
      .subscribe(({ data }: any) => {
        const { content: sections } = data;

        this.sections.push(...sections);
        this.loadArticles(id);
      });
  }

  private loadArticles(id: number) {
    this.articles = [];

    //TODO mudar o método de busca de artigos, buscar artigos privados também
    this.articleService
      .findByCategoryId(Number(id), this.config)
      .subscribe(({ data }: any) => this.articles.push(...data.content));
  }

  logout() {
    this.tokenStorageService.signOut();
    this.router.navigate(['/']);
  }

  cancel() {
    if (this.id) this.id = 0;
    this.showForm = false;
  }

  changeMore(section?: SectionModel, article?: ArticleModel) {
    if (section) section.showMore = !section.showMore;
    else if (article) article.showMore = !article.showMore;
  }

  handlePercentage(total: number, value: number): string {
    return ((value * 100) / total).toFixed(2) + '%';
  }
}
