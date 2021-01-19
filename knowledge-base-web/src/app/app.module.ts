import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeModule } from './features/home/home.module';
<<<<<<< HEAD
import { LoginModule } from './features/security/login/login.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ManageModule } from './features/manage/manage.module';
=======
import { LoginModule } from './features/login/login.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HomeModule,
    LoginModule,
<<<<<<< HEAD
    ManageModule,
=======
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
