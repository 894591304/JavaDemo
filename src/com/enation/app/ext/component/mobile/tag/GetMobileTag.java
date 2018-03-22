package com.enation.app.ext.component.mobile.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.mobile.service.IMobileMemberManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GetMobileTag extends BaseFreeMarkerTag{

	private IMobileMemberManager mobileMemberManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[GetMobileTag]");
		}
		Map result = new HashMap();
		int memberId = member.getMember_id().intValue();
		String mobile = this.mobileMemberManager.getMobileByMemberId(memberId);
		return mobile;
	}

	public IMobileMemberManager getMobileMemberManager() {
		return mobileMemberManager;
	}

	public void setMobileMemberManager(IMobileMemberManager mobileMemberManager) {
		this.mobileMemberManager = mobileMemberManager;
	}

}
