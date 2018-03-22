package com.enation.app.b2b2c.component.creditmember.action;

import java.util.List;

import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;
import com.enation.app.ext.component.proxyMemberBankInfo.service.IProxyMemberBankInfoManager;
import com.enation.framework.action.WWAction;

public class CreditMemberBankAction extends WWAction {

	private static final long serialVersionUID = 7261731169217625289L;
	private IProxyMemberBankInfoManager proxyMemberBankInfoManager;
	private Integer memberId;
	
	public String creditMemberBanklistJson() {
		List<ProxyMemberBankInfo> memberBankList= this.proxyMemberBankInfoManager.getByMemberId(this.memberId);
		showGridJson(memberBankList);
		return "json_message";
	}

	public IProxyMemberBankInfoManager getProxyMemberBankInfoManager() {
		return proxyMemberBankInfoManager;
	}

	public void setProxyMemberBankInfoManager(IProxyMemberBankInfoManager proxyMemberBankInfoManager) {
		this.proxyMemberBankInfoManager = proxyMemberBankInfoManager;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

}
