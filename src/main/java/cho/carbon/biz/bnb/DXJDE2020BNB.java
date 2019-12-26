package cho.carbon.biz.bnb;

import java.util.Collection;

import cho.carbon.biz.common.KIEHelper;
import cho.carbon.biz.common.KieSessionFactory;
import cho.carbon.complexus.FGRecordComplexus;
import cho.carbon.context.fg.FuncGroupContext;
import cho.carbon.fuse.fg.CheckFGResult;
import cho.carbon.fuse.fg.CheckFuncGroup;
import cho.carbon.fuse.fg.ConJunctionFGResult;
import cho.carbon.fuse.fg.FetchFGResult;
import cho.carbon.fuse.fg.FetchFuncGroup;
import cho.carbon.fuse.fg.FunctionGroup;
import cho.carbon.fuse.fg.FuseCallBackFuncGroup;
import cho.carbon.fuse.fg.IdentityQueryFuncGroup;
import cho.carbon.fuse.fg.ImproveFGResult;
import cho.carbon.fuse.fg.QueryJunctionFuncGroup;
import cho.carbon.fuse.fg.ThirdRoundImproveFuncGroup;
import cho.carbon.meta.criteria.model.ModelConJunction;
import cho.carbon.meta.criteria.model.ModelCriterion;
import cho.carbon.ops.complexus.OpsComplexus;
import cho.carbon.rrc.record.FGRootRecord;

//@Component(value = "DXJDE2020")
public class DXJDE2020BNB implements FunctionGroup, IdentityQueryFuncGroup, CheckFuncGroup, ThirdRoundImproveFuncGroup,
FuseCallBackFuncGroup, FetchFuncGroup,QueryJunctionFuncGroup {

	@Override
	public  ImproveFGResult preImprove(FuncGroupContext context, String recordCode, OpsComplexus opsComplexus,
			FGRecordComplexus recordComplexus) {
		return KIEHelper.getImproveFGResultFromKIE(context, recordCode, opsComplexus, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-f1-pre"));
	}

	@Override
	public ImproveFGResult improve(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
		return KIEHelper.getImproveFGResultFromKIE(context, recordCode, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-f2-imp"));

	}

	@Override
	public boolean afterFusition(FuncGroupContext context,String recordCode) {

		return false;
	}

	@Override
	public ImproveFGResult postImprove(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
		return KIEHelper.getImproveFGResultFromKIE(context, recordCode, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-f3-post"));
	}

	@Override
	public ImproveFGResult secondImprove(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
		return KIEHelper.getImproveFGResultFromKIE(context, recordCode, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-imp-second"));
	}

	@Override
	public ImproveFGResult thirdImprove(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
		return KIEHelper.getImproveFGResultFromKIE(context, recordCode, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-imp-third"));
	}

	@Override
	public CheckFGResult afterCheck(FuncGroupContext context, String recordCode, FGRecordComplexus recordComplexus) {
		
		return null;
	}

	@Override
	public Collection<ModelCriterion> getCriterions(String recordCode, FGRecordComplexus recordComplexus) {
		return KIEHelper.getBizCriteriaListFromKIE(recordCode, recordComplexus,
				KieSessionFactory.findScannerSession("ks-dxjde2020-identity-query"));
	}

	@Override
	public FetchFGResult fetchImprove(FuncGroupContext context, FGRootRecord record) {
		return KIEHelper.getFetchImproveResultFromKIE(context, record,
				KieSessionFactory.findScannerSession("ks-dxjde2020-imp-fetch"));
	}

	@Override
	public ConJunctionFGResult junctionImprove(FuncGroupContext context, ModelConJunction modelConJunction) {
		return KIEHelper.getConJunctionImproveResultFromKIE(context, modelConJunction,
				KieSessionFactory.findScannerSession("ks-dxjde2020-query-conjunction"));
	}

}
