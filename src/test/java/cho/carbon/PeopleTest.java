package cho.carbon;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cho.carbon.biz.bnb.DXJDE2020BNB;
import cho.carbon.context.hc.HCFusionContext;
import cho.carbon.entity.entity.Entity;
import cho.carbon.message.Message;
import cho.carbon.panel.Discoverer;
import cho.carbon.panel.Integration;
import cho.carbon.panel.IntegrationMsg;
import cho.carbon.panel.PanelFactory;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PeopleTest {
	
	protected String mapperName = "人口信息";
	
	
	@Test
	public void readData() {
		
			long startTime = System.currentTimeMillis();
			HCFusionContext context=new HCFusionContext();
			context.setSource(HCFusionContext.SOURCE_COMMON);

			context.setStrucTitle(mapperName);
			context.setUserCode("e10adc3949ba59abbe56e057f28888d5");
			
			context.getFunctionGroupContainer().putFunctionGroup("DXJDE2020",new DXJDE2020BNB());
			
			Integration integration=PanelFactory.getIntegration();
			Entity entity=createEntity(mapperName);
			System.out.println("初始实体： " + entity.toJson());
			IntegrationMsg imsg=integration.integrate(context,entity);
			if(imsg.success()) {
				String code=imsg.getCode();
				Discoverer discoverer=PanelFactory.getDiscoverer(context);
				Entity result=discoverer.discover(code);
				System.out.println("融合后实体： " + code + " : "+ result.toJson());
			}else{
				for(Message message:imsg.getRefuse()) {
					System.out.println(message.toString());
				}
				
			}

			long endTime = System.currentTimeMillis();// 记录结束时间
			System.out.println((float) (endTime - startTime) / 1000);
	}
	
	private Entity createEntity(String mappingName) {
		
		Entity entity = new Entity(mappingName);
		//entity.putValue("唯一编码", "d0e2eb99c6c34aeaa9e66c893afe4b89");
		entity.putValue("姓名", "测试an888");
		entity.putValue("身份证号码", "110101199003077598");
		/*Entity relationentity = new Entity("户籍家庭");
		
		
		relationentity.putValue("唯一编码", "687fafa97c0f491aaa3dd86e94220618");
		relationentity.putValue("户籍地址", "西湖区");
		relationentity.putValue("家庭分类", EnumKeyValue.ENUM_家庭分类_户籍家庭);
		entity.putRelationEntity("户籍家庭", "归属家庭", relationentity);
	*/
		
		/*SimpleEntity sentity2 = new SimpleEntity("证件信息");
		sentity2.putValue("证件类型", EnumKeyValue.ENUM_证件类型_身份证);
		sentity2.putValue("证件号码", "23423");
		sentity2.putValue("有效期结束", "2015-10-12");
		entity.putMultiAttrEntity(sentity2);*/
		
		/*SimpleEntity sentity = new SimpleEntity("居住信息");
		sentity.putValue("居住地门牌号", "祥符桥社区玉泉公寓");
		sentity.putValue("居住标识", "一般");
		entity.putMultiAttrEntity(sentity);*/
		
		
		/*SimpleEntity sentity1 = new SimpleEntity("居住信息");
		sentity1.putValue("居住地门牌号", "祥符社区xxx公益");
		sentity1.putValue("居住标识", "默认");
		entity.putMultiAttrEntity(sentity1);
		
		SimpleEntity sentity2 = new SimpleEntity("居住信息");
		sentity2.putValue("居住地门牌号", "祥符社区ddd分为");
		sentity2.putValue("居住标识", "常用");
		entity.putMultiAttrEntity(sentity2);
		
		*/
		
		/*Entity relationentity = new Entity("走访记录");
		relationentity.putValue("唯一编码", "cc7b211fb5ea48cc96cbfc44f19d29dd");
		relationentity.putValue("走访时间", "2019-1-14"); 
		relationentity.putValue("走访内容", "xxxxx");
		relationentity.putValue("走访类型", EnumKeyValue.ENUM_走访类型_老年人走访);
		entity.putRelationEntity("走访记录","走访记录", relationentity);*/
		
		//return relationentity;
		
		/*entity.putRelationEntity("子女信息","子女", relationentity);*/
		
		/*Entity relationentity1 = new Entity("人口信息");
		relationentity1.putValue("唯一编码", "0effa5c786034f5388cce004595ef6e8");
		relationentity1.putValue("姓名", "小强大女儿"); 
		relationentity1.putValue("人口类型", "户籍人口");
		relationentity1.putValue("所属社区", EnumKeyValue.ENUM_祥符街道社区_祥符桥社区);
		relationentity1.putValue("身份证号码", "23231112");
		entity.putRelationEntity("子女信息","子女", relationentity1);*/
		
		/*SimpleEntity sentity3 = new SimpleEntity("老人补助信息");
		sentity3.putValue("补助类型", EnumKeyValue.ENUM_老人补助枚举_居家养老补助);
		sentity3.putValue("补助金额", "3232");
		entity.putMultiAttrEntity(sentity3);*/
		
		/*SimpleEntity sentity3 = new SimpleEntity("人口错误信息");
		sentity3.putValue("错误类型", EnumKeyValue.ENUM_错误类型_身份证错误);
		sentity3.putValue("描述", "身份错误4444");
		entity.putMultiAttrEntity(sentity3);*/
		
		/*
		 * LeafEntity sentity2 = new LeafEntity("残疾信息"); sentity2.putValue("残疾类别",
		 * EnumKeyValue.ENUM_残疾类别_视力); sentity2.putValue("残疾等级",
		 * EnumKeyValue.ENUM_残疾等级_一级); entity.putMultiAttrEntity(sentity2);
		 */
		
		/*SimpleEntity sentity1 = new SimpleEntity("户籍变更");
		sentity1.putValue("申报人姓名", "李好帅");
		sentity1.putValue("变动前街路巷", "好帅社区1");
		sentity1.putValue("变动前门（楼）详址", "51幢3单元504");
		sentity1.putValue("变动后街路巷", "明星小区1");
		sentity1.putValue("变动后门（楼）详址", "41幢8单元902");
		sentity1.putValue("更改户籍门牌号", EnumKeyValue.ENUM_是否_是);
		sentity1.putValue("变动日期", "2018-10-14");
		entity.putMultiAttrEntity(sentity1);
		
		SimpleEntity sentity = new SimpleEntity("户籍变更");
		sentity.putValue("申报人姓名", "李好帅");
		sentity.putValue("变动前街路巷", "好帅社区");
		sentity.putValue("变动前门（楼）详址", "5幢3单元504");
		sentity.putValue("变动后街路巷", "明星小区");
		sentity.putValue("变动后门（楼）详址", "4幢8单元902");
		sentity.putValue("更改户籍门牌号", EnumKeyValue.ENUM_是否_是);
		sentity.putValue("变动日期", "2018-10-10");
		entity.putMultiAttrEntity(sentity);*/
		
		
		/*Entity relationentity = new Entity("配置文件名称");*/
//		relationentity.putValue("名字", "刘志华5");
//		entity.putRelationEntity("关系名称", "关系标签（具体关系）", relationentity);
		return entity;
	}

	
	
}
