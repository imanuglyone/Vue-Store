package com.vuestore.service;

import com.vuestore.dto.support.ContactRequest;
import com.vuestore.entity.ContactMessage;
import com.vuestore.repository.ContactMessageRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  private final ContactMessageRepository contactMessageRepository;
  private final JavaMailSender mailSender;
  private final String supportEmailTo;
  private final String mailFrom;
  private final String mailHost;
  private final Logger logger = LoggerFactory.getLogger(ContactService.class);

  public ContactService(ContactMessageRepository contactMessageRepository,
                        JavaMailSender mailSender,
                        @Value("${support.email.to:}") String supportEmailTo,
                        @Value("${spring.mail.username:}") String mailFrom,
                        @Value("${spring.mail.host:}") String mailHost) {
    this.contactMessageRepository = contactMessageRepository;
    this.mailSender = mailSender;
    this.supportEmailTo = supportEmailTo;
    this.mailFrom = mailFrom;
    this.mailHost = mailHost;
  }

  public void save(ContactRequest request) {
    ContactMessage message = new ContactMessage();
    message.setName(request.name());
    message.setEmail(request.email());
    message.setMessage(request.message());
    message.setCreatedAt(LocalDateTime.now());
    contactMessageRepository.save(message);
    sendEmail(request);
  }

  private void sendEmail(ContactRequest request) {
    if (supportEmailTo == null || supportEmailTo.isBlank() || mailHost == null || mailHost.isBlank()) {
      return;
    }

    try {
      SimpleMailMessage mail = new SimpleMailMessage();
      if (mailFrom != null && !mailFrom.isBlank()) {
        mail.setFrom(mailFrom);
      }
      mail.setTo(supportEmailTo);
      mail.setSubject("Новое обращение в поддержку");
      mail.setText(
        "Имя: " + request.name() + "\n" +
        "Email: " + request.email() + "\n\n" +
        request.message()
      );
      mailSender.send(mail);
    } catch (Exception ex) {
      logger.warn("Failed to send support email: {}", ex.getMessage());
    }
  }
}
