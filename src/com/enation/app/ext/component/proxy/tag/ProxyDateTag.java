package com.enation.app.ext.component.proxy.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.agent.service.IGoodsExtManager;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyDateTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IMemberShopManager memberShopManager;
	private IGoodsProxyManager goodsProxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Map goods = (Map)request.getAttribute("goods");
		if (goods == null) { 
			throw new TemplateModelException("调用代理商品属性标签前，必须先调用代理商品基本信息标签");
		}
		Object pid = goods.get("proxyid");
		int proxysid = Integer.parseInt(pid.toString());
		Proxy proxy = this.proxyManager.get(proxysid);
		GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
		List<Proxy> agentMemberList = this.proxyManager.getAllByGoodsid(proxy.getGoodsId());
		List aList = new ArrayList();
		int l1 = agentMemberList.size();
		int allsale = 0;
		for(int l=0;l<l1;l++){
			allsale = allsale+agentMemberList.get(l).getSale();
		}
		Goods goods2 = this.goodsProxyManager.getGoods(proxy.getGoodsId());
		int ctime = Integer.valueOf(String.valueOf(goods2.getCreate_time()));
		int r1 = ctime%3950+50;
		int mSize = agentMemberList.size();
		if(mSize==0){
		}
		for(int i=0;i<mSize;i++)
		{
			if(i<4||mSize==5)
			{
				Map tlist = new HashMap();
				MemberShop m = this.memberShopManager.getByMemberId(agentMemberList.get(i).getMemberId());
				tlist.put("memberid", String.valueOf(m.getMemberId()));
				tlist.put("memberimg", m.getMemberImg());
				aList.add(tlist);
			}
		}
		int agentNum = agentMemberList.size();
		result.put("proxyid", String.valueOf(proxy.getId()));
		result.put("goodsid", String.valueOf(proxy.getGoodsId()));
		result.put("mktprice",goodsAgent.getMktPrice());
		result.put("price", goodsAgent.getPrice());
		result.put("sale", allsale*3+r1);
		result.put("canbuy", proxy.getOnSale()-proxy.getSale());
		result.put("agentnum", agentNum);
		result.put("agentmembermap",aList);
		return result;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}



}
