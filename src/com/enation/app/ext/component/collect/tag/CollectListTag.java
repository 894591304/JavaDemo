package com.enation.app.ext.component.collect.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.collect.model.Collect;
import com.enation.app.ext.component.collect.service.ICollectManager;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CollectListTag extends BaseFreeMarkerTag{

	private ICollectManager collectManager;
	private IGoodsProxyManager goodsProxyManager;
	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[CollectListTag]");
		}
		Map result = new HashMap();
		List<Collect> cList = this.collectManager.getAllByMemberid(member.getMember_id());
		int total = cList.size();
		List ctList = new ArrayList();
		for(int i=0;i<total;i++)
		{
			Map pMap = new HashMap();
			Collect collect = cList.get(i);
			Proxy proxy = this.proxyManager.get(collect.getProxyId());
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			int count = this.collectManager.getAllCollect(collect.getProxyId());
			pMap.put("proxyid",String.valueOf(collect.getProxyId()));
			pMap.put("goodsimg",goods.getOriginal());
			pMap.put("goodsname",goods.getName());
			pMap.put("Mktprice",goods.getMktprice());
			pMap.put("price",goods.getPrice());
			pMap.put("sold",proxy.getSale());
			pMap.put("count",count);
			ctList.add(pMap);
		}
		result.put("total",total);	
		result.put("ctList",ctList);
		return result;
	}

	public ICollectManager getCollectManager() {
		return collectManager;
	}

	public void setCollectManager(ICollectManager collectManager) {
		this.collectManager = collectManager;
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

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

}
