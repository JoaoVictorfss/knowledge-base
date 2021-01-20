import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'kb-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
})
export class ToastComponent implements OnInit {
  @Input()
  message!: string;

  @Input()
  type!: string;

  @Input()
  show: boolean = false;
  
  title!: string;

  constructor() {}

  ngOnInit(): void {

    switch (this.type) {
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
