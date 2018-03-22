package com.enation.app.ext.component.membershop.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberShopTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	private ICreditManager creditManager;

	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MemberShopTag]");
		}
		if(this.creditManager.get(member.getMember_id()).getReview()!=1){
			throw new TemplateModelException("未通过授信，无法创建店铺！");
		}
		Map result = new HashMap();
		MemberShop memberShop = new MemberShop();
		memberShop = this.memberShopManager.get(member.getMember_id());
		if(memberShop==null){
			result.put("shopname","");
			result.put("label","");
			result.put("intro","");
			result.put("memberimg","/img/face/default.jpg");
			result.put("shopimg","/img/face/default_background.jpg");
			result.put("mobile","");
			result.put("qq","");
		}else{
			result.put("shopname",memberShop.getShopName());
			result.put("label",memberShop.getLabel());
			result.put("intro",memberShop.getShopIntro());
			result.put("memberimg",memberShop.getMemberImg());
			result.put("shopimg",memberShop.getShopImg());
			result.put("mobile",memberShop.getMobile());
			result.put("qq",memberShop.getQq());
		}		
		return result;
	}
	
	public IMemberShopManager getMemberShopManager()
	{
		return this.memberShopManager;
	}
	
	public void setMemberShopManager(IMemberShopManager memberShopManager)
	{
		this.memberShopManager = memberShopManager;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}

}
