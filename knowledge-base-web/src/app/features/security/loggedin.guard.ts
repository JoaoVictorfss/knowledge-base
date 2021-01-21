import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth-service';
import { TokenStorageService } from 'src/app/core/auth/token-storage-service';

@Injectable()
export class LoggedInGuard implements CanActivate {
  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {}

  canActivate() {
    let isLoggedIn = false;

    if (!this.tokenStorage.getToken()) 
        this.router.navigate(['/login']);
    else isLoggedIn = true;

    return isLoggedIn;
  }
}
