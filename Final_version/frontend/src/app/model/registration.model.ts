export interface Registration {
  id: number;
  username: string;
  password: string;
  name: string;
  surname: string;
  address: string;
  city: string;
  country: string;
  phone: string;
  enabled: boolean;
  clientType: ClientType;
  companyName: string;
  tin: string;
  servicePackage: ServicePackageType;
  using2FA: boolean;
}

export enum ClientType {
  PHYSICAL_PERSON = 'PHYSICAL_PERSON',
  LEGAL_ENTITY = 'LEGAL_ENTITY',
}

export enum ServicePackageType {
  BASIC = 'BASIC',
  STANDARD = 'STANDARD',
  GOLD = 'GOLD',
}
