import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthModel } from 'src/app/shared/models/auth.model';

const URL = 'http://localhost:8080/knowledge-base/auth';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

interface IUser{
  username: string;
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user!: IUser;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<AuthModel> {
    return this.http
      .post<any>(URL, { email, password }, httpOptions)
  }
}
