package com.enation.app.ext.component.zsydetail.model;

import java.io.Serializable;

public class ZsyDetail implements Serializable{
	
	private int id;
	private int proxyMemberId;
	private String outCustomerId;
	private String payDate;
	private int payTime;
	private float amount;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProxyMemberId() {
		return proxyMemberId;
	}
	public void setProxyMemberId(int proxyMemberId) {
		this.proxyMemberId = proxyMemberId;
	}
	public String getOutCustomerId() {
		return outCustomerId;
	}
	public void setOutCustomerId(String outCustomerId) {
		this.outCustomerId = outCustomerId;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public int getPayTime() {
		return payTime;
	}
	public void setPayTime(int payTime) {
		this.payTime = payTime;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
