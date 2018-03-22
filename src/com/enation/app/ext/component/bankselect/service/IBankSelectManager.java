package com.enation.app.ext.component.bankselect.service;

import java.util.List;

import com.enation.app.ext.component.bankselect.model.BankSelect;


public abstract interface IBankSelectManager {
	
	public abstract void add(BankSelect bankSelect);
	
	public abstract List<BankSelect> getByMemberId(int proxyMemberId);
	
	public abstract BankSelect getDefaultByMemberId(int proxyMemberId);
	
	public abstract List<BankSelect> getNotDefaultByMemberId(int proxyMemberId);
	
	public abstract void update(BankSelect bankSelect);
	
	public abstract void delete(int bankInfoId);
	
	public abstract void setAllStatusTo0(int proxyMemberId);
	
	public abstract BankSelect getByInfoId(int infoId);
}
