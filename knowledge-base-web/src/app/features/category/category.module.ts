import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CategoryComponent } from './category.component';

@NgModule({
  declarations: [CategoryComponent],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
  ],
})
export class CategoryModule {}
