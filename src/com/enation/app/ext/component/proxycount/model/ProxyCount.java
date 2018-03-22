package com.enation.app.ext.component.proxycount.model;

import java.io.Serializable;

public class ProxyCount implements Serializable
{
	private int id;
	private int goodsId;
	private int proxyTimeCount;
	private int sellCount;
	private int proxyAllCount;
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
	public int getProxyTimeCount() {
		return proxyTimeCount;
	}
	public void setProxyTimeCount(int proxyTimeCount) {
		this.proxyTimeCount = proxyTimeCount;
	}
	public int getSellCount() {
		return sellCount;
	}
	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}
	public int getProxyAllCount() {
		return proxyAllCount;
	}
	public void setProxyAllCount(int proxyAllCount) {
		this.proxyAllCount = proxyAllCount;
	}
}
