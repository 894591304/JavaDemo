package com.enation.app.ext.component.proxyorder.service.impl;

import java.util.List;

import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ProxyOrderManager extends BaseSupport<ProxyOrder> implements IProxyOrderManager{

	
	public List<ProxyOrder> getAllByMemberid(int memberid) {
		String sql = "select * from proxyorder where memberId = ? order by id DESC";
		List<ProxyOrder> pList = this.baseDaoSupport.queryForList(sql,ProxyOrder.class,new Object[]{memberid});
		return pList;
	}

	
	public ProxyOrder get(int id) {
		String sql = "select * from proxyorder where id = ?";
		ProxyOrder proxyOrder = this.baseDaoSupport.queryForObject(sql, ProxyOrder.class,new Object[]{id});
		return proxyOrder;
	}

	
	public void add(ProxyOrder proxyOrder) {
		this.baseDaoSupport.insert("proxyorder",proxyOrder);
	}

	
	public void update(ProxyOrder proxyOrder) {
		this.baseDaoSupport.update("proxyorder", proxyOrder,"id = "+proxyOrder.getId());		
	}


	public List<ProxyOrder> getByItemId(int ItemId) {
		String sql = "select * from proxyorder where itemId = ?";
		List<ProxyOrder> pList = this.baseDaoSupport.queryForList(sql, ProxyOrder.class, new Object[]{ItemId});
		return pList;
	}


	public ProxyOrder getByTicketId(int ticketid) {
		String sql = "select * from proxyorder where ticketId = ?";
		ProxyOrder proxyOrder = this.baseDaoSupport.queryForObject(sql, ProxyOrder.class,new Object[]{ticketid});
		return proxyOrder;
	}


	public List<ProxyOrder> getVipCardOutOfTime() {
		String sql = "select * from proxyorder where goodsId=99999999 or goodsId=9999998 or goodsId=99999997 and soldTime < ?";
		long time = System.currentTimeMillis()/1000;
		time = time-10*24*60*60;
		List<ProxyOrder> pList = this.baseDaoSupport.queryForList(sql,ProxyOrder.class,new Object[]{time});
		return pList;
	}

}
