package com.enation.app.ext.component.proxyorder.plugin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.plugin.order.IOrderPayEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;


@Component
public class ProxyOrderCreatePlugin extends AutoRegisterPlugin implements IOrderPayEvent{

	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private ITicketDetailManager tIcketDetailManager;
	
	public void pay(Order order, boolean isOnline) {
		int orderid = order.getOrder_id();
		int memberId = order.getMember_id();
		List<OrderItem> oList = this.newOrderItemsManager.getByOrderId(orderid);
		int count = oList.size();
		for(int i=0;i<count;i++){
			ProxyOrder proxyOrder = new ProxyOrder();
			int itemId = oList.get(i).getItem_id();
			int ticketId = this.tIcketDetailManager.getNewKey(oList.get(i).getGoods_id());
			proxyOrder.setMemberId(memberId);
			proxyOrder.setItemId(itemId);
			proxyOrder.setTicketId(ticketId);
			proxyOrder.setStatus(0);
			this.proxyOrderManager.add(proxyOrder);
		}
	}

	public IProxyOrderManager getProxyOrderManager() {
		return proxyOrderManager;
	}

	public void setProxyOrderManager(IProxyOrderManager proxyOrderManager) {
		this.proxyOrderManager = proxyOrderManager;
	}

	public INewOrderItemsManager getNewOrderItemsManager() {
		return newOrderItemsManager;
	}

	public void setNewOrderItemsManager(INewOrderItemsManager newOrderItemsManager) {
		this.newOrderItemsManager = newOrderItemsManager;
	}

	public ITicketDetailManager gettIcketDetailManager() {
		return tIcketDetailManager;
	}

	public void settIcketDetailManager(ITicketDetailManager tIcketDetailManager) {
		this.tIcketDetailManager = tIcketDetailManager;
	}

}
