package com.enation.app.ext.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class VipUserWalletTag extends BaseFreeMarkerTag{

	private IUserAccountManager userAccountManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[VipUserWalletTag]");
		}
		UserAccount userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		Map result = new HashMap();
		result.put("accountid", String.valueOf(userAccount.getAccountId()));
		result.put("addEarn", userAccount.getAddEarn());
		result.put("credit", userAccount.getCredit());
		result.put("remainCredit",userAccount.getRemainCredit());
		result.put("repay",userAccount.getRepay());
		if(userAccount.getRepayTime()==null){
			result.put("repayTime",0);
		}else{
			result.put("repayTime", userAccount.getRepayTime());
		}	
		result.put("waitCash", userAccount.getWaitCash());
		result.put("waitMoney",userAccount.getWaitMoney());	
		result.put("level",userAccount.getCreditLevel());
		return result;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

}
