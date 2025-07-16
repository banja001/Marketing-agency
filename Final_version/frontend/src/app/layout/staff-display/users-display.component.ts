import { Component } from '@angular/core';
import { UserProfile } from 'src/app/model/user-profile.model';
import { LayoutService } from '../layout.service';
import { forkJoin, zip } from 'rxjs';
import { AdminService } from 'src/app/admin/admin.service';

@Component({
  selector: 'app-users-display',
  templateUrl: './users-display.component.html',
  styleUrls: ['./users-display.component.css'],
})
export class UsersDisplayComponent {
  clients: UserProfile[] = [];
  employees: UserProfile[] = [];
  clientsAndEmployees: UserProfile[] = [];

  constructor(
    private layoutService: LayoutService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.getEmployees().subscribe(
      (employees: UserProfile[]) => {
        this.employees = employees;

        this.getClients().subscribe(
          (clients: UserProfile[]) => {
            this.clients = clients;

            this.clientsAndEmployees = this.clients.concat(this.employees);
            console.log('clientsAndEmployees', this.clientsAndEmployees);
          },
          (error) => {
            console.error('Error fetching clients:', error);
          }
        );
      },
      (error) => {
        console.error('Error fetching employees:', error);
      }
    );
  }

  getEmployees() {
    return this.layoutService.getAllUsers('employee');
  }

  getClients() {
    return this.layoutService.getAllUsers('client');
  }

  isClient(user: UserProfile): boolean {
    return this.clients.some((client) => client.id === user.id);
  }

  block(user: UserProfile): void {
    this.adminService.blockUser(user).subscribe({
      next: (result: Boolean) => {
        user.isBlockedUser = result;
        console.log('Is blocked', result);
      },
      error: (error) => {
        console.error('Error blocking user:', error);
      },
    });
  }
}
