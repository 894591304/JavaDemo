package com.enation.app.ext.component.collect.model;

import java.io.Serializable;

public class Collect implements Serializable
{
	private int id;
	private int memberId;
	private int proxyId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getProxyId() {
		return proxyId;
	}
	public void setProxyId(int proxyId) {
		this.proxyId = proxyId;
	}
	
}
