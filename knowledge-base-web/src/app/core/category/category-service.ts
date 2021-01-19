import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { HttpParams } from '@angular/common/http';

const url = 'http://localhost:8080/knowledgeBase-api/categories/';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) { }

  list(config: ConfigParamsModel): Observable<any>{
    const params = new HttpParams()
      .set('pag', config.page.toString());
    
    return this.http.get<any>(`${url}list`,{params});
  }
}
