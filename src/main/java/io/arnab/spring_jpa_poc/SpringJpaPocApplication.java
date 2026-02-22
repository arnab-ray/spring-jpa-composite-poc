package io.arnab.spring_jpa_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"io.arnab.spring_jpa_poc"})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"io.arnab.spring_jpa_poc"})
@EntityScan(basePackages = {"io.arnab.spring_jpa_poc"})
@SpringBootApplication
public class SpringJpaPocApplication {

	public static void main(String[] args) {

        SpringApplication.run(SpringJpaPocApplication.class, args);
	}

}
