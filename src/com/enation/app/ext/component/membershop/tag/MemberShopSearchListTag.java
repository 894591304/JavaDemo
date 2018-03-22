package com.enation.app.ext.component.membershop.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberShopSearchListTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	private IMemberExtManager memberExtManager;
	private IFollowManager followManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
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
			String keyword = null;
			request.setCharacterEncoding("utf-8");
			keyword = new String(request.getParameter("keyword").getBytes("utf-8"),"utf-8");
			if(keyword==null){
				result.put("search",2);
				return result;
			}
			List<MemberShop> mList = this.memberShopManager.search(keyword);
			if(mList.size()==0){
				result.put("search",0);
				return result;
			}
			int total = mList.size();
			List memberShopList = new ArrayList();
			int num = 0;
			if(member!=null)
			{
				for(int i=0;i<total;i++){
					MemberShop memberShop = mList.get(i);
					if(member.getMember_id()==memberShop.getMemberId()){					
					}else{
						Map shopMap = new HashMap();
						shopMap.put("memberid", memberShop.getMemberId());
						shopMap.put("memberimg", memberShop.getMemberImg());
						shopMap.put("label", memberShop.getLabel());
						shopMap.put("shopname", memberShop.getShopName());
						shopMap.put("uname", this.memberExtManager.getUnameByMemberid(memberShop.getMemberId()));
						if(this.followManager.checkFollow(member.getMember_id(),memberShop.getMemberId())==1){
							shopMap.put("guanzhu","1");
						}else{shopMap.put("guanzhu","0");}
						memberShopList.add(shopMap);
						num++;
					}
				}
			}else{
				for(int i=0;i<total;i++)
				{
					MemberShop memberShop = mList.get(i);
					Map shopMap = new HashMap();
					shopMap.put("memberid", memberShop.getMemberId());
					shopMap.put("memberimg", memberShop.getMemberImg());
					shopMap.put("label", memberShop.getLabel());
					shopMap.put("shopname", memberShop.getShopName());
					shopMap.put("uname", this.memberExtManager.getUnameByMemberid(memberShop.getMemberId()));
					shopMap.put("guanzhu","0");
					memberShopList.add(shopMap);
					num++;
				}
			}
			if(num==0){
				result.put("search",0);
			}else{
				result.put("search",1);
			}
			result.put("total",num);
			result.put("membershoplist", memberShopList);
		}
		catch (Exception e)
		{
			this.logger.error("查询失败！", e);
			result.put("search", 3);
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


	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}


	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

}
