package org.msss.cqrs.saga.productservice.business.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.msss.cqrs.saga.productservice.business.command.interceptor.CreateProductCommandInterceptor;
import org.msss.cqrs.saga.productservice.business.exception.CommonEventException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Autowired
    public void registerInterceptorToCommandBus(ApplicationContext context, CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }


    @Autowired
    public void registerListenEventHandler(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler
                ("product-group",
                        s -> {
                            return new CommonEventException();
                        });

    }

}

