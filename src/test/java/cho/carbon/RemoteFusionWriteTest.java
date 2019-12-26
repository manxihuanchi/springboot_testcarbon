package cho.carbon;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cho.carbon.complexus.FGRecordComplexus;
import cho.carbon.context.core.PersistentContext;
import cho.carbon.context.core.RemovedRecordInfo;
import cho.carbon.context.fg.FuncGroupContext;
import cho.carbon.context.hc.HCFusionContext;
import cho.carbon.entity.entity.Entity;
import cho.carbon.entity.entity.LeafEntity;
import cho.carbon.fuse.fg.CheckFGResult;
import cho.carbon.fuse.fg.CheckFuncGroup;
import cho.carbon.fuse.fg.ConJunctionFGResult;
import cho.carbon.fuse.fg.FGOSerializableFactory;
import cho.carbon.fuse.fg.FetchFGResult;
import cho.carbon.fuse.fg.FetchFuncGroup;
import cho.carbon.fuse.fg.FunctionGroup;
import cho.carbon.fuse.fg.FuseCallBackFuncGroup;
import cho.carbon.fuse.fg.IdentityQueryFuncGroup;
import cho.carbon.fuse.fg.ImproveFGResult;
import cho.carbon.fuse.fg.QueryJunctionFuncGroup;
import cho.carbon.fuse.fg.RemoveCallBackFuncGroup;
import cho.carbon.fuse.fg.ThirdRoundImproveFuncGroup;
import cho.carbon.fuse.improve.attribute.FuseAttribute;
import cho.carbon.fuse.improve.attribute.FuseAttributeFactory;
import cho.carbon.fuse.improve.ops.builder.FuseFGRecordOpsBuilder;
import cho.carbon.fuse.improve.transfer.BizzAttributeTransfer;
import cho.carbon.meta.criteria.model.ModelConJunction;
import cho.carbon.meta.criteria.model.ModelCriterion;
import cho.carbon.ops.builder.RecordRelationOpsBuilder;
import cho.carbon.ops.complexus.OpsComplexus;
import cho.carbon.panel.Discoverer;
import cho.carbon.panel.Integration;
import cho.carbon.panel.IntegrationMsg;
import cho.carbon.panel.PanelFactory;
import cho.carbon.rrc.builder.FGRootRecordBuilder;
import cho.carbon.rrc.record.FGAttribute;
import cho.carbon.rrc.record.FGRootRecord;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteFusionWriteTest {

	private static final Logger logger = LoggerFactory.getLogger(RemoteFusionWriteTest.class);
	protected String strucTitle = "people";
	protected String dictionarystrucTitle = "default_dm";

	protected String code;
	@Resource
	SessionFactory sessionFactory;
	private final String firstSetValue = "1施连心first";
	private final String secondSetValue = "1施连心edit", secondImpValue = "1施连心edit2";

	@Test
	public void readData() {
		// Session session = sessionFactory.getCurrentSession();
		long startTime = System.currentTimeMillis();
		HCFusionContext context = new HCFusionContext();
		context.setSource(PersistentContext.SOURCE_COMMON);

		// context.setDictionarystrucTitle(dictionarystrucTitle);
		Integration integration = PanelFactory.getIntegration();
		Entity entity = createEntity1(strucTitle);
		logger.debug(entity.toJson());
		IntegrationMsg msg = integration.integrate(context, entity);
		code = msg.getCode();
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		Entity result = discoverer.discover(code);
		logger.debug("一次融合" + code + " : " + result.toJson());
		assertEquals(firstSetValue, result.getStringValue("name"));

		// 设置 bnb
		context.getFunctionGroupContainer().putFunctionGroup("ABCE010", new PeopleFG());

		entity = createEntity2(strucTitle, code);
		// 二次融合
		msg = integration.integrate(context, entity);

		Discoverer discoverer1 = PanelFactory.getDiscoverer(context);
		Entity result1 = discoverer1.discover(code);
		logger.debug(code + " : " + result1.toJson());

		assertEquals(secondImpValue, result1.getStringValue("name"));
		assertEquals(2, result1.getGroup2DEntity("低保信息").size());

		long endTime = System.currentTimeMillis();// 记录结束时间
		logger.debug("用时：{}", (float) (endTime - startTime) / 1000);
	}

	private Entity createEntity1(String strucTitle) {
		Entity entity = new Entity(strucTitle);
		entity.putValue("name", firstSetValue);
		entity.putValue("身份证", "370105196906281912");
		entity.putValue("市民卡", "370105196906281市卡Y");
		entity.putValue("性别", "男");
		entity.putValue("子女数", 32);
		entity.putValue("区县政府定期补助金", null);
		entity.putValue("预设枚举性别", "女型");
		entity.putValue("出生日期", "1996-09-06");
		entity.putValue("级联省", "浙江省->杭州");

		// 枚举多选
		entity.putValue("枚举多选", "苗族,回族");

		LeafEntity sentity;

		sentity = new LeafEntity("低保信息");
		sentity.putValue("唯一编码", "80b97bd531244a60a9329b6091c18d8e");
		sentity.putValue("申请人", "你行8");
		sentity.putValue("多级测试", "一级a->二级aa");
		entity.putGroup2DEntity(sentity);

		entity.putValue("人口类型", "户籍人口");

		Entity relationentity;
		relationentity = new Entity("关系人口");
		relationentity.putValue("唯一编码", "b5baeb3f94e84f248635f5abfd8cd7ff");
		relationentity.putValue("身份证", "330105196906281111");
		relationentity.putValue("姓名", "刘志华5");
		relationentity.putValue("性别", "男");
		sentity = new LeafEntity("低保信息");
		sentity.putValue("申请人", "你好5");
		relationentity.putGroup2DEntity(sentity);
		entity.putRelationEntity("关系人口", "儿媳,入党联系人", relationentity);

		return entity;
	}

	private Entity createEntity2(String strucTitle, String code) {
		Entity entity = new Entity(strucTitle);
		entity.putValue("唯一编码", code);
		entity.putValue("name", secondSetValue);
		entity.putValue("身份证", "370105196906281912");
		entity.putValue("市民卡", "370105196906281市卡Y");
		entity.putValue("性别", "男");
		entity.putValue("子女数", 32);
		entity.putValue("区县政府定期补助金", null);
		entity.putValue("预设枚举性别", "女型");
		entity.putValue("出生日期", "1996-09-06");
		entity.putValue("级联省", "浙江省->杭州");

		// 枚举多选
		entity.putValue("枚举多选", "苗族,回族");

		LeafEntity sentity;

		sentity = new LeafEntity("低保信息");
		sentity.putValue("唯一编码", "80b97bd531244a60a9329b6091c18d8e");
		sentity.putValue("申请人", "你行7");
		sentity.putValue("多级测试", "一级a->二级aa");
		entity.putGroup2DEntity(sentity);

		entity.putValue("人口类型", "户籍人口");

		Entity relationentity;
		relationentity = new Entity("关系人口");
		relationentity.putValue("唯一编码", "b5baeb3f94e84f248635f5abfd8cd7ff");
		relationentity.putValue("身份证", "330105196906281111");
		relationentity.putValue("姓名", "刘志华5");
		relationentity.putValue("性别", "男");
		sentity = new LeafEntity("低保信息");
		sentity.putValue("申请人", "你好5");
		relationentity.putGroup2DEntity(sentity);
		entity.putRelationEntity("关系人口", "儿媳,入党联系人", relationentity);

		return entity;
	}

	protected class PeopleFG implements FunctionGroup, IdentityQueryFuncGroup, ThirdRoundImproveFuncGroup,
			CheckFuncGroup, RemoveCallBackFuncGroup, FuseCallBackFuncGroup, QueryJunctionFuncGroup, FetchFuncGroup {
		RemotePeopleFG remotePeopleFG = new RemotePeopleFG();

		@Override
		public ImproveFGResult preImprove(FuncGroupContext context, String recordCode, OpsComplexus opsComplexus,
				FGRecordComplexus recordComplexus) {

			OpsComplexus remoteOpsComplexus = FGOSerializableFactory.des2OpsComplexus(opsComplexus.serialize());
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			
			ImproveFGResult improveFGResult = remotePeopleFG.preImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode, remoteOpsComplexus,
					remoteFGRecordComplexus);
			
			return FGOSerializableFactory.des2ImproveFGResult(improveFGResult.serialize());
		}

		@Override
		public ImproveFGResult improve(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			ImproveFGResult improveFGResult = remotePeopleFG.improve(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode,
					remoteFGRecordComplexus);
			return FGOSerializableFactory.des2ImproveFGResult(improveFGResult.serialize());
		}

		@Override
		public ImproveFGResult postImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			ImproveFGResult improveFGResult = remotePeopleFG.postImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode,
					remoteFGRecordComplexus);
			return FGOSerializableFactory.des2ImproveFGResult(improveFGResult.serialize());
		}

		@Override
		public Collection<ModelCriterion> getCriterions(String recordCode, FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			Collection<ModelCriterion> criterions = remotePeopleFG.getCriterions(recordCode, remoteFGRecordComplexus);
			return FGOSerializableFactory.des2Criterions(FGOSerializableFactory.serializeCriterions(criterions));
		}

		@Override
		public ImproveFGResult secondImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			ImproveFGResult improveFGResult = remotePeopleFG.secondImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode,
					remoteFGRecordComplexus);
			return FGOSerializableFactory.des2ImproveFGResult(improveFGResult.serialize());
		}

		@Override
		public ImproveFGResult thirdImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			ImproveFGResult improveFGResult = remotePeopleFG.thirdImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode,
					remoteFGRecordComplexus);
			return FGOSerializableFactory.des2ImproveFGResult(improveFGResult.serialize());
		}

		@Override
		public CheckFGResult afterCheck(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			FGRecordComplexus remoteFGRecordComplexus = FGOSerializableFactory
					.des2FGRecordComplexus(recordComplexus.serialize());
			CheckFGResult checkResult = remotePeopleFG.afterCheck(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode,
					remoteFGRecordComplexus);
			return FGOSerializableFactory.des2CheckFGResult(checkResult.serialize());
		}

		@Override
		public FetchFGResult fetchImprove(FuncGroupContext context, FGRootRecord record) {
			FGRootRecord remoteRecord = FGOSerializableFactory
					.des2FGRootRecord(record.serialize());
			FetchFGResult fetchFGResult=remotePeopleFG.fetchImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), remoteRecord);
			return FGOSerializableFactory.des2FetchFGResult(fetchFGResult.serialize());
		}

		@Override
		public ConJunctionFGResult junctionImprove(FuncGroupContext context, ModelConJunction conJunction) {
			ModelConJunction remoteConJunction = FGOSerializableFactory
					.des2ModelConJunction(conJunction.serialize());
			ConJunctionFGResult conJunctionFGResult=remotePeopleFG.junctionImprove(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), remoteConJunction);
			return FGOSerializableFactory.des2ConJunctionFGResult(conJunctionFGResult.serialize());
		}

		@Override
		public boolean afterFusition(FuncGroupContext context, String recordCode) {

			return remotePeopleFG.afterFusition(
					FGOSerializableFactory.des2FuncGroupContext(context.serialize()), recordCode);
		}

		@Override
		public boolean beforeRemove(FuncGroupContext context, Collection<RemovedRecordInfo> removedRecordInfos) {
			Collection<RemovedRecordInfo> remoteRemovedRecordInfos = FGOSerializableFactory
					.des2RemovedRecordInfos(FGOSerializableFactory.serializeRemovedRecordInfos(removedRecordInfos));
			return remotePeopleFG.beforeRemove(FGOSerializableFactory.des2FuncGroupContext(context.serialize()), remoteRemovedRecordInfos);
		}

		@Override
		public boolean afterRemove(FuncGroupContext context, Collection<RemovedRecordInfo> removedRecordInfos) {
			Collection<RemovedRecordInfo> remoteRemovedRecordInfos = FGOSerializableFactory
					.des2RemovedRecordInfos(FGOSerializableFactory.serializeRemovedRecordInfos(removedRecordInfos));
			return remotePeopleFG.afterRemove(FGOSerializableFactory.des2FuncGroupContext(context.serialize()), remoteRemovedRecordInfos);
		}

	}

	protected class RemotePeopleFG implements FunctionGroup, IdentityQueryFuncGroup, ThirdRoundImproveFuncGroup,
			CheckFuncGroup, RemoveCallBackFuncGroup, FuseCallBackFuncGroup, QueryJunctionFuncGroup, FetchFuncGroup {

		@Override
		public ImproveFGResult preImprove(FuncGroupContext context, String recordCode, OpsComplexus opsComplexus,
				FGRecordComplexus recordComplexus) {

			return new ImproveFGResult();
		}

		@Override
		public ImproveFGResult improve(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {

			if (recordCode.equals(code)) {
				assertEquals(secondSetValue,
						recordComplexus.getRootRecord(recordCode).findAttribute("SW0208").getInherentTypeValue());

				ImproveFGResult imprveResult = new ImproveFGResult();
				FuseFGRecordOpsBuilder rootRecordOpsBuilder = FuseFGRecordOpsBuilder.getInstance("ABCE010", recordCode);
				Collection<FGAttribute> attributes = new ArrayList<>();
				FGAttribute attribute = FuseAttributeFactory.buildAttribute("SW0208", secondImpValue);
				attributes.add(attribute);

				Collection<FGAttribute> leafAttributes = new ArrayList<>();
//				attribute = new WSDTAttribute("SW2074", "你好POST");
//				attribute =FuseAttributeFactory.buildAttribute("SW2074", secondImpValue);
				leafAttributes.add(attribute);

				rootRecordOpsBuilder.addAttribute(attributes);
				rootRecordOpsBuilder.addLeaf("SW2070", leafAttributes);

				imprveResult.setRootRecordOps(rootRecordOpsBuilder.getRootRecordOps());

				return imprveResult;
			}
			return new ImproveFGResult();
		}

		@Override
		public ImproveFGResult postImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {

			FGRootRecord rootRecord = recordComplexus.getRootRecord(recordCode);

			FGRootRecordBuilder builder = FGRootRecordBuilder.getInstance("ABCE011");// 家庭信息
			builder.putAttribute("ABP0001", "e10adc3949ba59abbe56e057f2abcdef");
			builder.putAttribute("SWJT001", 12);
			builder.putAttribute("SWJT002", "哪跟哪11");
			Collection<FGRootRecord> list = new ArrayList<>();
			FGRootRecord record = builder.getRootRecord();
			list.add(record);

			// 添加个关系 R09119001 家庭信息
			RecordRelationOpsBuilder recordRelationOpsBuilder = RecordRelationOpsBuilder.getInstance("ABCE011",
					record.getCode());
			recordRelationOpsBuilder.putRelation("R09160002", rootRecord.getCode());

			ImproveFGResult imprveResult = new ImproveFGResult();
			imprveResult.setGeneratedRecords(list);
			imprveResult.putDerivedRecordRelationOps(recordRelationOpsBuilder.getRecordRelationOps());

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
			logger.debug("二级，yes");
			return new ImproveFGResult();
		}

		@Override
		public ImproveFGResult thirdImprove(FuncGroupContext context, String recordCode,
				FGRecordComplexus recordComplexus) {
			logger.debug("三级，yes");
			return new ImproveFGResult();
		}

		@Override
		public CheckFGResult afterCheck(FuncGroupContext context, String code, FGRecordComplexus complexus) {
			return new CheckFGResult();
		}

		@Override
		public FetchFGResult fetchImprove(FuncGroupContext context, FGRootRecord record) {
			return new FetchFGResult();
		}

		@Override
		public ConJunctionFGResult junctionImprove(FuncGroupContext context, ModelConJunction conJunction) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean afterFusition(FuncGroupContext context, String recordCode) {
			return false;
		}

		@Override
		public boolean beforeRemove(FuncGroupContext context, Collection<RemovedRecordInfo> removedRecordInfos) {
			return false;
		}

		@Override
		public boolean afterRemove(FuncGroupContext context, Collection<RemovedRecordInfo> removedRecordInfos) {
			return false;
		}

	}

}
