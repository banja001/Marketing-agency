import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { TokenStorage } from '../jwt/token.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { VerifyTotpDto } from 'src/app/model/totp.model';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/model/user.model';

@Component({
  selector: 'app-totp',
  templateUrl: './totp.component.html',
  styleUrls: ['./totp.component.css'],
})
export class TotpComponent {
  username: string = '';
  user: User | undefined;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.username = params.get('username') || '';
    });
  }

  authForm = new FormGroup({
    code: new FormControl('', [Validators.required, Validators.minLength(6)]),
  });

  login(): void {
    if (this.authForm.valid) {
      const verifyTotp: VerifyTotpDto = {
        username: this.username,
        code: this.authForm.value.code || '',
      };
      this.authService.verifyTotp(verifyTotp).subscribe({
        next: () => {
          alert('Succesfull log in!');

          this.authService.user$.subscribe((user) => {
            this.user = user;
          });

          this.router.navigate(['']);
        },
        error: (error) => {
          alert(error);
        },
      });
    }
  }
}
