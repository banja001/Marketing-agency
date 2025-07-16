import { UserProfile } from './user-profile.model';

export interface Employee extends UserProfile {
  enabled: Boolean;
  hasChangedPassword: Boolean;
}
