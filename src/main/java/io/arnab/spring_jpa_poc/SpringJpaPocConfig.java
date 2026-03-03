package io.arnab.spring_jpa_poc;

import io.arnab.spring_jpa_poc.id.PositionRepositoryCustom;
import io.arnab.spring_jpa_poc.id.PositionRepositoryCustomImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringJpaPocConfig {
    @Bean
    public PositionRepositoryCustom corePositionRepositoryCustom() {
        return new PositionRepositoryCustomImpl();
    }
}
