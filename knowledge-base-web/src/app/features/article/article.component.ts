import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from 'src/app/core/article/article-service';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { ToastModel } from 'src/app/shared/models/toast.model';

@Component({
  selector: 'kb-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css'],
})
export class ArticleComponent implements OnInit {
  article!: ArticleModel;
  articleId!: number;
  error: boolean = false;

  toastParams: ToastModel = {
    message: '',
    type: '',
  };
  
  constructor(
    private articleService: ArticleService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) =>
      this.loadArticle(Number(params['id']))
    );
  }

  private loadArticle(id: number) {
    this.articleId = id;
    this.articleService.showById(id).subscribe(
      ({ data }) => {
        this.article = data;
      },
      (err) => {
        this.error = true;
        (this.toastParams.type = 'error'),
          (this.toastParams.message =
            'Ops ...  Um erro ocorreu, tente novamente mais tarde!');
        this.error = true;
      }
    );
  }

  handleName(name: string): string {
    let formattedName: string = name
      .toUpperCase()
      .split(' ')
      .reduce((formattedName, current) => (formattedName += current[0]), '');
    if (formattedName.length > 2) formattedName = formattedName.substr(0, 2);

    return formattedName;
  }
}
