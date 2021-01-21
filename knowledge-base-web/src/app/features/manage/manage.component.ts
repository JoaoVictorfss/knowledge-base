import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/core/category/category-service';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { SectionModel } from 'src/app/shared/models/section.model';

@Component({
  selector: 'kb-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css'],
})
export class ManageComponent implements OnInit {
  constructor(
    private categoryService: CategoryService,
  ) {}
  item!: string;

  showForm: boolean = false;
  id: number = 0;

  sections: SectionModel[] = [
    {
      id: 1,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
    {
      id: 2,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
    {
      id: 1,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
    {
      id: 1,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
    {
      id: 1,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
    {
      id: 1,
      title: 'O titulo da seção',
      subtitle: 'Descrição da seção',
      slug: 'here we go',
      createdBy: 'joao',
      updatedBy: 'joao',
      articlesQtt: 3,
      updatedAt: new Date(),
      createdAt: new Date(),
      showMore: false,
    },
  ];

  categories: CategoryModel[] = [
    {
      id: 12,
      title: 'Categoria',
      createdBy: 'joao',
      updatedBy: 'joao',
      subtitle: 'Eu sou uma categoria para você ver o quanto eu sou importante',
      slug: 'slug',
      updatedAt: new Date(),
      createdAt: new Date(),
      articlesQtt: 23,
      sectionsQtt: 0,
    },
  ];

  ngOnInit(): void {
    //this.categoryService.showById().subscribe(({ data }: any) => {});
  }

  add(item: string) {
    this.item = item;
    this.showForm = true;
  }

  editSection(section: SectionModel) {
    this.item = 'seção';
    section.showMore = !section.showMore;
    this.id = section.id || 0;
    this.showForm = true;
  }

  cancel() {
    if (this.id) this.id = 0;
    this.showForm = false;
  }

  changeMore(section: SectionModel) {
    section.showMore = !section.showMore;
  }
}
