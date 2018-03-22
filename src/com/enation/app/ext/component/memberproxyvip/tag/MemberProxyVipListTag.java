package com.enation.app.ext.component.memberproxyvip.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberProxyVipListTag extends BaseFreeMarkerTag{

	private IMemberProxyVipManager memberProxyVipManager;
	private IMemberShopManager memberShopManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MemberProxyVipListTag]");
		}
		Map result = new HashMap();
		List<MemberProxyVip> mList = this.memberProxyVipManager.getByMemberId(member.getMember_id());
		List aList = new ArrayList();
		int count = mList.size();
		for(int i=0;i<count;i++){
			MemberProxyVip memberProxyVip = mList.get(i);
			MemberShop memberShop = this.memberShopManager.get(memberProxyVip.getProxyMemberId());
			Map mMap = new HashMap();
			mMap.put("proxymemberid",memberProxyVip.getProxyMemberId());
			if(memberProxyVip.getV3LT().equals("0")!=true){
				mMap.put("level",3);
			}else if(memberProxyVip.getV2LT().equals("0")!=true){
				mMap.put("level",2);
			}else if(memberProxyVip.getV1LT().equals("0")!=true){
				mMap.put("level",1);
			}else{
				mMap.put("level",0);
			}
			mMap.put("shopimg",memberShop.getMemberImg());
			mMap.put("shopname",memberShop.getShopName());
			mMap.put("label",memberShop.getLabel());
			mMap.put("ex",memberProxyVip.getVipEX());
			aList.add(mMap);
		}
		result.put("count",count);
		result.put("alist",aList);
		return result;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

}
