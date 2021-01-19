import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';
import { HttpParams } from '@angular/common/http';

<<<<<<< HEAD
const URL = 'http://localhost:8080/knowledgeBase-api/categories/';
=======
const url = 'http://localhost:8080/knowledgeBase-api/categories/';
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
<<<<<<< HEAD
  constructor(private http: HttpClient) {}

  list(config: ConfigParamsModel): Observable<any> {
    const params = new HttpParams().set('pag', config.page.toString());
    return this.http.get<any>(`${URL}list`, { params });
  }

  showById(): Observable<any> {
    return this.http.get<any>(`${URL}category/` + 3);
  };
=======
  constructor(private http: HttpClient) { }

  list(config: ConfigParamsModel): Observable<any>{
    const params = new HttpParams()
      .set('pag', config.page.toString());
    
    return this.http.get<any>(`${url}list`,{params});
  }
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22
}
