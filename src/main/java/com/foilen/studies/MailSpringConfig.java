package com.foilen.studies;

import com.foilen.smalltools.email.EmailService;
import com.foilen.smalltools.email.EmailServiceSpring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailSpringConfig {

    /**
     * The SMTP server is configured with the standard "spring.mail.*" properties.
     */
    @Bean
    public EmailService emailService() {
        return new EmailServiceSpring();
    }

}
