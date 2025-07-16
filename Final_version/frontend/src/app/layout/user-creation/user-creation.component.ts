import { Component } from '@angular/core';
import { LayoutService } from '../layout.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserProfile } from 'src/app/model/user-profile.model';
import { UserCreation } from 'src/app/model/user-creation.model';

@Component({
  selector: 'app-user-creation',
  templateUrl: './user-creation.component.html',
  styleUrls: ['./user-creation.component.css'],
})
export class UserCreationComponent {
  constructor(private layoutService: LayoutService, private router: Router) {}

  userForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    surname: new FormControl('', [Validators.required]),
    username: new FormControl('', [Validators.required]),
    password: new FormControl('bkt', [Validators.required]),
    address: new FormControl('', [Validators.required]),
    city: new FormControl('', [Validators.required]),
    country: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
  });

  createUser(): void {
    const user: UserCreation = {
      name: this.userForm.value.name || '',
      surname: this.userForm.value.surname || '',
      username: this.userForm.value.username || '',
      password: 'bkt' || '',
      address: this.userForm.value.address || '',
      city: this.userForm.value.city || '',
      country: this.userForm.value.country || '',
      phone: this.userForm.value.phone || '',
      role: this.userForm.value.role || '',
    };

    if (this.userForm.valid) {
      this.layoutService.createUser(user).subscribe({
        next: () => {
          alert('Successfully created!');
          this.router.navigate(['/staff']);
        },
        error: (err) => {
          if (err.status === 403) {
            alert('Forbidden');
          } else {
            console.error('Error:', err);
          }
        },
      });
    }
  }
}
