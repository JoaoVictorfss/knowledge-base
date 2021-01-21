import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'kb-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css'],
})
export class FormComponent implements OnInit {
  form!: FormGroup;
  title: string = 'Adicionar ';
  formData = {
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

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    if (this.id) {
      this.title = 'Editar ';
      if (this.item === 'categoria') {
        /**
         * TODO buscar categoria pelo id e adicionar os dados no formData
         */
      } else {
        /**
         * TODO buscar seção pelo id e adicionar os dados no formData
         */
      }
    } else this.title = 'Adicionar ';
    this.createForm();
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

  save() {
    if (this.item === 'categoria') {
      /**
       * TODO  atualizar categoria com os dados do form e salvar
       */
    } else {
      /**
       * TODO  atualizar seção com os dados do form e salvar
       */
    }
  }
}
