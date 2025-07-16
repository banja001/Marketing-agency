import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AuthModule } from './infrastructure/auth/auth.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './infrastructure/auth/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RoutingModule } from './infrastructure/routing/routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MaterialModule } from './infrastructure/material/material.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { JwtInterceptor } from './infrastructure/auth/jwt/jwt.interceptor';
import { AuthService } from '@auth0/auth0-angular';
import { provideHttpClient } from '@angular/common/http';
import { withFetch } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LayoutModule } from './layout/layout.module';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [LoginComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RoutingModule,
    FormsModule,
    FontAwesomeModule,
    AuthModule,
    MaterialModule,
    LayoutModule,
    MatInputModule,
    MatFormFieldModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    AuthService,
    {
      provide: provideHttpClient,
      useFactory: withFetch,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
