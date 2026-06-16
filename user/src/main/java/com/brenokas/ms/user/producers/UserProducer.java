package com.brenokas.ms.user.producers;

import com.brenokas.ms.user.models.UserModel;
import com.brenokas.ms.user.models.dtos.EmailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value="${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel) {
        EmailDTO emailDto = new EmailDTO();

        emailDto.setUserId(userModel.getId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso");
        emailDto.setText(userModel.getName() + ", seja bem vindo(a)! \nAgradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
