import { UserProfile } from './user-profile.model';

export interface Client extends UserProfile {
  enabled: boolean;
  companyName: string;
  tin: string;
  clientType: string;
  servicePackage: string;
}
