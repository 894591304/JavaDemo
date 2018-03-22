package com.enation.app.ext.component.proxycart.model;

import java.io.Serializable;

public class ProxyCart implements Serializable
{
	private int id;
	private int cartid;
	private int memberid;
	private int proxyid;
	private String sessionid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCartid() {
		return cartid;
	}
	public void setCartid(int cartid) {
		this.cartid = cartid;
	}
	public int getProxyid() {
		return proxyid;
	}
	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public int getMemberid() {
		return memberid;
	}
	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}
}
