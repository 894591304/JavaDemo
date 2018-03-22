package com.enation.app.ext.component.goodsdiscountticketdetail.model;

import java.io.Serializable;

public class GoodsDiscountTicketDetail implements Serializable{
	
	private int id;
	private int goodsId;
	private int proxyId;
	private int proxyMemberId;
	private float ticketValue;
	private String discountTicketId;
	private int giveStatus;
	private String sendKey;
	private int sendStatus;
	private int useStatus;
	private int useOrderItemId;
	private int belongMemberId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getProxyId() {
		return proxyId;
	}
	public void setProxyId(int proxyId) {
		this.proxyId = proxyId;
	}
	public int getProxyMemberId() {
		return proxyMemberId;
	}
	public void setProxyMemberId(int proxyMemberId) {
		this.proxyMemberId = proxyMemberId;
	}
	public float getTicketValue() {
		return ticketValue;
	}
	public void setTicketValue(float ticketValue) {
		this.ticketValue = ticketValue;
	}
	public String getDiscountTicketId() {
		return discountTicketId;
	}
	public void setDiscountTicketId(String discountTicketId) {
		this.discountTicketId = discountTicketId;
	}
	public int getGiveStatus() {
		return giveStatus;
	}
	public void setGiveStatus(int giveStatus) {
		this.giveStatus = giveStatus;
	}
	public String getSendKey() {
		return sendKey;
	}
	public void setSendKey(String sendKey) {
		this.sendKey = sendKey;
	}
	public int getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}
	public int getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public int getBelongMemberId() {
		return belongMemberId;
	}
	public void setBelongMemberId(int belongMemberId) {
		this.belongMemberId = belongMemberId;
	}
	public int getUseOrderItemId() {
		return useOrderItemId;
	}
	public void setUseOrderItemId(int useOrderItemId) {
		this.useOrderItemId = useOrderItemId;
	}
	
}
