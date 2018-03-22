package com.enation.app.b2b2c.component.goods.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.goods.model.GoodsTicket;
import com.enation.framework.database.Page;

public abstract interface IGoodsTicketManager {
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void add(GoodsTicket ticket);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void edit(GoodsTicket ticket);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract GoodsTicket get(Integer id);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void delete(Integer id);
	
	public abstract Page searchGoodsTicket(Map ticketMap, Integer page, Integer pageSize, String other, String order);
	
	public void createTicket(Integer goods_id, String pre, Integer num, Long startdate, Long enddate);
	
}
