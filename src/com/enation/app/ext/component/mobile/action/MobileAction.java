package com.enation.app.ext.component.mobile.action;

import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.StringUtil;
import com.enation.framework.util.JsonMessageUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.mobile.service.IMobileManager;
import com.enation.app.ext.component.mobile.service.IMobileMemberManager;
import com.enation.app.ext.component.mobile.util.MsgSendClient;
import com.enation.framework.action.WWAction;


public class MobileAction extends WWAction{
	
	private static final long serialVersionUID = -9024137817164601003L;

	private IMobileManager mobileManager;
	private IMobileMemberManager mobileMemberManager;
	private String mobile;
	private String username;
	private String checkNum;
	
	
	public String sendmsg()
	{
		boolean result = MsgSendClient.sendMsg(this.mobile);
		if(result){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			showSuccessJson("验证短信发送成功，请在三分钟内输入验证码！");
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "1");
			showErrorJson("验证短信发送失败，请重新点击发送验证码！");
		}
		return "json_message";
	}
	
	public String checkmsg()
	{
		boolean result = MsgSendClient.checkMsg(this.mobile, this.checkNum);
		if(result){
			showSuccessJson("验证完成！");
		}else{
			showErrorJson("您输入的验证码有误，请查看后再次输入！");
		}
		return "json_message";
	}
	
	public String checkMobile()
	{
		int result = this.mobileMemberManager.checkmobile(this.mobile);
		if(result == 0){
			showSuccessJson("手机号可以使用！");
		}else{
			showErrorJson("该手机号已被注册！");
		}
		return "json_message";
	}
	
	public String checkname()
	{
		int result = this.mobileMemberManager.checkname(this.username);
		if(result == 0){
			showSuccessJson("昵称可以使用！");
		}else{
			showErrorJson("该昵称已被使用！");
		}
		return "json_message";
	}
	
	public String sendCode(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("请登录后验证！");
			return "json_message";
		}
		boolean result = MsgSendClient.sendMsg(member.getMobile());
		if(result){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			showSuccessJson("验证短信发送成功，请在三分钟内输入验证码！");
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "1");
			showErrorJson("验证短信发送失败，请重新点击发送验证码！");
		}
		return "json_message";
	}
	
	public String checkCode(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("请登录后验证！");
			return "json_message";
		}
		boolean result = MsgSendClient.checkMsg(member.getMobile(), this.checkNum);
		if(result){
			showSuccessJson("验证完成！");
		}else{
			showErrorJson("您输入的验证码有误，请查看后再次输入！");
		}
		return "json_message";
	}
	
	public IMobileManager getMobileManager()
	{
		return this.mobileManager;
	}
	
	public void setMobileManager(IMobileManager mobileManager){
		this.mobileManager = mobileManager;
	}
	
	public IMobileMemberManager getMobileMemberManager(){
		return this.mobileMemberManager;
	}
	
	public void setMobileMemberManager(IMobileMemberManager mobileMemberManager){
		this.mobileMemberManager = mobileMemberManager;
	}
	
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	public String getMobile()
	{
		return this.mobile;
	}
	
	public void setUserName(String username){
		this.username = username;
	}
	
	public String getUserName()
	{
		return this.username;
	}
	
	public void setCheckNum(String checkNum)
	{
		this.checkNum = checkNum;
	}
	
	public String getCheckNum()
	{
		return this.checkNum;
	}
}
