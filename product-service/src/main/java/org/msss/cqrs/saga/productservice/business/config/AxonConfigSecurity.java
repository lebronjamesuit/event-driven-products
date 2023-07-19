package org.msss.cqrs.saga.productservice.business.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfigSecurity {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] {
                "org.msss.cqrs.saga.sharedcommon.**",
                "org.msss.cqrs.saga.productservice.**"
        });
        return xStream;
    }
}
