package com.enation.app.ext.component.proxy.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.ext.core.service.IMemberExtManager;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyMemberShopAllTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsProxyManager goodsProxyManager;
	
	protected Object exec(Map params) throws TemplateModelException {
		Map result = new HashMap();
		HttpServletRequest request = getRequest();
		Integer shopid = (Integer)request.getAttribute("shopid");
		List<Proxy> pList = this.proxyManager.getAllCanBuyByMemberid(shopid);
		List proxyGoods = new ArrayList();
		int total = pList.size();
		if(total%2==0){
			for(int i=0;i<total;i++){
				Map tlist = this.goodsProxyManager.setGoodsInfoIntoProxyList(pList.get(i).getGoodsId(),i%2);
				tlist.put("proxyid", String.valueOf(pList.get(i).getId()));
				proxyGoods.add(tlist);
				}
		}else{
			for(int i=0;i<total;i++){
				if(i==total-1)
				{
					Map tlist = this.goodsProxyManager.setGoodsInfoIntoProxyList(pList.get(i).getGoodsId(),2);
					tlist.put("proxyid", String.valueOf(pList.get(i).getId()));
					proxyGoods.add(tlist);
				}else{
					Map tlist = this.goodsProxyManager.setGoodsInfoIntoProxyList(pList.get(i).getGoodsId(),i%2);
					tlist.put("proxyid", String.valueOf(pList.get(i).getId()));
					proxyGoods.add(tlist);
				}
			}
		}
		result.put("total", total);
		result.put("pglist", proxyGoods);
		return result;
	}

	
	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}


	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}


	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}


}
