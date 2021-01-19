import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
<<<<<<< HEAD
import { ManageComponent } from './features/manage/manage.component';
import { LoginComponent } from './features/security/login/login.component';
=======
import { LoginComponent } from './features/login/login.component';
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22

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
    path: 'login',
    component: LoginComponent,
  },
  {
<<<<<<< HEAD
    path: 'management',
    component: ManageComponent,
  },
  {
=======
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22
    path: '**',
    component: HomeComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
