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
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyVipMemberListTag extends BaseFreeMarkerTag{

	private IMemberProxyVipManager memberProxyVipManager;
	private IMemberManager memberManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		if(member == null){
			result.put("re", 0); //未登录
		}
		List<MemberProxyVip> mList = this.memberProxyVipManager.getByProxyMemberId(member.getMember_id());
		int count = mList.size();
		int vipall = 0;
		int vip1 = 0;
		int vip2 = 0;
		int vip3 = 0;
		int vipold = 0;
		List mpList = new ArrayList();
		for(int i=0;i<count;i++){
			Map mMap = new HashMap();
			MemberProxyVip memberProxyVip = mList.get(i);
			Member m = this.memberManager.get(memberProxyVip.getMemberId());
			String img = m.getFace();
			if(img==null){
				img = "/img/face/default.jpg";
			}
			mMap.put("img",img);
			mMap.put("name",m.getName());
			mMap.put("exp",memberProxyVip.getVipEX());
			if(memberProxyVip.getV3LT().equals("0")==false){
				mMap.put("level", 3);
				vip3++;
				vipall++;
			}else if(memberProxyVip.getV2LT().equals("0")==false){
				mMap.put("level", 2);
				vip2++;
				vipall++;
			}else if(memberProxyVip.getV1LT().equals("0")==false){
				mMap.put("level", 1);
				vip1++;
				vipall++;
			}else{
				mMap.put("level", 0);
				vipold++;
			}
			mpList.add(mMap);
		}
		result.put("vipcount", vipall);
		result.put("vip1count", vip1);
		result.put("vip2count", vip2);
		result.put("vip3count", vip3);
		result.put("vipoldcount", vipold);
		result.put("mplist",mpList);
		return result;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

}
