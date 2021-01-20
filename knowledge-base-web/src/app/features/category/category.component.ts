import { Component, Input, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/core/category/category-service';
import { CategoryModel } from 'src/app/shared/models/category.model';

@Component({
  selector: 'kb-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css'],
})
export class CategoryComponent implements OnInit {
  invalidCategory: boolean = false;

  category!: CategoryModel;

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {
    //this.loadCategory();
  }

  private loadCategory() {
    this.categoryService
      .showById(2)
      .subscribe(
        ({ data }) => {
          this.category = data;
          //this.loadArticles();
        },
        (err) => {
          this.invalidCategory = true;
        }
      );
  }

  private loadArticles() {
  }
}
