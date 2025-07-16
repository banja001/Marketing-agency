export interface UserProfile {
  id: number;
  username: string;
  password: string;
  name: string;
  surname: string;
  address: string;
  city: string;
  country: string;
  phone: string;
  isBlockedUser: Boolean;
}
