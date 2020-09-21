package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public RsEventService rsEventService(UserRepository userRepository,
                    RsEventRepository rsEventRepository) {
        return new RsEventService(rsEventRepository, userRepository);
    }
}
