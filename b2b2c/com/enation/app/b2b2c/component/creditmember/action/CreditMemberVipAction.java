package com.enation.app.b2b2c.component.creditmember.action;

import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.framework.action.WWAction;

public class CreditMemberVipAction extends WWAction {

	private static final long serialVersionUID = 7261731169217625289L;
	private Integer memberId;
	
	private IMemberProxyVipManager memberProxyVipManager;
	
	public String creditProxyViplistJson() {
		this.webpage = this.memberProxyVipManager.searchProxyVipByMemberId(this.memberId, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()));
		showGridJson(this.webpage);
		return "json_message";
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

}
