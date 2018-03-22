package com.enation.app.ext.component.proxy.model;

import java.io.Serializable;

public class Proxy implements Serializable
{
	private int id;
	private int memberId;
	private int goodsId;
	private int goodsAmount;
	private int onSale;
	private int sale;
	private float frozenDeposit;
	private float frozenCredit;
	private float ticketFrozenCredit;
	private float frozenEarn;
	private String proxyBeginTime;
	private String proxyTestTime;
	private String proxyEndTime;
	private String goodsCategory;
	private String goodsLabel;
	private int status;
	
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
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(int goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public int getOnSale() {
		return this.onSale;
	}
	public void setOnSale(int onSale) {
		this.onSale = onSale;
	}
	public String getProxyBeginTime() {
		return proxyBeginTime;
	}
	public void setProxyBeginTime(String proxyBeginTime) {
		this.proxyBeginTime = proxyBeginTime;
	}
	public String getProxyEndTime() {
		return proxyEndTime;
	}
	public void setProxyEndTime(String proxyEndTime) {
		this.proxyEndTime = proxyEndTime;
	}
	public String getGoodsCategory() {
		return goodsCategory;
	}
	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	public String getGoodsLabel() {
		return goodsLabel;
	}
	public void setGoodsLabel(String goodsLabel) {
		this.goodsLabel = goodsLabel;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSale() {
		return sale;
	}
	public void setSale(int sale) {
		this.sale = sale;
	}
	public float getFrozenDeposit() {
		return frozenDeposit;
	}
	public void setFrozenDeposit(float frozenDeposit) {
		this.frozenDeposit = frozenDeposit;
	}
	public float getFrozenCredit() {
		return frozenCredit;
	}
	public void setFrozenCredit(float frozenCredit) {
		this.frozenCredit = frozenCredit;
	}
	public float getFrozenEarn() {
		return frozenEarn;
	}
	public void setFrozenEarn(float frozenEarn) {
		this.frozenEarn = frozenEarn;
	}
	public String getProxyTestTime() {
		return proxyTestTime;
	}
	public void setProxyTestTime(String proxyTestTime) {
		this.proxyTestTime = proxyTestTime;
	}
	public float getTicketFrozenCredit() {
		return ticketFrozenCredit;
	}
	public void setTicketFrozenCredit(float ticketFrozenCredit) {
		this.ticketFrozenCredit = ticketFrozenCredit;
	}
	
}
