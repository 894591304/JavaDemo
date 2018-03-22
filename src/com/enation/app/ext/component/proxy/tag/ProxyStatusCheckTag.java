package com.enation.app.ext.component.proxy.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyStatusCheckTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IGoodsProxyManager goodsProxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[ProxyStatusCheckTag]");
		}
		List<Proxy> pList = this.proxyManager.getAllByMemberid(member.getMember_id());
		int total = pList.size();
		for(int i=0;i<total;i++){
			Proxy proxy = pList.get(i);
			String testTime = proxy.getProxyTestTime();
			Long tTime = Long.valueOf(testTime);
			Long nTime = System.currentTimeMillis()/1000;
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			if(goods.getDisabled()==1&&proxy.getStatus()!=0&&proxy.getStatus()!=5){
				proxy.setStatus(5);
				proxy.setProxyTestTime(String.valueOf(Integer.valueOf(String.valueOf(System.currentTimeMillis()/1000))));
				this.proxyManager.edit(proxy);
			}			
			if(nTime>tTime&&proxy.getStatus()!=0&&proxy.getStatus()!=5){
				proxy.setStatus(5);
				this.proxyManager.edit(proxy);
			}
		}		
		return 1;
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
