package com.enation.app.ext.component.proxyorder.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.eop.sdk.database.BaseSupport;

public class NewOrderManager extends BaseSupport<Order> implements INewOrderManager{

	public Order getById(int id) {
		String sql = "select * from order where order_id = ?";
		Order order = this.baseDaoSupport.queryForObject(sql, Order.class, new Object[]{id});
		return order;
	}

	
	public Order getBySnAndMemberid(String sn, int memberid) {
		String sql = "select * from order where sn = ? and member_id = ?";
		Order order = this.baseDaoSupport.queryForObject(sql, Order.class, new Object[]{sn,memberid});
		return order;
	}


	public List<Order> getByMemberid(int memberid) {
		String sql = "select * from order where member_id = ?";
		List<Order> oList = this.baseDaoSupport.queryForList(sql, Order.class, new Object[]{memberid});
		return oList;
	}
	
	public List<Order> getNotPayOrder(int memberid){
		String sql = "select * from order where member_id = ? and pay_status = 0 order by order_id desc";
		List<Order> oList = this.baseDaoSupport.queryForList(sql,Order.class, new Object[]{memberid});
		return oList;
	}


	
	public Order getBySn(String sn) {
		String sql = "select * from order where sn = ?";
		Order order = this.baseDaoSupport.queryForObject(sql, Order.class, new Object[]{sn});
		return order;
	}


	public void update(Order order) {
		String sql = "select count(*) from order where order_id = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{order.getOrder_id()});
		if(count!=0){
			this.baseDaoSupport.update("order",order,"order_id = "+order.getOrder_id());
		}
	}



	public List<Order> checkOrderTime() {
		String sql = "select * from order where create_time < ? and pay_status = 0 and status = 0";
		long time = System.currentTimeMillis()/1000-60*10;
		List<Order> oList = this.baseDaoSupport.queryForList(sql, Order.class, new Object[]{time});
		return oList;
	}


	@Override
	public List<Order> getAll() {
		String sql = "select * from order";
		List<Order> oList = this.baseDaoSupport.queryForList(sql, Order.class, new Object[]{});
		return oList;
	}


}
