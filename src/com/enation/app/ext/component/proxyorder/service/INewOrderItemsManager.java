package com.enation.app.ext.component.proxyorder.service;

import java.util.List;

import com.enation.app.shop.core.model.OrderItem;


public abstract interface INewOrderItemsManager 
{
	public abstract List<OrderItem> getByOrderId(int orderid);
	
	public abstract OrderItem get(int itemid);
	
	public abstract List<OrderItem> getByProxyId(int proxyid);
}
