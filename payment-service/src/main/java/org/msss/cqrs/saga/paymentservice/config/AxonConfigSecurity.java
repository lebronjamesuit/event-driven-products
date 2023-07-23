package org.msss.cqrs.saga.paymentservice.config;


import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfigSecurity {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[]{
                "org.msss.cqrs.saga.**"

        });
        return xStream;
    }
}