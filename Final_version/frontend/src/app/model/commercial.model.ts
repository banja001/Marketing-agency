import { Client } from './client.model';
import { Employee } from './employee.model';

export interface Commercial {
  employee: Employee;
  client: Client;
  moto: string;
  duration: string;
  description: string;
}
