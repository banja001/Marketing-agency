import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-passwordless-verification',
  templateUrl: './passwordless-verification.component.html',
  styleUrls: ['./passwordless-verification.component.css'],
})
export class PasswordlessVerificationComponent implements OnInit {
  token: string = '';
  error: Boolean = true;

  constructor(
    private router: Router,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.token = params['id'];
      this.token = this.token.split('=')[1];
      this.authService.verify(this.token).subscribe({
        next: (result) => {
          if (result) {
            this.error = false;
            this.loginUser(result);
          } else {
            this.error = false;
          }
          setTimeout(() => {
            this.router.navigate(['']);
          }, 2000);
        },
        error: (error) => {
          this.error = true;
          alert(error);
          setTimeout(() => {
            this.router.navigate(['']);
          }, 2000);
        },
      });
    });
  }

  loginUser(username: string) {
    this.authService.passwordlessLogin(username).subscribe({
      next: () => {},
      error: (error) => {
        console.error(error);
      },
    });
  }
}
