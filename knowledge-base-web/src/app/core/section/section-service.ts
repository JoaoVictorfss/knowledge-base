import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SectionModel } from 'src/app/shared/models/section.model';
import { ConfigParamsModel } from 'src/app/shared/models/config-params.model';

const URL = 'http://localhost:8080/knowledgeBase-api/sections/';

interface IResponse {
  data: SectionModel;
  errors: string[];
}

@Injectable({
  providedIn: 'root',
})
export class SectionService {
  constructor(private http: HttpClient) {}

  findByCategoryId(id: number, config: ConfigParamsModel): Observable<any> {
    const params = new HttpParams().set('pag', config.page.toString());
    return this.http.get<any>(`${URL}list/${id}`, { params });
  }

  showById(id: number): Observable<IResponse> {
    return this.http.get<IResponse>(`${URL}section/` + id);
  }
}
