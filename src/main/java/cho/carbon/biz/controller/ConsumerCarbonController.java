package cho.carbon.biz.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cho.carbon.complexus.FGRecordComplexus;
import cho.carbon.context.fg.FuncGroupContext;
import cho.carbon.context.hc.HCFusionContext;
import cho.carbon.dto.CarbonParam;
import cho.carbon.entity.entity.Entity;
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
import cho.carbon.fuse.fg.ThirdRoundImproveFuncGroup;
import cho.carbon.message.Message;
import cho.carbon.meta.criteria.model.ModelConJunction;
import cho.carbon.meta.criteria.model.ModelCriterion;
import cho.carbon.ops.complexus.OpsComplexus;
import cho.carbon.panel.Discoverer;
import cho.carbon.panel.Integration;
import cho.carbon.panel.IntegrationMsg;
import cho.carbon.panel.PanelFactory;
import cho.carbon.rrc.record.FGRootRecord;
import cho.carbon.service.CarbonClientService;

@RestController
public class ConsumerCarbonController {

//	@Autowired
	CarbonClientService carbonClientService;
	
	@RequestMapping("/consumer/query")
	public String query() {
//		Person person = new Person();
//		person.setUsername("网络");
//		person.setPassword("345345dsfge");
//		
//		boolean runningAfterCodeQuery = carbonClientService.runningAfterCodeQuery(person);
//		
//		System.out.println("");
		return "师傅手动";
	}
	
	@RequestMapping("/consumer/carbon")
	public String get() {
		System.out.println();	
			
			readData();
			
			return "啦啦啦";
		}
	
	
	public void readData() {
		
		long startTime = System.currentTimeMillis();
		HCFusionContext context=new HCFusionContext();
		context.setSource(HCFusionContext.SOURCE_COMMON);

		context.setStrucTitle("人口信息");
		context.setUserCode("e10adc3949ba59abbe56e057f28888d5");
		
//		context.getFunctionGroupContainer().putFunctionGroup("DXJDE2020",new PeopleBnb());
		
		Integration integration=PanelFactory.getIntegration();
		Entity entity=createEntity();
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

	private Entity createEntity() {
		Entity entity = new Entity("人口信息");
		//entity.putValue("唯一编码", "d0e2eb99c6c34aeaa9e66c893afe4b89");
		entity.putValue("姓名", "测试aa453");
//		entity.putValue("身份证号码", "110101199003077598");
		return entity;
	}
	
	class PeopleBnb implements FunctionGroup, IdentityQueryFuncGroup, CheckFuncGroup, ThirdRoundImproveFuncGroup,
	FuseCallBackFuncGroup, FetchFuncGroup,QueryJunctionFuncGroup {

		public PeopleBnb() {
			System.out.println("创建了。。。 PeopleBnb");
		}
		@Override
		public boolean runningAfterCodeQuery() {
			
			return carbonClientService.runningAfterCodeQuery();
		}
		
			
		@Override
		public ImproveFGResult secondImprove(FuncGroupContext funcGroupContext, String record, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(record);
			
			return FGOSerializableFactory.des2ImproveFGResult(carbonClientService.secondImprove(carbonParam));
		}

		@Override
		public ImproveFGResult improve(FuncGroupContext funcGroupContext, String recordCode, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			
			return FGOSerializableFactory.des2ImproveFGResult(carbonClientService.improve(carbonParam));
			
		}
		
		@Override
		public boolean improveOnlyCorrelativeRelation() {
			// TODO Auto-generated method stub
			return carbonClientService.improveOnlyCorrelativeRelation();
		}
		
		@Override
		public boolean needImprove(String recordCode, OpsComplexus opsComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setRecordCode(recordCode);
			carbonParam.setOpsComplexus(opsComplexus.serialize());
			return carbonClientService.needImprove(carbonParam);
		}
		
		@Override
		public boolean improveEveryTime() {
			// TODO Auto-generated method stub
			return carbonClientService.improveEveryTime();
		}

		@Override
		public ImproveFGResult postImprove(FuncGroupContext funcGroupContext, String recordCode, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			return FGOSerializableFactory.des2ImproveFGResult(carbonClientService.postImprove(carbonParam));
		}

		@Override
		public ImproveFGResult preImprove(FuncGroupContext funcGroupContext, String recordCode, OpsComplexus opsComplexus, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			carbonParam.setOpsComplexus(opsComplexus.serialize());
			
			return FGOSerializableFactory.des2ImproveFGResult(carbonClientService.preImprove(carbonParam));
		}

		@Override
		public ConJunctionFGResult junctionImprove(FuncGroupContext funcGroupContext, ModelConJunction modelConJunction) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setModelConJunction(modelConJunction.serialize());
			
			return FGOSerializableFactory.des2ConJunctionFGResult(carbonClientService.junctionImprove(carbonParam));
		}

		@Override
		public FetchFGResult fetchImprove(FuncGroupContext funcGroupContext, FGRootRecord record) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setfGRootRecord(record.serialize());
			 
			 return FGOSerializableFactory.des2FetchFGResult( carbonClientService.fetchImprove(carbonParam));
		}

		@Override
		public boolean afterFusition(FuncGroupContext funcGroupContext, String recordCode) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			
			return  carbonClientService.afterFusition(carbonParam);
		}

		@Override
		public ImproveFGResult thirdImprove(FuncGroupContext funcGroupContext, String recordCode, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			
			return FGOSerializableFactory.des2ImproveFGResult(carbonClientService.thirdImprove(carbonParam));
		}

		@Override
		public CheckFGResult afterCheck(FuncGroupContext funcGroupContext, String recordCode, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setFuncGroupContext(funcGroupContext.serialize());
			carbonParam.setRecordCode(recordCode);
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());

			return FGOSerializableFactory.des2CheckFGResult(carbonClientService.afterCheck(carbonParam));
		}

		@Override
		public Collection<ModelCriterion> getCriterions(String recordCode, FGRecordComplexus fGRecordComplexus) {
			CarbonParam carbonParam = new CarbonParam();
			carbonParam.setRecordCode(recordCode);
			carbonParam.setfGRecordComplexus(fGRecordComplexus.serialize());
			
			return FGOSerializableFactory.des2Criterions(carbonClientService.getCriterions(carbonParam));
		}

	}

	
}
