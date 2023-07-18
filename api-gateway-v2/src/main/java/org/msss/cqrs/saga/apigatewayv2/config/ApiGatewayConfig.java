package org.msss.cqrs.saga.apigatewayv2.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Var", "Value"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.path("/api/v1/products/**")
                        .uri("lb://products-service")
                )
                .route(p -> p.path("/api/v1/orders/**")
                        .uri("lb://order-service"))
                .build();
    }

    // This bean solved the DNS on MAC OS
   /* java.net.UnknownHostException: failed to resolve 'product-service' after 2 queries
    at io.netty.resolver.dns.DnsResolveContext.finishResolve(DnsResolveContext.java:1013) ~[netty-resolver-dns-4.1.55.Final.jar:4.1.55.Final]
    Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:*/

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

}