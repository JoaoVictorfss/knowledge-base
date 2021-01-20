import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeModule } from './features/home/home.module';
import { LoginModule } from './features/security/login/login.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ManageModule } from './features/manage/manage.module';
import { NotFoundModule } from './features/not-found/not-found.module';
import { CategoryModule } from './features/category/category.module';
import { ArticleModule } from './features/article/article.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HomeModule,
    LoginModule,
    ManageModule,
    BrowserAnimationsModule,
    NotFoundModule,
    CategoryModule,
    ArticleModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
