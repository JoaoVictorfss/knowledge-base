import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/core/category/category-service';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { ToastModel } from 'src/app/shared/models/toast.model';

interface ICategoryData {
  id: number;
  title: string;
  subtitle: string;
}

@Component({
  selector: 'kb-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css'],
})
export class CategoriesComponent implements OnInit {
  config: ConfigParamsModel = {
    page: 0,
  };

  categories: CategoryModel[] = [];

  @Output()
  addCategoryIdFunc = new EventEmitter<ICategoryData>();

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  private loadCategories(): void {
    this.categoryService.list(this.config).subscribe(
      ({ data }: any) => {
        const { content: categories} = data;

        this.categories.push(...categories);
        this.addCategoryId(this.categories[0]);
      });
  }

  addCategoryId(selectedCategory: CategoryModel) {
    this.categories = this.categories.map((category) => {
      if (category.id !== selectedCategory.id) category.selected = false;
      else category.selected = true;
      return category;
    });

    const categoryEmit: ICategoryData = {
      id: Number(selectedCategory.id),
      title: selectedCategory.title,
      subtitle: selectedCategory.subtitle,
    };

    this.addCategoryIdFunc.emit(categoryEmit);
  }
}
