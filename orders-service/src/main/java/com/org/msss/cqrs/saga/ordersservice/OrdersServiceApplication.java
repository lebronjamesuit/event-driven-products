package com.org.msss.cqrs.saga.ordersservice;

import com.org.msss.cqrs.saga.ordersservice.command.api.CreateOrderCommand;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.serializer.Serializer;

@EnableDiscoveryClient
@SpringBootApplication
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }



    @Bean
    public XStream xStream(){
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream;
    }


}
