import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/core/category/category-service';

@Component({
  selector: 'kb-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css'],
})
export class ManageComponent implements OnInit {
  constructor(
    private categoryService: CategoryService,
  ) {}

  ngOnInit(): void {
   //this.categoryService.showById().subscribe(({ data }: any) => {});
  }
}
