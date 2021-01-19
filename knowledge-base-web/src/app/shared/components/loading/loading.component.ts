import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'kb-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css'],
})
export class LoadingComponent implements OnInit {
  @Input()
  showLoading: boolean = false;

  constructor() { }
  
  ngOnInit(): void {
  }
}
