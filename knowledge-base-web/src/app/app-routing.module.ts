import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CategoryComponent } from './features/category/category.component';
import { HomeComponent } from './features/home/home.component';
import { ManageComponent } from './features/manage/manage.component';
import { NotFoundComponent } from './features/not-found/not-found.component';
import { LoginComponent } from './features/security/login/login.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'category/:id/:name',
    component: CategoryComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'management',
    component: ManageComponent,
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
