import { Injectable } from '@angular/core';
import { ACCESS_TOKEN , USER } from '../../../shared/constants';

@Injectable({
    providedIn: 'root',
  })
  export class TokenStorage {
    constructor() {}
  
    saveAccessToken(token: string): void {
      localStorage.removeItem(ACCESS_TOKEN);
      localStorage.setItem(ACCESS_TOKEN, token);
    }
  
    getAccessToken(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem(ACCESS_TOKEN);
    } else {
      return null;
    }
  }
    clear() {
      localStorage.removeItem(ACCESS_TOKEN);
      localStorage.removeItem(USER);
    }
  }