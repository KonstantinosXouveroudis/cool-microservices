package com.ticktac;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.ticktac.domain.Message;

@SpringBootApplication
public class MessengerServiceApplication {
	
	@Bean
	public JmsListenerContainerFactory<?> ticktacCF(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory cf = new DefaultJmsListenerContainerFactory();
		configurer.configure(cf, connectionFactory);
		
		return cf;
	}
	
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		
		return converter;
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MessengerServiceApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("Sending a message..");
		jmsTemplate.convertAndSend("ticktacQueue", new Message(1, 2, "Administrator", "HEYYY!"));
	}
}
