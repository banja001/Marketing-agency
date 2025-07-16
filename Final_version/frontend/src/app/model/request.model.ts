import { Client } from './client.model';

export interface Request {
  id: string;
  client: Client;
  deadline: string;
  activeFrom: string;
  activeTo: string;
  description: string;
}
