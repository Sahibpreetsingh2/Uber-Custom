package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(List<Driver> drivers) {

        for (Driver driver : drivers) {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("sahibpreetsingh432@gmail.com");
            simpleMailMessage.setTo(driver.getUsers().getEmail());
            simpleMailMessage.setSubject("Uber Related Rides :");
            simpleMailMessage.setText("Accept the ride .....");
            simpleMailMessage.setSentDate(new Date());

            javaMailSender.send(simpleMailMessage); // send email :

        }
    }
}
