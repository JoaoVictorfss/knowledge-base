import { Component, Input, OnInit } from '@angular/core';
import { ToastModel } from '../../models/toast.model';

@Component({
  selector: 'kb-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
})
export class ToastComponent implements OnInit {
  @Input()
  toastParams!: ToastModel;

  @Input()
  show: boolean = false;
  
  title!: string;

  constructor() {}

  ngOnInit(): void {

    switch (this.toastParams.type) {
      case 'info':
        this.title = 'Info';
        break;
      case 'error':
        this.title = 'Error';
        break;
      case 'success':
        this.title = 'Success!';
        break;
      default:
        break;
    }

    this.closeToast();
  }

  closeToast() {
    setTimeout(() => {
      this.show = false;
    }, 3500);
  }

}
