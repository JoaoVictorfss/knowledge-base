import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth-service';
import { AuthModel } from 'src/app/shared/models/auth.model';

@Component({
  selector: 'kb-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
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
    this.authService.login(email, password).subscribe(
      (data: AuthModel) => {
          this.router.navigate(['/management']);
      },
      (error) => {
        //TODO colocar um modal
        alert('verifique os dados');
      }
    );
  }
}
