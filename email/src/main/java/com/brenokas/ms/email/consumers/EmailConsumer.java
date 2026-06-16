package com.brenokas.ms.email.consumers;

import com.brenokas.ms.email.models.EmailModel;
import com.brenokas.ms.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.brenokas.ms.email.model.dto.EmailRequestDTO;

@Component
public class EmailConsumer {

  final EmailService emailService;

  public EmailConsumer(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = "${broker.queue.email.name}")
  public void listenEmailQueue(@Payload EmailRequestDTO email) {
    EmailModel emailModel = new EmailModel();
    BeanUtils.copyProperties(email, emailModel);

    emailService.sendEmail(emailModel);
  }
}
