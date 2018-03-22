package com.enation.app.ext.component.proxyMemberBankInfo.service;

import java.util.List;

import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;

public abstract interface IProxyMemberBankInfoManager {
	
	public abstract void add(ProxyMemberBankInfo proxyMemberBankInfo);
	
	public abstract ProxyMemberBankInfo get(int id);
	
	public abstract List<ProxyMemberBankInfo> getByMemberId(int memberId);
	
	public abstract ProxyMemberBankInfo getByCard(String cardNum);
	
	public abstract void update(ProxyMemberBankInfo proxyMemberBankInfo);
	
	public abstract void delete(int id);
}
