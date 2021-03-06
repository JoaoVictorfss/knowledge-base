import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth-service';
import { TokenStorageService } from 'src/app/core/auth/token-storage-service';
import { AuthModel } from 'src/app/shared/models/auth.model';
import { ToastModel } from 'src/app/shared/models/toast.model';

@Component({
  selector: 'kb-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  error: boolean = false;

  toastParams: ToastModel = {
    message: '',
    type: '',
  };

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    this.createForm();
  }

  private createForm() {
    this.loginForm = this.fb.group({
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(6),
      ]),
    });
  }

  login(email: string, password: string) {
    this.error = false;
    this.authService.login(email, password).subscribe(
      (authData: AuthModel) => {
        const { data } = authData;

        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveUser(data.username);
        this.router.navigate(['/management']);
      },
      (error) => {
        (this.toastParams.type = 'error'),
          (this.toastParams.message = 'Usuário ou senha incorreto.');
        this.error = true;
      }
    );
  }
}
