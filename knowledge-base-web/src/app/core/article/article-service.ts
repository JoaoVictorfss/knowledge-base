import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

const url = 'http://localhost:8080/knowledgeBase-api/articles/';
@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  constructor(private http: HttpClient) {}

  search(config: ConfigParamsModel): Observable<any>{
      return this.http.get<any>(`${url}search/${config.search}`);
  }
}
