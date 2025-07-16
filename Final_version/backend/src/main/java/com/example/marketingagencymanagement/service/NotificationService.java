package com.example.marketingagencymanagement.service;

import com.example.marketingagencymanagement.model.Notification;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotificationByUserId(long userId)
    {
        List<Notification> notifications=notificationRepository.findAll();
        List<Notification> notificationsForUser=new ArrayList<>();
        for (Notification n:notifications) {
            if(n.getUserId()==userId)
                notificationsForUser.add(n);
        }
        return notificationsForUser;
    }
    public Notification createNotification(Notification n)
    {
        return notificationRepository.save(n);
    }

    public void sendToAdmins(List<User> admins, String message)
    {
        for (User admin:admins) {
            sendNotification(message,admin.getUsername(),admin.getId());
        }
    }
    public void sendNotification(String message, String email, Long id)
    {
        Notification n = new Notification();
        n.setUserId(id);
        n.setMessage(message);
        n.setDate(setCurrentDate());
        createNotification(n);
        String warningMail = generateEmail(message);
        emailService.sendNotificaitionAsync(email, "Mejl upozorenja, BSEP", warningMail);

    }
    private String generateEmail(String message) {
        return String.format(
                "<p>Neko je pokusao da se uloguje sa pokresnim kredencijalima: </p>\n" +
                        "<p>"+message+"</p>\n" +
                        "<p> Vreme: "+setCurrentDate()+"</p>"+
                        "<p>Srdacan pozdrav,<br/>BSEP Tim!</p>"
                , message);
    }
    public String setCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.now().format(formatter);
    }
}
