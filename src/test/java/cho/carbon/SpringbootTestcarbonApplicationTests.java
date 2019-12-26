package cho.carbon;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cho.carbon.biz.common.KieSessionFactory;
import cho.carbon.service.CarbonClientService;



@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootTestcarbonApplicationTests {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	CarbonClientService carbonClientService;
	
	@Test
	void contextLoads() throws SQLException {
		System.out.println("===============" + dataSource.getClass());
		Connection connection = dataSource.getConnection();
		System.out.println("===============" +connection);
		connection.close();
		
		
	}
	
	@Test
	void con() throws SQLException {
		System.out.println("===============" +sessionFactory);
		
		Session currentSession = sessionFactory.getCurrentSession();
		System.out.println(currentSession);
	}


	
	@Test
	public void fun() {
		KieSession findScannerSession = KieSessionFactory.findScannerSession("ks-dxjde2020-f1-pre");
		int fireAllRules = findScannerSession.fireAllRules();
		System.out.println("===============" + findScannerSession + "  执行规则数量： " + fireAllRules);
	}
	
	@Test
	public void fuo() {
//		Person person = new Person();
//		person.setUsername("网络");
//		person.setPassword("345345dsfge");
//		boolean runningAfterCodeQuery = carbonClientService.runningAfterCodeQuery(person);
//		
//		System.out.println("");
		
	}
	
}
