package com.enation.app.ext.component.proxyorder.plugin;

import java.util.List;

import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;

public class CheckMyOrderTicket {

	private INewOrderManager newOrderManager;
	private IProxyOrderManager proxyOrderManager;
	private INewOrderItemsManager newOrderItemsManager;
	private ITicketDetailManager tIcketDetailManager;
	
	public void checkMyTicket(int memberid){
		List<Order> oList = this.newOrderManager.getByMemberid(memberid);
		int count = oList.size();
		for(int i=0;i<count;i++){
			Order o = oList.get(i);
			if(o.getPay_status()==2){
				List<OrderItem> oiList = this.newOrderItemsManager.getByOrderId(o.getOrder_id());
				int count2 = oiList.size();
				for(int j=0;j<count;j++){
					OrderItem oi = oiList.get(j);
					if(this.proxyOrderManager.getByItemId(oi.getItem_id()).size()!=oi.getNum()){
						addTicket(memberid,oi.getItem_id(),oi.getNum()-this.proxyOrderManager.getByItemId(oi.getItem_id()).size());
					}
				}
			}
		}		
	}
	
	public void addTicket(int memberid,int itemid,int num){
		OrderItem oi = this.newOrderItemsManager.get(itemid);
		for(int i=0;i<num;i++){
			int ticketid = this.tIcketDetailManager.getNewKey(oi.getGoods_id());
			if(ticketid!=0){
				ProxyOrder po = new ProxyOrder();
				po.setMemberId(memberid);
				po.setItemId(itemid);
				po.setTicketId(ticketid);
				po.setStatus(0);
				this.proxyOrderManager.add(po);
			}else{
				System.out.println("兑换码不足！");
			}
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
	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}
	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}
	
	
}
