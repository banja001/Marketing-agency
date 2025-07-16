import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-account-activation',
  templateUrl: './account-activation.component.html',
  styleUrls: ['./account-activation.component.css'],
})
export class AccountActivationComponent implements OnInit {
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
      console.log(this.token)
      this.authService.activateAccount(this.token).subscribe({
        next: (result) => {
          if (result) {
            this.error = false;
            console.log(result)
          } else {
            this.error = false;
          }
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 4000);
        },
        error: (error) => {
          this.error = true;
          alert(error);
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 4000);
        },
      });
    });
  }
  }
