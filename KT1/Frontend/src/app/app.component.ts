import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './infrastructure/auth/auth.service';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { LayoutModule } from './layout/layout.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports:[RouterOutlet, LayoutModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bezbednostKT1';
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.checkIfUserExists();
  }

  private checkIfUserExists(): void {
    this.authService.checkIfUserExists();
  }
}
