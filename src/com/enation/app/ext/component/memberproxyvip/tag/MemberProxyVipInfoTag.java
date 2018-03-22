package com.enation.app.ext.component.memberproxyvip.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;
import com.enation.app.ext.component.vipleveldetail.service.IVipLevelDetailManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberProxyVipInfoTag extends BaseFreeMarkerTag{

	private IMemberProxyVipManager memberProxyVipManager;
	private IVipLevelDetailManager vipLevelDetailManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MemberProxyVipInfoTag]");
		}
		Map result = new HashMap();
		try
		{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map<String, String> params = new HashMap();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String)iter.next();
				String[] values = (String[])requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			int proxyid = 0;
			if(request.getParameter("proxyid")==null){
				result.put("re", 0);
				return result;
			}else{
				proxyid = Integer.valueOf(request.getParameter("proxyid"));
				MemberProxyVip memberProxyVip = this.memberProxyVipManager.getByMemberIdAndProxyMemberId(member.getMember_id(), proxyid);
				if(memberProxyVip==null){
					result.put("re", 2);
					return result;
				}else if(memberProxyVip.getV3LT().equals("0")==true&&memberProxyVip.getV2LT().equals("0")==true&&memberProxyVip.getV1LT().equals("0")==true){
					result.put("re", 3);
					return result;
				}
				result.put("re", 1);
				VipLevelDetail vipLevelDetail = this.vipLevelDetailManager.getByMemberId(proxyid);
				if(memberProxyVip.getV3LT().equals("0")!=true){
					result.put("level",3);
					result.put("vname",vipLevelDetail.getV3Name());
					result.put("vcontent",vipLevelDetail.getV3Content());
					return result;
				}else if(memberProxyVip.getV2LT().equals("0")!=true){
					result.put("level",2);
					result.put("vname",vipLevelDetail.getV2Name());
					result.put("vcontent",vipLevelDetail.getV2Content());
					return result;
				}else{
					result.put("level",1);
					result.put("vname",vipLevelDetail.getV1Name());
					result.put("vcontent",vipLevelDetail.getV1Content());
					return result;
				}
			}		
		}catch (Exception e){
			this.logger.error("查询失败！", e);
		}
		return result;
	}

	public IMemberProxyVipManager getMemberProxyVipManager() {
		return memberProxyVipManager;
	}

	public void setMemberProxyVipManager(
			IMemberProxyVipManager memberProxyVipManager) {
		this.memberProxyVipManager = memberProxyVipManager;
	}

	public IVipLevelDetailManager getVipLevelDetailManager() {
		return vipLevelDetailManager;
	}

	public void setVipLevelDetailManager(
			IVipLevelDetailManager vipLevelDetailManager) {
		this.vipLevelDetailManager = vipLevelDetailManager;
	}
}
