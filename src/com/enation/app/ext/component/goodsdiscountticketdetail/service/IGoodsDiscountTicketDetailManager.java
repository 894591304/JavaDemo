package com.enation.app.ext.component.goodsdiscountticketdetail.service;

import java.util.List;

import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;

public abstract interface IGoodsDiscountTicketDetailManager 
{
	public abstract void add(GoodsDiscountTicketDetail goodsDiscountTicketDetail);
	
	public abstract void update (GoodsDiscountTicketDetail goodsDiscountTicketDetail);
	
	public abstract GoodsDiscountTicketDetail get(int id);
	
	public abstract List<GoodsDiscountTicketDetail> getByProxyId(int proxyid);
	
	public abstract List<GoodsDiscountTicketDetail> getByProxyIdAndTicketValue(int proxyid,float value);
	
	public abstract List<GoodsDiscountTicketDetail> getNotGiveByProxyId(int proxyid);
	
	public abstract List<GoodsDiscountTicketDetail> getByMemberId(int memberid);
	
	public abstract GoodsDiscountTicketDetail getBySendKey(String sendkey);
	
	public abstract GoodsDiscountTicketDetail getHignValueByProxyid(int proxyid,int memberid);
	
	public abstract GoodsDiscountTicketDetail getByItemId(int itemid);
	
	public abstract  List<GoodsDiscountTicketDetail> getSaleProxy(int proxyid);
	
	public abstract int getTicketNum(int proxyid);
}
