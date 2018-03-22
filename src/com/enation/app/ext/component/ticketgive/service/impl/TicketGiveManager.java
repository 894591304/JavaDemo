package com.enation.app.ext.component.ticketgive.service.impl;

import java.util.List;

import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.eop.sdk.database.BaseSupport;

public class TicketGiveManager extends BaseSupport<TicketGive> implements ITicketGiveManager{

	public TicketGive getByGiveKey(String giveKey) {
		String sql = "select * from ticketgive where giveKey = ?";
		TicketGive ticketGive = this.baseDaoSupport.queryForObject(sql, TicketGive.class, new Object[]{giveKey});
		return ticketGive;
	}

	public TicketGive getByProxyOrderId(int proxyorderId) {
		String sql = "select * from ticketgive where proxyorderId = ?";
		TicketGive ticketGive = this.baseDaoSupport.queryForObject(sql, TicketGive.class, new Object[]{proxyorderId});
		return ticketGive;
	}

	public List<TicketGive> getByMemberId(int memberId) {
		String sql = "select * from ticketgive where memberId = ?";
		List<TicketGive> ticketGiveList = this.baseDaoSupport.queryForList(sql, TicketGive.class, new Object[]{memberId});
		return ticketGiveList;
	}

	public void add(TicketGive ticketGive) {
		this.baseDaoSupport.insert("ticketgive",ticketGive);		
	}

	public void edit(TicketGive ticketGive) {
		this.baseDaoSupport.update("ticketgive", ticketGive,"id = "+ticketGive.getId());		
	}

}
