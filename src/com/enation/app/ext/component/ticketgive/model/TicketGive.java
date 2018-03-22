package com.enation.app.ext.component.ticketgive.model;

import java.io.Serializable;

public class TicketGive implements Serializable
{
	private int id;
	private int proxyorderId;
	private String giveKey;
	private int memberId;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProxyorderId() {
		return proxyorderId;
	}
	public void setProxyorderId(int proxyorderId) {
		this.proxyorderId = proxyorderId;
	}
	public String getGiveKey() {
		return giveKey;
	}
	public void setGiveKey(String giveKey) {
		this.giveKey = giveKey;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
