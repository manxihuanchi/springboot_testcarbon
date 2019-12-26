package cho.carbon.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import cho.carbon.dto.CarbonParam;

//@FeignClient(value = "SPRINGBOOT-PROVIDER")
public interface CarbonClientService {

	@RequestMapping(value = "/dxjde2020bnb/getCriterions")
	public String getCriterions(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/runningAfterCodeQuery")
	public boolean runningAfterCodeQuery();

	@RequestMapping(value = "/dxjde2020bnb/afterCheck")
	public String afterCheck(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/thirdImprove")
	public String thirdImprove(@RequestBody CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/secondImprove")
	public String secondImprove(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/needImprove")
	public boolean needImprove( CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/postImprove")
	public String postImprove(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/improveEveryTime")
	public boolean improveEveryTime();

	@RequestMapping(value = "/dxjde2020bnb/preImprove")
	public String preImprove(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/improve")
	public String improve(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/improveOnlyCorrelativeRelation")
	public boolean improveOnlyCorrelativeRelation();
	@RequestMapping(value = "/dxjde2020bnb/afterFusition")
	public boolean afterFusition(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/fetchImprove")
	public String fetchImprove(CarbonParam carbonParam);

	@RequestMapping(value = "/dxjde2020bnb/junctionImprove")
	public String junctionImprove(CarbonParam carbonParam);

}
