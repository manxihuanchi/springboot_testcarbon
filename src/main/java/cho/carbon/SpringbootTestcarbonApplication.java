package cho.carbon;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cho.carbon.anotation.EnableFGClient;

@EnableFGClient
@EnableRabbit
@Configuration
@EnableEurekaClient
@EnableDiscoveryClient
@EnableJpaRepositories
@SpringBootApplication
//@EnableFeignClients(basePackages= {"cho.carbon"})
@EnableAutoConfiguration(exclude=HibernateJpaAutoConfiguration.class)
@EnableTransactionManagement(proxyTargetClass = false)
@ComponentScan("cho.carbon")
public class SpringbootTestcarbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestcarbonApplication.class, args);
	}
	
	 @Bean
    public MessageConverter messageConverter() {
    	return new Jackson2JsonMessageConverter();
    }
	
}
