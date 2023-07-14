package org.msss.cqrs.saga.apigatewayv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayV2Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayV2Application.class, args);
    }

}
