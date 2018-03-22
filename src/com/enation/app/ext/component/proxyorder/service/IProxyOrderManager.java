package com.enation.app.ext.component.proxyorder.service;

import java.util.List;

import org.apache.tools.ant.filters.FixCrLfFilter.AddAsisRemove;

import com.enation.app.ext.component.proxyorder.model.ProxyOrder;

public abstract interface IProxyOrderManager 
{
	public abstract List<ProxyOrder> getAllByMemberid(int memberid);
	
	public abstract ProxyOrder get(int id);
	
	public abstract void add(ProxyOrder proxyOrder);
	
	public abstract void update(ProxyOrder proxyOrder);
	
	public abstract List<ProxyOrder> getByItemId(int ItemId);
	
	public abstract ProxyOrder getByTicketId(int ticketid);
	
	public abstract List<ProxyOrder> getVipCardOutOfTime();
}
