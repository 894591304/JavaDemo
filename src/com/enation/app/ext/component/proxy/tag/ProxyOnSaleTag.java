package com.enation.app.ext.component.proxy.tag;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class ProxyOnSaleTag extends BaseFreeMarkerTag{

	
	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	private IGoodsAgentManager goodsAgentManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[ProxyOnSaleTag]");
		}
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Map goods = (Map)request.getAttribute("goods");
		if (goods == null) { 
			throw new TemplateModelException("调用商品属性标签前，必须先调用商品基本信息标签");
		}
		Object gid = goods.get("goods_id");
		int goodsid = Integer.parseInt(gid.toString());
		if(this.proxyManager.checkProxy(goodsid, member.getMember_id())==-1){
			throw new TemplateModelException("您代理的商品已过代理期或商品已售光！");
		}
		if(this.proxyManager.checkProxy(goodsid, member.getMember_id())==0){
			throw new TemplateModelException("您未代理此商品！");
		}
		Proxy proxy = this.proxyManager.getForOnSale(goodsid, member.getMember_id());
		GoodsAgent goodsAgent = this.goodsAgentManager.get(goodsid);
		long dxsq = (Long.valueOf(proxy.getProxyEndTime())-Long.valueOf(proxy.getProxyBeginTime()))/60/60/24;
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		df.format(Long.valueOf(proxy.getProxyBeginTime())*1000);
		String huohao = "HJYX"+String.valueOf((proxy.getId()+10000000));
		result.put("goodsid", String.valueOf(proxy.getGoodsId()));
		result.put("huohao", huohao);
		result.put("goodsamount", proxy.getGoodsAmount());
		result.put("canonsale", proxy.getGoodsAmount()-proxy.getOnSale());
		result.put("frozencredit", proxy.getFrozenCredit());
		result.put("frozendeposit", proxy.getFrozenDeposit());
		result.put("dxsq", dxsq);
		result.put("begintime",df.format(Long.valueOf(proxy.getProxyBeginTime())*1000));
		result.put("testtime", df.format(Long.valueOf(proxy.getProxyTestTime())*1000));
		result.put("levelname", this.userAccountManager.getLevelName(member.getMember_id()));
		result.put("mktprice",goodsAgent.getMktPrice());
		result.put("price", goodsAgent.getPrice());
		result.put("status", proxy.getStatus());
		return result;
	}


	public IProxyManager getProxyManager() {
		return proxyManager;
	}


	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}


	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}


	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}


	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}


	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

}
