package cho.carbon.biz.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;

import cho.carbon.complexus.FGRecordComplexus;
import cho.carbon.context.fg.FuncGroupContext;
import cho.carbon.fuse.fg.CheckFGResult;
import cho.carbon.fuse.fg.ConJunctionFGResult;
import cho.carbon.fuse.fg.FetchFGResult;
import cho.carbon.fuse.fg.ImproveFGResult;
import cho.carbon.fuse.improve.attribute.FuseAttribute;
import cho.carbon.fuse.improve.attribute.leaf.FuseLeafAttribute;
import cho.carbon.fuse.improve.ops.builder.FuseFGRecordOpsBuilder;
import cho.carbon.fuse.improve.transfer.BizzAttributeTransfer;
import cho.carbon.message.Message;
import cho.carbon.meta.criteria.model.ModelConJunction;
import cho.carbon.meta.criteria.model.ModelCriterion;
import cho.carbon.meta.criteria.model.relation.RightRelationJunction;
import cho.carbon.ops.builder.ConJunctionOpsBuilder;
import cho.carbon.ops.builder.RecordRelationOpsBuilder;
import cho.carbon.ops.complexus.OpsComplexus;
import cho.carbon.query.model.FGConJunctionFactory;
import cho.carbon.relation.FGRelationCorrelation;
import cho.carbon.rrc.record.FGAttribute;
import cho.carbon.rrc.record.FGRootRecord;

public class KIEHelper {


//	private static Logger logger = Logger.getLogger(KIEHelper.class);

	public static Collection<ModelCriterion> getBizCriteriaListFromKIE(String recordCode, FGRecordComplexus complexus,
			KieSession kSession) {
		FGRootRecord record = complexus.getRootRecord(recordCode);
		String recordName = record.getName();

		List<FuseAttribute> transfer = BizzAttributeTransfer.transfer(record);

		BizzAttributeTransfer.transfer(record).forEach(fuseAttribute -> kSession.insert(fuseAttribute));
		kSession.setGlobal("recordName", recordName);

		FGConJunctionFactory conJunctionFactory = null;
		try {

			conJunctionFactory = new FGConJunctionFactory(recordName);

			// conJunctionFactory.getGroupFactory().addCommon(itemCode, value,
			// UnaryOperator)
			// conJunctionFactory.getGroupFactory().addBetween(itemCode, left, right,
			// operator)
			// conJunctionFactory.getGroup2DFactory("").addBetween(itemCode, left, right,
			// operator)
			// conJunctionFactory.getGroupFactory().addCommon($nameFB.getAttribute().getName(),
			// $nameFB.getAttribute().getValueStr(), UnaryOperator.EQUAL);
			// conJunctionFactory.getGroupFactory().addCommon($IdFB.getAttribute().getName(),
			// $IdFB.getAttribute().getValueStr(), UnaryOperator.EQUAL);

			kSession.setGlobal("conJunctionFactory", conJunctionFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 触发规则
		System.out.println("开始执行规则===================== ");
		int fireAllRules = kSession.fireAllRules();
		System.out.println("本次触发规则数量 =  " + fireAllRules);
		System.out.println("规则执行完毕===================== ");

		kSession.destroy();

		ModelConJunction junction = conJunctionFactory.getJunction();
		Collection<ModelCriterion> criterions = null;
		if (junction != null) {
			criterions = junction.getCriterions();
		}

		return criterions;
	}

	public static ImproveFGResult getImproveFGResultFromKIE(FuncGroupContext fgFusionContext, String recordCode,
			OpsComplexus opsComplexus, FGRecordComplexus recordComplexus, KieSession kSession) {

		String userCode = fgFusionContext.getUserCode();
		FGRootRecord fgRootRecord = recordComplexus.getRootRecord(recordCode);

		String recordName = fgRootRecord.getName();
		String hostCode = recordComplexus.getHostCode();
		String hostType = recordComplexus.getHostType();
		// 定义 全局变量

		List<FGRootRecord> rootRecordList = new ArrayList<FGRootRecord>();
		List<FGAttribute> attributeList = new ArrayList<FGAttribute>();
		List<FuseLeafAttribute> addedLeafAttrList = new ArrayList<FuseLeafAttribute>();
		Map<String, String> removedLeafAttrMap = new HashMap<String, String>();

		// 存放新建
		List<RecordRelationOpsBuilder> recordRelationOpsBuilderNew = new ArrayList<RecordRelationOpsBuilder>();

		RecordRelationOpsBuilder recordRelationOpsBuilder = RecordRelationOpsBuilder.getInstance(recordName,
				recordCode);
		List<Message> messageList = new ArrayList<Message>();
		try {
			kSession.setGlobal("recordRelationOpsBuilder", recordRelationOpsBuilder);

			kSession.setGlobal("recordRelationOpsBuilderNew", recordRelationOpsBuilderNew);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordRelationOpsBuilderNew");
		}
		
		try {
			kSession.setGlobal("recordCode", recordCode);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordCode");
		}
		
		try {
			kSession.setGlobal("userCode", userCode);
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： userCode");
		}
		
		try {
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordName");
		}
		
		try {
			kSession.setGlobal("rootRecordList", rootRecordList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： rootRecordList");
		}
		try {
			kSession.setGlobal("hostCode", hostCode);
		} catch (Exception e) {
			System.out.println("全局变量未设置： hostCode");
		}
		try {
			kSession.setGlobal("hostType", hostType);
		} catch (Exception e) {
			System.out.println("全局变量未设置： hostType");
		}
		try {
			kSession.setGlobal("attributeList", attributeList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： attributeList");
		}
		try {
			kSession.setGlobal("addedLeafAttrList", addedLeafAttrList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： addedLeafAttrList");
		}
		try {
			kSession.setGlobal("removedLeafAttrMap", removedLeafAttrMap);
		} catch (Exception e) {
			System.out.println("全局变量未设置： removedLeafAttrMap");
		}
		try {
			kSession.setGlobal("fgRootRecord", fgRootRecord);
		} catch (Exception e) {
			System.out.println("全局变量未设置： rootRecord");
		}
		try {
			kSession.setGlobal("recordComplexus", recordComplexus);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordComplexus");
		}
		try {
			kSession.setGlobal("messageList", messageList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： messageList");
		}
		// insert object
		BizzAttributeTransfer.transfer(fgRootRecord).forEach(fuseAttribute -> kSession.insert(fuseAttribute));

		// 这里需要改
		FGRelationCorrelation relationCorrelation = recordComplexus.getRelationCorrelation(recordCode);

		if (relationCorrelation != null) {

			relationCorrelation.getRecordRelation().forEach(recordRelation -> kSession.insert(recordRelation));
		}

		if (opsComplexus != null) {
			if (opsComplexus.getRootRecordOps(recordCode) != null) {
				BizzAttributeTransfer.transfer(opsComplexus.getRootRecordOps(recordCode))
						.forEach(opsAttr -> kSession.insert(opsAttr));
			}

			if (opsComplexus.getRecordRelationOps(recordCode) != null) {
				BizzAttributeTransfer.transfer(opsComplexus.getRecordRelationOps(recordCode))
						.forEach(opsRelation -> kSession.insert(opsRelation));
			}

		}

		// 触发规则
		System.out.println("开始执行规则===================== ");
		int fireAllRules = kSession.fireAllRules();
		System.out.println("本次触发规则数量 =  " + fireAllRules);
		System.out.println("规则执行完毕===================== ");
		kSession.destroy();

		// 组装结果
		FuseFGRecordOpsBuilder recordOpsBuilder = FuseFGRecordOpsBuilder.getInstance(recordName, recordCode);
		recordOpsBuilder.addAttribute(attributeList);

		recordOpsBuilder.addLeafAttribute(addedLeafAttrList);
		// 删除的多值属性
		for (String key : removedLeafAttrMap.keySet()) {
			recordOpsBuilder.removeLeaf(removedLeafAttrMap.get(key), key);
		}

		ImproveFGResult imprveResult = new ImproveFGResult();
		imprveResult.setRootRecordOps(recordOpsBuilder.getRootRecordOps());
		imprveResult.setRecordRelationOps(recordRelationOpsBuilder.getRecordRelationOps());
		imprveResult.setGeneratedRecords(rootRecordList);
		imprveResult.setMessages(messageList);

		for (RecordRelationOpsBuilder builder : recordRelationOpsBuilderNew) {
			imprveResult.putDerivedRecordRelationOps(builder.getRecordRelationOps());
		}

		return imprveResult;
	}

	public static ImproveFGResult getImproveFGResultFromKIE(FuncGroupContext context, String recordCode,
			FGRecordComplexus recordComplexus, KieSession kSession) {
		return getImproveFGResultFromKIE(context, recordCode, null, recordComplexus, kSession);
	}

	public static CheckFGResult getCheckInfoFromKIE(FuncGroupContext context, String recordCode,
			FGRecordComplexus recordComplexus, KieSession kSession) {

		String userCode = context.getUserCode();
		FGRootRecord fgRootRecord = recordComplexus.getRootRecord(recordCode);

		String recordName = fgRootRecord.getName();
		String hostCode = recordComplexus.getHostCode();
		String hostType = recordComplexus.getHostType();
		// 定义 全局变量

		List<Message> messageList = new ArrayList<Message>();
		try {
			kSession.setGlobal("recordCode", recordCode);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordCode");
		}
		
		try {
			kSession.setGlobal("userCode", userCode);
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： userCode");
		}
		
		try {
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordName");
		}
		try {
			kSession.setGlobal("hostCode", hostCode);
		} catch (Exception e) {
			System.out.println("全局变量未设置： hostCode");
		}
		try {
			kSession.setGlobal("hostType", hostType);
		} catch (Exception e) {
			System.out.println("全局变量未设置： hostType");
		}

		try {
			kSession.setGlobal("fgRootRecord", fgRootRecord);
		} catch (Exception e) {
			System.out.println("全局变量未设置： rootRecord");
		}
		try {
			kSession.setGlobal("recordComplexus", recordComplexus);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordComplexus");
		}
		try {
			kSession.setGlobal("messageList", messageList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： messageList");
		}

		// insert object
		BizzAttributeTransfer.transfer(fgRootRecord).forEach(fuseAttribute -> kSession.insert(fuseAttribute));

		// 这里需要改
		FGRelationCorrelation relationCorrelation = recordComplexus.getRelationCorrelation(recordCode);

		if (relationCorrelation != null) {
			relationCorrelation.getRecordRelation().forEach(recordRelation -> kSession.insert(recordRelation));
		}

		// 触发规则
		System.out.println("开始执行规则===================== ");
		int fireAllRules = kSession.fireAllRules();
		System.out.println("本次触发规则数量 =  " + fireAllRules);
		System.out.println("规则执行完毕===================== ");
		kSession.destroy();

		// 组装结果
		CheckFGResult fuseCheckInfo = new CheckFGResult(recordCode);
		fuseCheckInfo.setMessages(messageList);
		return fuseCheckInfo;
	}

	public static FetchFGResult getFetchImproveResultFromKIE(FuncGroupContext context,
			FGRootRecord fgRootRecord, KieSession kSession) {

		String userCode = context.getUserCode();

		String recordName = fgRootRecord.getName();
		String recordCode = fgRootRecord.getCode();
		// 定义 全局变量
		List<FGAttribute> attributeList = new ArrayList<FGAttribute>();
		List<FuseLeafAttribute> addedLeafAttrList = new ArrayList<FuseLeafAttribute>();
		Map<String, String> removedLeafAttrMap = new HashMap<String, String>();

		try {
			kSession.setGlobal("recordCode", recordCode);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordCode");
		}
		
		try {
			kSession.setGlobal("userCode", userCode);
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： userCode");
		}
		
		try {
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordName");
		}

		try {
			kSession.setGlobal("fgRootRecord", fgRootRecord);
		} catch (Exception e) {
			System.out.println("全局变量未设置： rootRecord");
		}
		try {
			kSession.setGlobal("attributeList", attributeList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： attributeList");
		}
		try {
			kSession.setGlobal("addedLeafAttrList", addedLeafAttrList);
		} catch (Exception e) {
			System.out.println("全局变量未设置： addedLeafAttrList");
		}
		try {
			kSession.setGlobal("removedLeafAttrMap", removedLeafAttrMap);
		} catch (Exception e) {
			System.out.println("全局变量未设置： removedLeafAttrMap");
		}

		// insert object
		BizzAttributeTransfer.transfer(fgRootRecord).forEach(fuseAttribute -> kSession.insert(fuseAttribute));

		// 触发规则
		System.out.println("开始执行规则===================== ");
		int fireAllRules = kSession.fireAllRules();
		System.out.println("本次触发规则数量 =  " + fireAllRules);
		System.out.println("规则执行完毕===================== ");
		kSession.destroy();

		// 组装结果
		FetchFGResult fetchImproveResult = new FetchFGResult();
		// 组装结果
		FuseFGRecordOpsBuilder recordOpsBuilder = FuseFGRecordOpsBuilder.getInstance(recordName, recordCode);
		recordOpsBuilder.addAttribute(attributeList);
		recordOpsBuilder.addLeafAttribute(addedLeafAttrList);
		// 删除的多值属性
		for (String key : removedLeafAttrMap.keySet()) {
			recordOpsBuilder.removeLeaf(removedLeafAttrMap.get(key), key);
		}
		fetchImproveResult.setRootRecordOps(recordOpsBuilder.getRootRecordOps());
		return fetchImproveResult;
	}

	public static ConJunctionFGResult getConJunctionImproveResultFromKIE(FuncGroupContext context,
			ModelConJunction modelConJunction, KieSession kSession) {

		String userCode = context.getUserCode();

		String recordName = modelConJunction == null ? context.getModelCode() : modelConJunction.getRecordName();

		// 定义 全局变量
		Collection<ModelCriterion> addedCriterions = new ArrayList<>();
		Collection<String> removedCriterions = new HashSet<>();
		Collection<RightRelationJunction> addedRRJunctions = new ArrayList<>();
		Collection<String> removedRRJunctions = new HashSet<>();

		try {
			kSession.setGlobal("userCode", userCode);
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： userCode");
		}
		
		try {
			kSession.setGlobal("recordName", recordName);
		} catch (Exception e) {
			System.out.println("全局变量未设置： recordName");
		}

		try {
			kSession.setGlobal("modelConJunction", modelConJunction);
		} catch (Exception e) {
			System.out.println("全局变量未设置： rootRecord");
		}
		try {
			kSession.setGlobal("addedCriterions", addedCriterions);
		} catch (Exception e) {
			System.out.println("全局变量未设置： addedCriterions");
		}
		try {
			kSession.setGlobal("removedCriterions", removedCriterions);
		} catch (Exception e) {
			System.out.println("全局变量未设置： removedCriterions");
		}
		try {
			kSession.setGlobal("addedRRJunctions", addedRRJunctions);
		} catch (Exception e) {
			System.out.println("全局变量未设置： addedRRJunctions");
		}

		try {
			kSession.setGlobal("removedRRJunctions", removedRRJunctions);
		} catch (Exception e) {
			System.out.println("全局变量未设置： removedRRJunctions");
		}

		if (modelConJunction != null) {
			// insert object
			modelConJunction.getCriterions().forEach(criterion -> kSession.insert(criterion));
		}

		// 触发规则
		System.out.println("开始执行规则===================== ");
		int fireAllRules = kSession.fireAllRules();
		System.out.println("本次触发规则数量 =  " + fireAllRules);
		System.out.println("规则执行完毕===================== ");
		kSession.destroy();

		// 组装结果
		ConJunctionFGResult conJunctionFGResult = new ConJunctionFGResult();
		// 组装结果
		ConJunctionOpsBuilder conJunctionOpsBuilder = ConJunctionOpsBuilder.getInstance(recordName);
		conJunctionOpsBuilder.setAddedCriterion(addedCriterions);
		conJunctionOpsBuilder.setRemovedCriterion(removedCriterions);
		conJunctionOpsBuilder.setAddedRRJunction(addedRRJunctions);
		conJunctionOpsBuilder.setRemovedCriterion(removedRRJunctions);

		conJunctionFGResult.setConJunctionOps(conJunctionOpsBuilder.getRootRecordOps());
		return conJunctionFGResult;
	}

}
