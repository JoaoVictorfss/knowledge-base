import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticleComponent } from './article.component';
import { SharedModule } from 'src/app/shared/shared.module';


@NgModule({
  declarations: [ArticleComponent],
  imports: [
    CommonModule,
    SharedModule
  ]
})
export class ArticleModule { }
