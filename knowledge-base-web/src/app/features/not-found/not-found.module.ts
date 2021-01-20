import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotFoundComponent } from './not-found.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [NotFoundComponent],
  imports: [CommonModule, MatIconModule],
})
export class NotFoundModule {}
