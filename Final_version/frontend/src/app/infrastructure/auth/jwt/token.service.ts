import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
  })
  export class TokenStorage {
    constructor() {}
  
    saveAccessToken(token: string): void {
      localStorage.removeItem('access_token');
      localStorage.setItem('access_token', token);
    }

    saveRefreshToken(token: string): void {
      localStorage.removeItem('refresh_token');
      localStorage.setItem('refresh_token', token);
    }
  
    getAccessToken(): string | null {
      if (typeof localStorage !== 'undefined') {
        return localStorage.getItem('access_token');
      } else {
        return null;
      }
    }

    getRefreshToken(): string | null {
      if (typeof localStorage !== 'undefined') {
        return localStorage.getItem('refresh_token');
      } else {
        return null;
      }
    }
  
    clear() {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      localStorage.removeItem('user');
    }
  }