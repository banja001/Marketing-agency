export interface User {
  id: number;
  username: string;
  role: string;
}

export interface UserModel {
  id: number;
  name: string;
  lastname: string;
  email: string;
  username: string;
  phoneNumber: string;
  password: string;
  role: Role;
  organization: string;
  country: string;
}

export enum Role {
  ADMIN,
  INTERMEDIARY_CA,
  END_ENTITY,
}
