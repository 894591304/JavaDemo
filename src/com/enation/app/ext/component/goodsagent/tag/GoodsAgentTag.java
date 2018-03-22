package com.enation.app.ext.component.goodsagent.tag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GoodsAgentTag extends BaseFreeMarkerTag{

	private ICreditManager creditManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyManager proxyManager;
	private IAgentManager agentManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[GoodsAgentTag]");
		}
		if(this.creditManager.checkReviewByMemberId(member.getMember_id())!=1){
			throw new TemplateModelException("未申请授信或授信未通过不能使用此标签[UserMsgTag]");
		}
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Map goods = (Map)request.getAttribute("goods");
		if (goods == null) { 
			throw new TemplateModelException("调用商品属性标签前，必须先调用商品基本信息标签");
		}
		Object goodsId = goods.get("goods_id");
		int id = Integer.parseInt(goodsId.toString());
		GoodsAgent goodsAgent = this.goodsAgentManager.get(id);
		String timeOption = goodsAgent.getTimeOption();
		String[] tOption = timeOption.split("/");
		int tolength =tOption.length;
		List timeList = new ArrayList();
		for(int i=0;i<tolength;i++){
			Map tMap = new HashMap();
			tMap.put("time",tOption[i]);
			timeList.add(tMap);
		}
		result.put("agentname",this.agentManager.get(this.goodsProxyManager.getAgentid(id)).getName());
		List agentInfo = this.goodsAgentManager.getByGoodsId(id);
		result.put("store",this.goodsProxyManager.getGoods(id).getStore());
		result.put("onsale",this.proxyManager.getAllAgentNumByGoodsid(id));
		result.put("level",this.userAccountManager.getLevel(member.getMember_id()) );
		result.put("remain",this.userAccountManager.getRemainCredit(member.getMember_id()));
		result.put("goodsAgent",agentInfo);
		result.put("memberName",this.creditManager.get(member.getMember_id()).getName());
		result.put("idCard", this.creditManager.get(member.getMember_id()).getIdCard());
		result.put("mobile", this.creditManager.get(member.getMember_id()).getMobile());
		result.put("accountId",this.userAccountManager.getAccountId(member.getMember_id()) );
		result.put("timeList",timeList);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		result.put("signTime", df.format(date));
		return result;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}
	
}
