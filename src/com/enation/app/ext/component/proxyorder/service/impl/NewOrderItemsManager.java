package com.enation.app.ext.component.proxyorder.service.impl;

import java.util.List;

import com.enation.app.ext.component.proxyorder.service.INewOrderItemsManager;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.eop.sdk.database.BaseSupport;

public class NewOrderItemsManager extends BaseSupport<OrderItem> implements INewOrderItemsManager
{

	public List<OrderItem> getByOrderId(int orderid) {
		String sql = "select * from order_items where order_id = ?";
		List<OrderItem> oList= this.baseDaoSupport.queryForList(sql, OrderItem.class,new Object[]{orderid});
		return oList;
	}

	public OrderItem get(int itemid) {
		String sql = "select * from order_items where item_id = ?";
		OrderItem oi = this.baseDaoSupport.queryForObject(sql, OrderItem.class, new Object[]{itemid});
		return oi;
	}

	
	public List<OrderItem> getByProxyId(int proxyid) {
		String sql = "select * from order_items where addon = ?";
		List<OrderItem> oList = this.baseDaoSupport.queryForList(sql,OrderItem.class,new Object[]{proxyid});
		return oList;
	}
	
	

}
