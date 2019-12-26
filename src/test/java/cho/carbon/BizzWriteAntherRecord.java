package cho.carbon;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cho.carbon.complexus.FGRecordComplexus;
import cho.carbon.context.core.PersistentContext;
import cho.carbon.context.fg.FuncGroupContext;
import cho.carbon.context.hc.HCFusionContext;
import cho.carbon.entity.entity.Entity;
import cho.carbon.fuse.fg.CheckFGResult;
import cho.carbon.fuse.fg.CheckFuncGroup;
import cho.carbon.fuse.fg.FunctionGroup;
import cho.carbon.fuse.fg.IdentityQueryFuncGroup;
import cho.carbon.fuse.fg.ImproveFGResult;
import cho.carbon.fuse.fg.ThirdRoundImproveFuncGroup;
import cho.carbon.fuse.improve.attribute.FuseAttribute;
import cho.carbon.fuse.improve.transfer.BizzAttributeTransfer;
import cho.carbon.meta.criteria.model.ModelCriterion;
import cho.carbon.ops.complexus.OpsComplexus;
import cho.carbon.panel.Discoverer;
import cho.carbon.panel.Integration;
import cho.carbon.panel.IntegrationMsg;
import cho.carbon.panel.PanelFactory;
import cho.carbon.rrc.builder.FGRootRecordBuilder;
import cho.carbon.rrc.record.FGRootRecord;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BizzWriteAntherRecord {

	private static Logger logger = Logger.getLogger(BizzWriteAntherRecord.class);
	protected String strucTitle = "people";
	protected String dictionarystrucTitle = "default_dm";

	protected String code;
	@Resource
	SessionFactory sessionFactory;
	
	@Test
	public void readData() {
		// Session session = sessionFactory.getCurrentSession();
		long startTime = System.currentTimeMillis();
		HCFusionContext context = new HCFusionContext();
		context.setSource(HCFusionContext.SOURCE_COMMON);

		Integration integration = PanelFactory.getIntegration();
		Entity entity = createFmEntity();
		logger.debug(entity.toJson());
		IntegrationMsg msg = integration.integrate(context, entity);
		code = msg.getCode();
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		Entity result = discoverer.discover(code);
		logger.debug("一次融合"+code + " : " + result.toJson());

		HCFusionContext context1 = new HCFusionContext();
		context1.setSource(PersistentContext.SOURCE_COMMON);
		//设置 bnb
		context1.getFunctionGroupContainer().putFunctionGroup("ABCE010", new PeopleFG());
		entity = createPeopleEntity(strucTitle);
		// 二次融合,触发规则修改家庭地址
		msg = integration.integrate(context1, entity);

		Discoverer discoverer1 = PanelFactory.getDiscoverer(context);
		Entity result1 = discoverer1.discover(code);
		logger.debug(code + " : " + result1.toJson());
		
		assertEquals("修改后的地址",result1.getStringValue("家庭地址"));

		long endTime = System.currentTimeMillis();// 记录结束时间
		logger.debug((float) (endTime - startTime) / 1000);
	}

	private Entity createFmEntity() {
		Entity entity = new Entity("家庭信息");
		entity.putValue("家庭地址", "第一个地址");
		return entity;
	}
	


	private Entity createPeopleEntity(String strucTitle) {
		Entity entity = new Entity(strucTitle);
		entity.putValue("name", "家庭因子");
		entity.putValue("身份证", "370105196906281912");
		entity.putValue("市民卡", "370105196906281市卡Y");
		return entity;
	}

	protected class PeopleFG implements FunctionGroup, ThirdRoundImproveFuncGroup, IdentityQueryFuncGroup,CheckFuncGroup {

		@Override
		public ImproveFGResult preImprove(FuncGroupContext context, String recordCode, OpsComplexus opsComplexus,
				FGRecordComplexus recordComplexus) {

			return null;
		}

		@Override
		public ImproveFGResult improve(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
			
			return null;
		}

		@Override
		public ImproveFGResult postImprove(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {

			FGRootRecord rootRecord = recordComplexus.getRootRecord(recordCode);

			FGRootRecordBuilder builder =FGRootRecordBuilder.getInstance("ABCE011",code);// 家庭信息
			builder.putAttribute("SWJT002", "修改后的地址");
			Collection<FGRootRecord> list = new ArrayList<>();
			FGRootRecord record = builder.getRootRecord();
			list.add(record);

			ImproveFGResult imprveResult = new ImproveFGResult();
			imprveResult.setGeneratedRecords(list);
			

			return imprveResult;
		}

		@Override
		public Collection<ModelCriterion> getCriterions(String recordCode, FGRecordComplexus complexus) {
			List<FuseAttribute> list = BizzAttributeTransfer.transfer(complexus.getRootRecord(recordCode));
			return null;
		}

		@Override
		public ImproveFGResult secondImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			
			return null;
		}

		@Override
		public ImproveFGResult thirdImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			
			return null;
		}

	

		@Override
		public CheckFGResult afterCheck(FuncGroupContext context, String code, FGRecordComplexus complexus) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
