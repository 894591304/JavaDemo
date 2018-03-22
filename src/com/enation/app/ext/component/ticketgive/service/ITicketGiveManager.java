package com.enation.app.ext.component.ticketgive.service;

import java.util.List;

import com.enation.app.ext.component.ticketgive.model.TicketGive;

public abstract interface ITicketGiveManager 
{
	public abstract void add(TicketGive ticketGive);
	
	public abstract TicketGive getByGiveKey(String giveKey);
	
	public abstract TicketGive getByProxyOrderId(int proxyorderId);
	
	public abstract List<TicketGive> getByMemberId(int memberId);
	
	public abstract void edit(TicketGive ticketGive);
}
