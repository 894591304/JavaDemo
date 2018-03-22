package com.enation.app.b2b2c.component.goods.service;

import java.util.Map;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;

public abstract interface ITicketDetailManager {
	public String export(Integer goods_id);
	
	public abstract int getNewKey(int goodsid);
	
	public abstract void sendKey(int id);
	
	public abstract TicketDetail get(int id);
	
	public abstract void update(TicketDetail ticketDetail);
	
	public Map<String,Object> getTicketByOrderId(int id);
}
