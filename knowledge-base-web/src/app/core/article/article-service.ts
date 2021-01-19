import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

<<<<<<< HEAD
const URL = 'http://localhost:8080/knowledgeBase-api/articles/';
=======
const url = 'http://localhost:8080/knowledgeBase-api/articles/';
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22
@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  constructor(private http: HttpClient) {}

  search(config: ConfigParamsModel): Observable<any>{
<<<<<<< HEAD
      return this.http.get<any>(`${URL}search/${config.search}`);
=======
      return this.http.get<any>(`${url}search/${config.search}`);
>>>>>>> 75c1cc5538fc31b5702ccd5c58c2f7058ebe8d22
  }
}
