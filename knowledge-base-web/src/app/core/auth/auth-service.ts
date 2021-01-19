import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthModel } from 'src/app/shared/models/auth.model';

const URL = 'http://localhost:8080/knowledge-base/auth';

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

  isLoggedIn(): boolean {
    return this.user != undefined;
  }

  login(email: string, password: string): Observable<AuthModel> {
    return this.http
      .post<any>(URL, { email, password })
      .pipe(tap((user) => {
        this.user = user.data;
      }));
  }
}
