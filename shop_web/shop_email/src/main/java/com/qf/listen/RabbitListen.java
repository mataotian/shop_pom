package com.qf.listen;

import com.qf.entity.Email;
import com.qf.utils.MailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitListen {
    @Autowired
    private MailUtils mailUtils;

    @RabbitListener(queues = "email_queue")
    public void send(Email email){
        mailUtils.sendMail(email);
    }

}
