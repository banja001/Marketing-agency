import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';
import { MatDialog } from '@angular/material/dialog';
import { Notification } from 'src/app/model/notification';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  notifications: Notification[]=[];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.adminService.getNotifications(-1).subscribe(
      notifications => this.notifications = notifications,
      error => console.error('Error fetching notifications:', error)
    );
  }
}