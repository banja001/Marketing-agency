import { UserModel } from '../../infrastructure/auth/model/user.model';

export interface CreateCertificate {
  subjectUsername: string;
  issuerUsername: string;
  publicKey: string;
  privateKey: string;
}

export interface Certificate {
  id: number;
  subject: UserModel;
  issuer: UserModel;
  startDate: Date;
  endDate: Date;
  type: CertType;
  publicKey: string;
  isValid: boolean;
}

export enum CertType {
  ROOT,
  INTERMEDIATE,
  END_ENTITY,
}
