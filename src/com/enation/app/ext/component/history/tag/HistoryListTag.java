package com.enation.app.ext.component.history.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.collect.service.ICollectManager;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.history.model.History;
import com.enation.app.ext.component.history.service.IHistoryManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class HistoryListTag extends BaseFreeMarkerTag{

	private IGoodsProxyManager goodsProxyManager;
	private IProxyManager proxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IHistoryManager historyManager;
	private ICollectManager collectManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[HistoryListTag]");
		}
		Map result = new HashMap();
		List<History> historyList = this.historyManager.getAllByMemberid(member.getMember_id());
		int total = historyList.size();
		if(total>10){
			total=10;
		}
		List hList = new ArrayList();
		for(int i=0;i<total;i++){
			Map pMap = new HashMap();
			History history = historyList.get(i);
			Goods goods = this.goodsProxyManager.getGoods(history.getGoodsId());
			List<Proxy> agentMemberList = this.proxyManager.getAllByGoodsid(history.getGoodsId());
			List aList = new ArrayList();
			int l1 = agentMemberList.size();
			int allsale = 0;
			for(int l=0;l<l1;l++){
				allsale = allsale+agentMemberList.get(l).getSale();
			}
			int ctime = Integer.valueOf(String.valueOf(goods.getCreate_time()));
			int r1 = ctime%3950+50;
			if(goods!=null){
				Proxy proxy = this.proxyManager.get(history.getProxyId());
				int count = this.collectManager.getAllCollect(history.getProxyId());
				pMap.put("proxyid",String.valueOf(history.getProxyId()));
				pMap.put("goodsimg",goods.getOriginal());
				pMap.put("goodsname",goods.getName());
				pMap.put("Mktprice",goods.getMktprice());
				pMap.put("price",goods.getPrice());
				pMap.put("sold",allsale*3+r1);
				pMap.put("count",count);
				hList.add(pMap);
			}
		}
		result.put("total",total);
		result.put("hlist",hList);
		return result;
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

	public IHistoryManager getHistoryManager() {
		return historyManager;
	}

	public void setHistoryManager(IHistoryManager historyManager) {
		this.historyManager = historyManager;
	}

	public ICollectManager getCollectManager() {
		return collectManager;
	}

	public void setCollectManager(ICollectManager collectManager) {
		this.collectManager = collectManager;
	}

}
