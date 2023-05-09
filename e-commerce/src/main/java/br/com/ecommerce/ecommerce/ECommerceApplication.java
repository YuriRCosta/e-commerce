package br.com.ecommerce.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "br.com.ecommerce.ecommerce.model")
@ComponentScan(basePackages = {"br.*"})
@EnableJpaRepositories(basePackages = {"br.com.ecommerce.ecommerce.repository"})
@EnableTransactionManagement
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
