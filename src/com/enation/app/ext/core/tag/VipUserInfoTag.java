package com.enation.app.ext.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class VipUserInfoTag extends BaseFreeMarkerTag{

	private ICreditManager creditManager;
	private IMemberShopManager memberShopManager;
	private IProxyManager proxyManager;
	private IFollowManager followManager;
	private IMemberExtManager memberExtManager;
	private IUserAccountManager userAccountManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[VipUserInfoTag]");
		}
		Map result = new HashMap();
		int level = this.userAccountManager.getLevel(member.getMember_id());
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
		int salenum = this.proxyManager.getAllSaleByMemberid(member.getMember_id());
		int agentnum = this.proxyManager.getAllByMemberid(member.getMember_id()).size();
		Credit credit = this.creditManager.get(member.getMember_id());
		MemberShop memberShop = this.memberShopManager.get(member.getMember_id());
		result.put("face", memberShop.getMemberImg());
		result.put("name",member.getName());
		result.put("sale",salenum);
		result.put("agentnum",agentnum);
		result.put("fans",this.followManager.getFansNum(member.getMember_id()));
		result.put("levellist", levellist);
		result.put("levellist2", levellist2);
		result.put("weibo", credit.getWeibo());
		result.put("weixin", credit.getWeixin());
		result.put("zhibo", credit.getLive());
		result.put("zhiboID",credit.getLiveId());
		result.put("level",level);
		return result;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
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

	public IFollowManager getFollowManager() {
		return followManager;
	}

	public void setFollowManager(IFollowManager followManager) {
		this.followManager = followManager;
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

}
