package com.example.productservice.business.command.interceptor;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorRegister {

    @Autowired
    public void registerInterceptorToCommandBus(ApplicationContext  context, CommandBus commandBus){
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }
}
