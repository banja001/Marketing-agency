export interface Login {
  username: string;
  password: string;
  recaptchaToken: string;
}

export interface Passwordless {
  email: string;
  recaptchaToken: string;
}
