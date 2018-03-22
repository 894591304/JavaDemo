package com.enation.app.ext.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.app.ext.component.proxy.model.Proxy;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class VipUserTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[VipUserTag]");
		}
		Map result = new HashMap();
		int level = this.userAccountManager.getLevel(member.getMember_id());
		MemberShop memberShop = this.memberShopManager.get(member.getMember_id());
		List levellist = new ArrayList();
		List levellist2 = new ArrayList();
		Map list1 = new HashMap();
		Map list2 = new HashMap();
		for(int i=0;i<level+2;i++){
			list1.put("level",i);
			levellist.add(list1);
			}
		for(int j=0;j<3-level;j++){
			list2.put("level", j);
			levellist2.add(list2);
		}
		UserAccount userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		List<Proxy> pList = this.proxyManager.getAllByMemberid(member.getMember_id());
		result.put("memberid",String.valueOf(member.getMember_id()));
		result.put("face", memberShop.getMemberImg());
		result.put("agentnum", pList.size());
		result.put("waitCash",userAccount.getWaitCash());
		result.put("waitMoney",userAccount.getWaitMoney());
		result.put("remaincredit", userAccount.getRemainCredit());
		result.put("levellist", levellist);
		result.put("levellist2", levellist2);
		return result;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}
	
}
