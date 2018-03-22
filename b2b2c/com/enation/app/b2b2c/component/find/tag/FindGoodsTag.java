package com.enation.app.b2b2c.component.find.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class FindGoodsTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IMemberShopManager memberShopManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Map goods = (Map)request.getAttribute("goods");
		if (goods == null) { 
			throw new TemplateModelException("调用代理商品属性标签前，必须先调用代理商品基本信息标签");
		}
		Object gid = goods.get("goods_id");
		int goodsid = Integer.parseInt(gid.toString());
		List<Proxy> agentMemberList = this.proxyManager.getAgentMember(goodsid);
		List plist = new ArrayList();
		int mSize = agentMemberList.size();	
		if(mSize==0){
		}
		for(int i=0;i<mSize;i++)
		{
			if(i<4||mSize==5)
			{
				Map agentMemberInfoMap = new HashMap();
				agentMemberInfoMap.put("memberid", String.valueOf(this.memberShopManager.getByMemberId(agentMemberList.get(i).getMemberId()).getMemberId()));
				agentMemberInfoMap.put("memberimg", this.memberShopManager.getByMemberId(agentMemberList.get(i).getMemberId()).getMemberImg());
				plist.add(agentMemberInfoMap);
			}
		}
		result.put("agentnum", mSize);
		result.put("agentmembermap",plist);
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

}
