package com.enation.app.ext.component.useraccount.model;

import java.io.Serializable;

public class UserAccount implements Serializable
{
	private int id;
	private int memberId;
	private String accountId;
	private int creditLevel;
	private float credit;
	private float remainCredit;
	private float repay;
	private String repayTime;
	private float waitCash;
	private float waitMoney;
	private float addEarn;
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public int getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(int creditLevel) {
		this.creditLevel = creditLevel;
	}
	public float getCredit() {
		return credit;
	}
	public void setCredit(float credit) {
		this.credit = credit;
	}
	public float getRemainCredit() {
		return remainCredit;
	}
	public void setRemainCredit(float remainCredit) {
		this.remainCredit = remainCredit;
	}
	public float getRepay() {
		return repay;
	}
	public void setRepay(float repay) {
		this.repay = repay;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public float getWaitCash() {
		return waitCash;
	}
	public void setWaitCash(float waitCash) {
		this.waitCash = waitCash;
	}
	public float getWaitMoney() {
		return waitMoney;
	}
	public void setWaitMoney(float waitMoney) {
		this.waitMoney = waitMoney;
	}
	public float getAddEarn() {
		return addEarn;
	}
	public void setAddEarn(float addEarn) {
		this.addEarn = addEarn;
	}
}
