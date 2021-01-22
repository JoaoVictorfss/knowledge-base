import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from 'src/app/core/category/category-service';
import { SectionService } from 'src/app/core/section/section-service';
import { CategoryModel } from 'src/app/shared/models/category.model';
import { SectionModel } from 'src/app/shared/models/section.model';

interface FormData {
  title: string;
  subtitle: string;
  slug: string;
}

@Component({
  selector: 'kb-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css'],
})
export class FormComponent implements OnInit {
  form!: FormGroup;
  loaded: boolean = false;
  title!: string;
  
  category!: CategoryModel;
  section!: SectionModel;

  formData: FormData = {
    title: '',
    subtitle: '',
    slug: '',
  };

  @Input()
  show: boolean = false;

  @Input()
  item!: string;

  @Input()
  id!: number;

  @Output()
  cancelFunc = new EventEmitter<null>();

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private sectionService: SectionService
  ) {}

  ngOnInit(): void {
    if (this.id) {
      this.title = 'Editar ';

      if (this.item === 'categoria') {
      } else {
        this.sectionService.showById(this.id).subscribe(({ data: section }) => {
          this.section = section;
          this.formData.title = section.title;
          this.formData.subtitle = section.subtitle;
          this.formData.slug = section.slug;
          this.loaded = true;
          this.createForm();
        });
      }
    } else {
      this.loaded = true;
      this.title = 'Adicionar ';
      this.createForm();
    }
  }

  private createForm() {
    this.form = this.fb.group({
      title: this.fb.control(this.formData.title, [
        Validators.required,
        Validators.minLength(5),
      ]),
      subtitle: this.fb.control(this.formData.subtitle, [
        Validators.required,
        Validators.minLength(5),
      ]),
      slug: this.fb.control(this.formData.slug, [
        Validators.required,
        Validators.minLength(5),
      ]),
    });
  }

  cancel() {
    this.cancelFunc.emit();
  }

  save() {}
}
