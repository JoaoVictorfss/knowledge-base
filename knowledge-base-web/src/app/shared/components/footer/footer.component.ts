import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'kb-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  @Input()
  author!: string;
  
  constructor() { }

  ngOnInit(): void {
  }

}
