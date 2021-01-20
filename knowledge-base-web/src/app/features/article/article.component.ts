import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from 'src/app/core/article/article-service';
import { ArticleModel } from 'src/app/shared/models/article.model';

@Component({
  selector: 'kb-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css'],
})
export class ArticleComponent implements OnInit {
  article!: ArticleModel;
  articleId!: number;
  error: boolean = false;

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

    this.articleService
      .showById(id)
      .subscribe(
        ({ data }) => {
          this.article = data;
        },
        (err) => {
          this.error = true;
        }
      );
  }

  handleName(name: string):string {
    return name.toUpperCase().split(' ').reduce((formattedName, current) =>  formattedName += current[0], "");
  }

}
