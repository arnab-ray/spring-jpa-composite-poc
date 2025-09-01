package io.arnab.spring_jpa_poc;

import io.arnab.spring_jpa_poc.id.CorePositionRepositoryCustom;
import io.arnab.spring_jpa_poc.id.CorePositionRepositoryCustomImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringJpaPocConfig {
    @Bean
    public CorePositionRepositoryCustom corePositionRepositoryCustom() {
        return new CorePositionRepositoryCustomImpl();
    }
}
