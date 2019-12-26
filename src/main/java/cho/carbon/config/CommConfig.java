package cho.carbon.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
public class CommConfig {
	@Autowired
	private DataSource dataSource;

  @Primary
  @Bean(name = "sessionFactory")
  public LocalSessionFactoryBean localSessionFactoryBean() throws IOException {
      LocalSessionFactoryBean bean=new LocalSessionFactoryBean();
      bean.setDataSource(dataSource);
      bean.setPackagesToScan("cho.carbon*");
      Properties prop = new Properties();
      prop.setProperty("current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
      bean.setHibernateProperties(prop);
      bean.afterPropertiesSet();
      return bean;
  }
  
  @Bean
  public HibernateTransactionManager txManager(SessionFactory sessionFactory) throws Exception {
      HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
      hibernateTransactionManager.setSessionFactory(sessionFactory);
      return hibernateTransactionManager;
  }
    
}
