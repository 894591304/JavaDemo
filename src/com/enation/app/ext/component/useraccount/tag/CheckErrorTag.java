package com.enation.app.ext.component.useraccount.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CheckErrorTag extends BaseFreeMarkerTag{

	private IUserAccountManager userAccountManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		List<UserAccount> uList = this.userAccountManager.getErrorUserAccount();
		int total = uList.size();
		for(int i=0;i<total;i++){
			UserAccount userAccount = uList.get(i);
			userAccount.setWaitCash(userAccount.getWaitCash()+userAccount.getRemainCredit()+userAccount.getRepay()-userAccount.getCredit());
			userAccount.setRemainCredit(userAccount.getRemainCredit()-(userAccount.getRemainCredit()+userAccount.getRepay()-userAccount.getCredit()));
			this.userAccountManager.update(userAccount);
		}
		return 1;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}	
}
