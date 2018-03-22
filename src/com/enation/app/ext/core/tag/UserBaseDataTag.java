package com.enation.app.ext.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class UserBaseDataTag extends BaseFreeMarkerTag{

	private IMemberExtManager memberExtManager;
	private IFollowManager followManager;
	private ICreditManager creditManager;
	private IMemberShopManager memberShopManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[UserBaseDataTag]");
		}
		Map result = new HashMap();
		Member m = memberExtManager.getByMemberid(member.getMember_id());
		String face = m.getFace();
		MemberShop memberShop = this.memberShopManager.get(member.getMember_id());
		if(face==null||face==""){result.put("face", "/javamall/img/face/default.jpg");}
		else{result.put("face", face);}
		result.put("username", member.getName());
		String mobile = m.getMobile();
		long mob = Long.valueOf(mobile);
		String l4 = mobile.substring(mobile.length()-4,mobile.length());
		String mobileStar = mob/100000000+"****"+l4;
		result.put("mobile",mobileStar);
		result.put("follownum", String.valueOf(this.followManager.getFollowNum(member.getMember_id())));
		int review = this.creditManager.checkReviewByMemberId(member.getMember_id());
		if(memberShop==null&&review==1){
			result.put("isCredit",3);			//通过未完善商铺
		}else if(review==1){
			result.put("isCredit",1);			//通过已完善商铺
		}else if(review==2){
			result.put("isCredit",4);			//未通过
		}else if(review==0){
			result.put("isCredit",0);			//未申请
		}else{
			result.put("isCredit",2);			//审核中
		}
		return result;
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}

	public IFollowManager getFollowManager() {
		return followManager;
	}

	public void setFollowManager(IFollowManager followManager) {
		this.followManager = followManager;
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

}
