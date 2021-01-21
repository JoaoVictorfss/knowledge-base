import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageComponent } from './manage.component';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [ManageComponent],
  imports: [
    CommonModule,
    SharedModule
  ],
})
export class ManageModule { }
