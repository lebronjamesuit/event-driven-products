package org.msss.cqrs.saga.userservice;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfigSecurity {

    //  "org.msss.cqrs.saga.sharedcommon.**",
    //   "org.msss.cqrs.saga.productservice.**"
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
                "org.msss.cqrs.saga.**"
        });
        return xStream;
    }
}
