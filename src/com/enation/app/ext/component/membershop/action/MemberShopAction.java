package com.enation.app.ext.component.membershop.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

import freemarker.template.TemplateModelException;

public class MemberShopAction extends WWAction{

	private static final long serialVersionUID = -253936824260336278L;
	
	private IMemberShopManager memberShopManager;
	private ICreditManager creditManager;
	private int memberId;
	private String shopName;
	private String label;
	private String shopIntro;
	private String memberImg;
	private String shopImg;
	private String mobile;
	private String qq;
	
	public String submit()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能申请店铺！");
			return "json_message";
		}
		setMemberId(member.getMember_id());
		MemberShop memberShop = new MemberShop();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		if(this.creditManager.checkReviewByMemberId(this.memberId)!=1){
			showErrorJson("未申请授信或授信未通过不能申请店铺！");
			return "json_message";
		}
		if(this.shopName.contains("@")){
			showErrorJson("店铺名不能包含特殊字符！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.shopName)){
			showErrorJson("店铺名不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.memberImg)){
			showErrorJson("请上传店铺头像！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.shopImg)){
			showErrorJson("请上传店铺背景！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.label)){
			showErrorJson("请输入店铺标签！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.shopIntro)){
			showErrorJson("请输入店铺简介！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.mobile)){
			showErrorJson("请输入联系电话！");
			return "json_message";
		}
		if(!this.mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")){
			showErrorJson("手机号输入有误，请重新输入！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.qq)){
			showErrorJson("请输入客服QQ！");
			return "json_message";
		}
		String regex="[1-9][0-9]{4,14}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(this.qq);
		if(!m.matches()){
			showErrorJson("QQ号输入有误，请重新输入！");
			return "json_message";
		}
		memberShop.setMemberId(this.memberId);
		memberShop.setShopName(this.shopName);
		memberShop.setLabel(this.label);
		memberShop.setShopIntro(this.shopIntro);
		memberShop.setMemberImg(this.memberImg);
		memberShop.setShopImg(this.shopImg);
		memberShop.setMobile(this.mobile);
		memberShop.setQq(this.qq);
		//memberShop.setLevel(this.creditManager.getLevelByMemberId(this.memberId));
		memberShop.setLevel(9);
		memberShop.setAgencyId(-1);
		this.memberShopManager.add(memberShop);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	
	
	public IMemberShopManager getMemberShopManager(){
		return this.memberShopManager;
	}
	
	public void setMemberShopManager(IMemberShopManager memberShopManager){
		this.memberShopManager = memberShopManager;
	}
	
	public ICreditManager getCreditManager(){
		return this.creditManager;
	}
	
	public void setCreditManager(ICreditManager creditManager){
		this.creditManager = creditManager;
	}
	
	public int getMemberId() {
		return this.memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getShopName() {
		return this.shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getShopIntro() {
		return this.shopIntro;
	}
	public void setShopIntro(String shopIntro) {
		this.shopIntro = shopIntro;
	}
	public String getMemberImg() {
		return this.memberImg;
	}
	public void setMemberImg(String memberImg) {
		this.memberImg = memberImg;
	}
	public String getShopImg() {
		return this.shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQq() {
		return this.qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	
}
