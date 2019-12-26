package cho.carbon;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cho.carbon.context.hc.HCFusionContext;
import cho.carbon.entity.entity.Entity;
import cho.carbon.message.Message;
import cho.carbon.panel.Discoverer;
import cho.carbon.panel.Integration;
import cho.carbon.panel.IntegrationMsg;
import cho.carbon.panel.PanelFactory;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
	
	private static Logger logger = Logger.getLogger(UserTest.class);
	protected String strucName = "人口信息";
	
	
	@Test
	public void readData() {
		
			long startTime = System.currentTimeMillis();
			HCFusionContext context=new HCFusionContext();
			context.setSource(HCFusionContext.SOURCE_COMMON);
//			context.setToEntityRange(BizFusionContext.ENTITY_CONTENT_RANGE_ABCNODE_CONTAIN);
			context.setStrucTitle(strucName);
			context.setUserCode("e10adc3949ba59abbe56e057f28888d5");
			Integration integration=PanelFactory.getIntegration();
			Entity entity=createEntity(strucName);
			logger.debug("初始实体： " + entity.toJson());
			IntegrationMsg imsg=integration.integrate(context,entity);
			if(imsg.success()) {
				String code=imsg.getCode();
				Discoverer discoverer=PanelFactory.getDiscoverer(context);
				Entity result=discoverer.discover(code);
				logger.debug("融合后实体： " + code + " : "+ result.toJson());
			}else{
				for(Message message:imsg.getRefuse()) {
					logger.debug(message.toString());
				}
				
			}

			long endTime = System.currentTimeMillis();// 记录结束时间
			logger.debug((float) (endTime - startTime) / 1000);
	}
	
	private Entity createEntity(String mappingName) {
		
		Entity entity = new Entity(mappingName);

		entity.putValue("姓名", "测试9898"); 
		entity.putValue("身份证号码", "110101199003077598");
		
		return entity;
	}
	
	@Test
	public void updateData() {
		
			long startTime = System.currentTimeMillis();
			HCFusionContext context=new HCFusionContext();
			context.setSource(HCFusionContext.SOURCE_COMMON);
//			context.setToEntityRange(BizFusionContext.ENTITY_CONTENT_RANGE_ABCNODE_CONTAIN);
			context.setStrucTitle(strucName);
			context.setUserCode("e10adc3949ba59abbe56e057f28888d5");
			Integration integration=PanelFactory.getIntegration();
			Entity entity=updateEntity(strucName);
			logger.debug("初始实体： " + entity.toJson());
			IntegrationMsg imsg=integration.integrate(context,entity);
			if(imsg.success()) {
				String code=imsg.getCode();
				Discoverer discoverer=PanelFactory.getDiscoverer(context);
				Entity result=discoverer.discover(code);
				logger.debug("融合后实体： " + code + " : "+ result.toJson());
			}else{
				for(Message message:imsg.getRefuse()) {
					logger.debug(message.toString());
				}
				
			}

			long endTime = System.currentTimeMillis();// 记录结束时间
			logger.debug((float) (endTime - startTime) / 1000);
	}
	
	private Entity updateEntity(String mappingName) {
		
		Entity entity = new Entity(mappingName);

		entity.putValue("唯一编码", "7ce60626f6c8471dac69fa9766804626");
		entity.putValue("用户名", "program03"); 
		entity.putValue("原始密码", "123456");
		entity.putValue("状态", "正常使用");
		
		
		
		 
		return entity;
	}

	
	
}
