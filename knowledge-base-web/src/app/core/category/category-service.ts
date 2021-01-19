import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { HttpParams } from '@angular/common/http';

const URL = 'http://localhost:8080/knowledgeBase-api/categories/';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  list(config: ConfigParamsModel): Observable<any> {
    const params = new HttpParams().set('pag', config.page.toString());
    return this.http.get<any>(`${URL}list`, { params });
  }

  showById(): Observable<any> {
    return this.http.get<any>(`${URL}category/` + 3);
  };
}
