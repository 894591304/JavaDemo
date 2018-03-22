package com.enation.app.ext.component.proxyorder.service;

import java.util.List;

import com.enation.app.shop.core.model.Order;

public abstract interface INewOrderManager 
{
	public abstract Order getById(int id);
	
	public abstract Order getBySnAndMemberid(String sn,int memberid );
	
	public abstract List<Order> getByMemberid(int memberid);
	
	public abstract List<Order> getNotPayOrder(int memberid);
	
	public abstract Order getBySn(String sn);
	
	public abstract void update(Order order);
	
	public abstract List<Order> checkOrderTime();
	
	public abstract List<Order> getAll();
}
