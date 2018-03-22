package com.enation.app.ext.component.bankselect.model;

import java.io.Serializable;

public class BankSelect implements Serializable
{
	private int id;
	private int proxyMemberId;
	private int bankInfoId;
	private int status;
	private String editTime;
	
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
	public int getBankInfoId() {
		return bankInfoId;
	}
	public void setBankInfoId(int bankInfoId) {
		this.bankInfoId = bankInfoId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	
}
