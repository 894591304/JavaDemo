package com.enation.app.ext.core.action;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.action.api.MemberApiAction;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.app.ext.component.mobile.service.IMobileManager;
import com.enation.app.ext.component.mobile.service.IMobileMemberManager;
import com.enation.app.ext.component.mobile.util.MsgSendClient;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.EncryptionUtil1;
import com.enation.framework.util.HttpUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

public class MobileMemberAction extends MemberApiAction
{

	private static final long serialVersionUID = 5704832454505691176L;	
	
	private IMemberManager memberManager;
	private IMobileMemberManager mobileMemberManager;
	private IMobileManager mobileManager;
	private String username;
	private String mobile;
	private String password;
	private String checkcode;
	private String passwd_re;
	private String remember;
	
	
	public String mobileRegister()
	{
		Member member = new Member();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		if(StringUtil.isEmpty(this.username)){
			setUsername(this.mobile);
		}
		if((this.username.length()<2)||(this.username.length()>20)){
			showErrorJson("用户名的长度为2-20个字符！");
			return "json_message";
		}
		if(this.username.equals("请输入昵称")){
			this.username = this.mobile;
		}
		if(this.username.contains("@")){
			showErrorJson("用户名不能包含特殊字符！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.mobile)){
			showErrorJson("注册手机号不能为空！");
			return "json_message";
		}
		if(!this.mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")){
			showErrorJson("手机号输入有误，请重新输入！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.password)||this.password.equals("请输入登录密码")){
			showErrorJson("密码不能为空！");
			return "json_message";
		}
		if(!this.password.equals(this.passwd_re)){
			showErrorJson("两次密码输入不一致！");
			return "json_message";
		}
		if(this.memberManager.checkname(this.username)>0){
			showErrorJson("此用户名已经存在，请您选择另外的用户名！");
			return "json_message";
		}
		if(!MsgSendClient.checkMsg(this.mobile, this.checkcode)){
			showErrorJson("验证码输入有误或已超时！");
			return "json_message";
		}
		if(this.mobileMemberManager.checkmobile(this.mobile)>0){
			showErrorJson("此手机号已经注册，请您选择另外的手机号！");
			return "json_message";
		}
		this.mobileManager.delete(this.mobile);
		member.setMobile(mobile);
		member.setUname(this.username);
		member.setPassword(this.password);
		member.setEmail("");
		member.setRegisterip(registerip);
		member.setParentid(null);
		int result = this.memberManager.register(member);
		if(result == 1){
			this.memberManager.login(this.username, this.password);
			this.json=JsonMessageUtil.getStringJson("result", "1");
		}else{
			showErrorJson("用户名["+member.getUname()+"]已存在！");
		}		
		return "json_message";
	}
	
	public String updatePassword(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		if(StringUtil.isEmpty(this.mobile)){
			showErrorJson("请填写手机号！");
			return "json_message";
		}
		if(!this.mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")){
			showErrorJson("手机号输入有误，请重新输入！");
			return "json_message";
		}
		int mid = this.mobileMemberManager.getMemberidByMobile(this.mobile);
		if(mid==-1){
			showErrorJson("手机号账户不存在，请重新输入！");
			return "json_message";
		}
		if(!MsgSendClient.checkMsg(this.mobile, this.checkcode)){
			showErrorJson("验证码输入有误或已超时！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.password)){
			showErrorJson("密码不能为空！");
			return "json_message";
		}
		if(!this.password.equals(this.passwd_re)){
			showErrorJson("两次密码输入不一致！");
			return "json_message";
		}	
		this.memberManager.updatePassword(mid, this.password);
		String uname = this.mobileMemberManager.getUnameByMobile(this.mobile);
		String cookieValue = EncryptionUtil1.authcode("{username:\"" + uname + "\",password:\"" + StringUtil.md5(this.password) + "\"}", "ENCODE", "", 0);
		HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(), "MJUser", cookieValue, 20);
		this.mobileManager.delete(this.mobile);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String mobileLogin()
	{
		String uname = this.mobileMemberManager.getUnameByMobile(this.mobile);
		if(uname==null&&uname==""){
			showErrorJson("手机号或密码错误！");
		}else{
			int result = this.memberManager.login(uname, this.password);
			if(result == 1)
			{
				if((this.remember != null) && (this.remember.equals("1"))) {
					String cookieValue = EncryptionUtil1.authcode("{username:\"" + this.username + "\",password:\"" + StringUtil.md5(this.password) + "\"}", "ENCODE", "", 0);
					HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(), "MJUser", cookieValue, 20);
				}
				showSuccessJson("登陆成功");
			}else{
				showErrorJson("手机号或密码错误！");
			}
		}		
		return "json_message";
	}
	
	public IMobileMemberManager getMobileMemberManager(){
		return this.mobileMemberManager;
	}
	
	public void setMobileMemberManager(IMobileMemberManager mobileMemberManager){
		this.mobileMemberManager = mobileMemberManager;
	}
	
	public IMobileManager getMobileManager(){
		return this.mobileManager;
	}
	
	public void setMobileManager(IMobileManager mobileManager){
		this.mobileManager = mobileManager;
	}
	public IMemberManager getMemberManager(){
		return this.memberManager;
	}
	
	public void setMemberManager(IMemberManager memberManager){
		this.memberManager = memberManager;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String password){
		this.password = password;		
	}
	
	public String getMobile(){
		return this.mobile;
	}
	
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	
	public String getCheckcode(){
		return this.checkcode;
	}
	
	public void setCheckcode(String checkcode){
		this.checkcode = checkcode;
	}
	
	public String getPasswd_re(){
		return this.passwd_re;		
	}
	
	public void setPasswd_re(String passwd_re){
		this.passwd_re = passwd_re;
	}
	
	public String getRemember(){
		return this.remember;
	}
	
	public void setRemember(String remember){
		this.remember = remember;
	}
}
