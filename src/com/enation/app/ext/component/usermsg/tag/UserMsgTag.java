package com.enation.app.ext.component.usermsg.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.usermsg.UserMsg;
import com.enation.app.ext.component.usermsg.service.IUserMsgManager;
import com.enation.app.ext.component.usermsg.service.UserMsgManager;
import com.enation.eop.sdk.user.IUserService;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class UserMsgTag extends BaseFreeMarkerTag
{
	private IUserMsgManager userMsgManager;
	
	protected Object exec(Map arg0) throws TemplateModelException 
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[UserMsgTag]");
		}
		Map result = new HashMap();
		int memberid = member.getMember_id().intValue();
		List msgList=this.userMsgManager.getByMemberId(memberid);
		Long totalCount = Long.valueOf(msgList.size());
		result.put("totalCount",totalCount);
		result.put("msgList",msgList);
		return result;
	}
	
	public IUserMsgManager getUserMsgManager()
	{
		return this.userMsgManager;
	}
	
	public void setUserMsgManager(IUserMsgManager userMsgManager)
	{
		this.userMsgManager = userMsgManager;
	}
}
