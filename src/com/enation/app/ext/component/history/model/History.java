package com.enation.app.ext.component.history.model;

import java.io.Serializable;

public class History implements Serializable{
	
	private int id;
	private int memberId;
	private int proxyId;
	private int goodsId;
	private String lookTime;
	private int lookNum;
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
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getLookTime() {
		return lookTime;
	}
	public void setLookTime(String lookTime) {
		this.lookTime = lookTime;
	}
	public int getLookNum() {
		return lookNum;
	}
	public void setLookNum(int lookNum) {
		this.lookNum = lookNum;
	}
}
