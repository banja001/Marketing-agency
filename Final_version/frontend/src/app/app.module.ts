import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AuthModule } from './infrastructure/auth/auth.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './infrastructure/auth/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RoutingModule } from './infrastructure/routing/routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MaterialModule } from './infrastructure/material/material.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '@auth0/auth0-angular';
import { JwtInterceptor } from './infrastructure/auth/jwt/jwt.interceptor';
import { NavbarComponent } from './navbar/navbar.component';
import { JWT_OPTIONS, JwtHelperService, JwtModule } from '@auth0/angular-jwt';
import { AdminModule } from './admin/admin.module';
import { LayoutModule } from './layout/layout.module';
import {
  RECAPTCHA_SETTINGS,
  RecaptchaFormsModule,
  RecaptchaModule,
  RecaptchaSettings,
} from 'ng-recaptcha';

export function jwtOptionsFactory() {
  return {
    // Add your JWT options here if needed
    // For example, you can specify the token getter function:
    accessTokenGetter: () => {
      return localStorage.getItem('access-token');
    },
    refreshTokenGetter: () => {
      return localStorage.getItem('refresh-token');
    },
  };
}

@NgModule({
  declarations: [AppComponent, NavbarComponent],
  imports: [
    BrowserModule,
    RoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    RoutingModule,
    HttpClientModule,
    AuthModule,
    MaterialModule,
    FontAwesomeModule,
    FormsModule,
    AdminModule,
    LayoutModule,
    RecaptchaModule,
    RecaptchaFormsModule,
  ],
  providers: [
    {
      provide: RECAPTCHA_SETTINGS,
      useValue: {
        siteKey: '6LcOXPcpAAAAAB-IY67megTX-cA6lNHP8ZXEtI3A',
      } as RecaptchaSettings,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    AuthService,
    {
      provide: JWT_OPTIONS,
      useValue: JWT_OPTIONS,
    },
    JwtHelperService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
