import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageComponent } from './manage.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormComponent } from './form/form.component';
import { CategoriesComponent } from './categories/categories.component';

@NgModule({
  declarations: [ManageComponent, FormComponent, CategoriesComponent],
  imports: [
    CommonModule,
    SharedModule
  ],
})
export class ManageModule { }
