package com.enation.app.ext.component.vipleveldetail.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;
import com.enation.app.ext.component.vipleveldetail.service.IVipLevelDetailManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class VipInfoTag extends BaseFreeMarkerTag{

	private IVipLevelDetailManager vipLevelDetailManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[VipInfoTag]");
		}
		Map result = new HashMap();
		VipLevelDetail vipLevelDetail = this.vipLevelDetailManager.getByMemberId(member.getMember_id());
		if(vipLevelDetail==null){
			result.put("re","0");
			result.put("v1name","");
			result.put("v1info","");
			result.put("v2name","");
			result.put("v2info","");
			result.put("v3name","");
			result.put("v3info","");
			return result;
		}else{
			result.put("re","1");
			result.put("v1name",vipLevelDetail.getV1Name());
			result.put("v1info",vipLevelDetail.getV1Content());
			result.put("v2name",vipLevelDetail.getV2Name());
			result.put("v2info",vipLevelDetail.getV2Content());
			result.put("v3name",vipLevelDetail.getV3Name());
			result.put("v3info",vipLevelDetail.getV3Content());
			return result;
		}
	}

	public IVipLevelDetailManager getVipLevelDetailManager() {
		return vipLevelDetailManager;
	}

	public void setVipLevelDetailManager(
			IVipLevelDetailManager vipLevelDetailManager) {
		this.vipLevelDetailManager = vipLevelDetailManager;
	}

}
