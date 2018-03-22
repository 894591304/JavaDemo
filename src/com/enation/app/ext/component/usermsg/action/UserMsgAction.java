package com.enation.app.ext.component.usermsg.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.enation.app.ext.component.usermsg.service.IUserMsgManager;
import com.enation.eop.sdk.user.IUserService;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;

@ParentPackage("shop_default")
@Namespace("api/shop")
@Action
public class UserMsgAction extends WWAction {
	
	
	private IUserMsgManager userMsgManager;
	
	private boolean checkAdminPerm()
	{
		IUserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn()) {
			showErrorJson("您无权限访问此API");
			return false;
		}
			return true;
	}
	
	
}
