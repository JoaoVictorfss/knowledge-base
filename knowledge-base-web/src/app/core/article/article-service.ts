import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArticleModel } from 'src/app/shared/models/article.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

const URL = 'http://localhost:8080/knowledgeBase-api/articles/';

interface IResponse {
  data: ArticleModel;
  errors: string[];
}

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  constructor(private http: HttpClient) {}

  search(config: ConfigParamsModel): Observable<any> {
    return this.http.get<any>(`${URL}search/${config.search}`);
  }

  findByCategoryId(id: number, config: ConfigParamsModel): Observable<any> {
    const params = new HttpParams().set('pag', config.page.toString());
    return this.http.get<any>(`${URL}list-by-category/${id}`, { params });
  }

  showById(id: number): Observable<IResponse> {
    return this.http.get<IResponse>(`${URL}article/` + id);
  }
}
