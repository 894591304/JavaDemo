package com.enation.app.ext.component.proxyMemberBankInfo.service.impl;

import java.util.List;

import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;
import com.enation.app.ext.component.proxyMemberBankInfo.service.IProxyMemberBankInfoManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ProxyMemberBankInfoManager extends BaseSupport<ProxyMemberBankInfo> implements IProxyMemberBankInfoManager{

	
	public void add(ProxyMemberBankInfo proxyMemberBankInfo) {
		String sql = "select count(*) from proxymemberbankinfo where proxyMemberId = ? and payeeBankCard = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{proxyMemberBankInfo.getProxyMemberId(),proxyMemberBankInfo.getPayeeBankCard()});
		if(count==0)
		{this.baseDaoSupport.insert("proxymemberbankinfo",proxyMemberBankInfo);}
		else
		{update(proxyMemberBankInfo);}
	}

	
	public ProxyMemberBankInfo get(int id) {
		String sql = "select * from proxymemberbankinfo where id = ?";
		ProxyMemberBankInfo proxyMemberBankInfo = this.baseDaoSupport.queryForObject(sql,ProxyMemberBankInfo.class,new Object[]{id});
		return proxyMemberBankInfo;
	}

	
	public List<ProxyMemberBankInfo> getByMemberId(int memberId) {
		String sql = "select * from proxymemberbankinfo where proxyMemberId = ?";
		List<ProxyMemberBankInfo> pList = this.baseDaoSupport.queryForList(sql,ProxyMemberBankInfo.class,new Object[]{memberId});
		return pList;
	}

	
	public void update(ProxyMemberBankInfo proxyMemberBankInfo) {
		this.baseDaoSupport.update("proxymemberbankinfo",proxyMemberBankInfo,"id="+proxyMemberBankInfo.getId());	
	}

	
	public void delete(int id) {
		String sql = "delete from proxymemberbankinfo where id = ?";
		this.baseDaoSupport.execute(sql, new Object[]{id});
	}


	@Override
	public ProxyMemberBankInfo getByCard(String cardNum) {
		String sql = "select * from proxymemberbankinfo where payeeBankCard = ?";
		ProxyMemberBankInfo proxyMemberBankInfo = this.baseDaoSupport.queryForObject(sql,ProxyMemberBankInfo.class,new Object[]{cardNum});
		return proxyMemberBankInfo;
	}

}
