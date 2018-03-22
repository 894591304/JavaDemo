package com.enation.app.ext.component.proxyorder.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CheckOrderTimeTag extends BaseFreeMarkerTag{

	private INewOrderManager newOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyPayOrderTag]");
		}
		List<Order> oList = this.newOrderManager.checkOrderTime();
		int total1 = oList.size();
		for(int i=0;i<total1;i++){
			Order order = oList.get(i);
			List<OrderItem> oItemsList = this.newOrderItemsManager.getByOrderId(order.getOrder_id());
			int total2 = oItemsList.size();
			for(int t=0;t<total2;t++){
				OrderItem orderItem = oItemsList.get(t);
				Proxy proxy = this.proxyManager.get(Integer.valueOf(orderItem.getAddon()));
				proxy.setSale(proxy.getSale()-orderItem.getNum());
				this.proxyManager.edit(proxy);
			}
			order.setStatus(8);
			this.newOrderManager.update(order);
		}
		return 1;
	}

	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}

	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
