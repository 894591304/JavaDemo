package com.enation.app.ext.component.proxyMemberBankInfo.model;

import java.io.Serializable;

public class ProxyMemberBankInfo implements Serializable{
	
	private int id;
	private int proxyMemberId;
	private int payeeBankAccountType;
	private String payeeBankCard;
	private String payeeBankNum;
	private String payeeCardHolder;
	private String payeeBankBranchName;
	private String payeeBankProvinceName;
	private String payeeBankCityName;
	
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
	public int getPayeeBankAccountType() {
		return payeeBankAccountType;
	}
	public void setPayeeBankAccountType(int payeeBankAccountType) {
		this.payeeBankAccountType = payeeBankAccountType;
	}
	public String getPayeeBankCard() {
		return payeeBankCard;
	}
	public void setPayeeBankCard(String payeeBankCard) {
		this.payeeBankCard = payeeBankCard;
	}
	public String getPayeeCardHolder() {
		return payeeCardHolder;
	}
	public void setPayeeCardHolder(String payeeCardHolder) {
		this.payeeCardHolder = payeeCardHolder;
	}
	public String getPayeeBankBranchName() {
		return payeeBankBranchName;
	}
	public void setPayeeBankBranchName(String payeeBankBranchName) {
		this.payeeBankBranchName = payeeBankBranchName;
	}
	public String getPayeeBankProvinceName() {
		return payeeBankProvinceName;
	}
	public void setPayeeBankProvinceName(String payeeBankProvinceName) {
		this.payeeBankProvinceName = payeeBankProvinceName;
	}
	public String getPayeeBankCityName() {
		return payeeBankCityName;
	}
	public void setPayeeBankCityName(String payeeBankCityName) {
		this.payeeBankCityName = payeeBankCityName;
	}
	public String getPayeeBankNum() {
		return payeeBankNum;
	}
	public void setPayeeBankNum(String payeeBankNum) {
		this.payeeBankNum = payeeBankNum;
	}
}
