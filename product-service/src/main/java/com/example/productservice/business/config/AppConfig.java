package com.example.productservice.business.config;

import com.example.productservice.business.command.interceptor.CreateProductCommandInterceptor;
import com.example.productservice.business.exception.CommonEventException;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class AppConfig {

    @Autowired
    public void registerInterceptorToCommandBus(ApplicationContext context, CommandBus commandBus){
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }


    @Autowired
    public void registerListenEventHandler(EventProcessingConfigurer eventProcessingConfigurer){
            eventProcessingConfigurer.registerListenerInvocationErrorHandler
                    ("product-group",
                            s -> {
                        return new CommonEventException();
                    } );

    }




}

