export interface ChangePassword {
  username: string;
  oldPassword: string;
  newPassword: string;
  repeatedPassword: string;
}

export interface RecoverAccount {
  newPassword: string;
  token: string;
}
