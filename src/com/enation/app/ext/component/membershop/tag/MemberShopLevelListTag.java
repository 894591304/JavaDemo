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
public class MemberShopLevelListTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	private IMemberExtManager memberExtManager;
	private IFollowManager followManager;

	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		int listnum = 20;
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
			int page = 1;
			request.setCharacterEncoding("utf-8");
			page = new Integer(request.getParameter("page"));			
			List sList = new ArrayList();
			List<MemberShop> shopLevelList = this.memberShopManager.OrderByLevel();
			int total = shopLevelList.size();
			int allPage = total/listnum;
			int num=0;
			int pn = 0;
			if(member!=null)
			{
				for(int i=0;i<total;i++)
				{
					if(num>=listnum){break;}else{
						if(pn<(page-1)*listnum){
							pn++;
						}else{
					if(member.getMember_id()==shopLevelList.get(i).getMemberId()){					
					}else{
						Map shoplist = new HashMap();
						shoplist.put("memberid", shopLevelList.get(i).getMemberId());
						shoplist.put("memberimg", shopLevelList.get(i).getMemberImg());
						shoplist.put("label", shopLevelList.get(i).getLabel());
						shoplist.put("shopname", shopLevelList.get(i).getShopName());
						shoplist.put("uname", this.memberExtManager.getUnameByMemberid(shopLevelList.get(i).getMemberId()));
						if(this.followManager.checkFollow(member.getMember_id(),shopLevelList.get(i).getMemberId())==1){
							shoplist.put("guanzhu","1");
						}else{shoplist.put("guanzhu","0");}
						sList.add(shoplist);
						num++;
					}}}
				}
			}else{
				for(int i=0;i<total;i++)
				{
					if(num>=listnum){break;}else{
						if(pn<(page-1)*listnum){
							pn++;
						}else{
					Map shoplist = new HashMap();
					shoplist.put("memberid", shopLevelList.get(i).getMemberId());
					shoplist.put("memberimg", shopLevelList.get(i).getMemberImg());
					shoplist.put("label", shopLevelList.get(i).getLabel());
					shoplist.put("shopname", shopLevelList.get(i).getShopName());
					shoplist.put("uname", this.memberExtManager.getUnameByMemberid(shopLevelList.get(i).getMemberId()));
					shoplist.put("guanzhu","0");
					sList.add(shoplist);
					num++;}}
				}
			}
			result.put("allpage",allPage+1);
			result.put("nowpage",page+1);
			result.put("shopLevelList",sList);
			return result;
		}catch (Exception e){
			List sList = new ArrayList();
			List<MemberShop> shopLevelList = this.memberShopManager.OrderByLevel();
			int total = shopLevelList.size();
			int allPage = total/listnum;
			int num=0;
			if(member!=null)
			{
				for(int i=0;i<total;i++)
				{
					if(num>=listnum){break;}else{
					if(member.getMember_id()==shopLevelList.get(i).getMemberId()){					
					}else{
						Map shoplist = new HashMap();
						shoplist.put("memberid", shopLevelList.get(i).getMemberId());
						shoplist.put("memberimg", shopLevelList.get(i).getMemberImg());
						shoplist.put("label", shopLevelList.get(i).getLabel());
						shoplist.put("shopname", shopLevelList.get(i).getShopName());
						shoplist.put("uname", this.memberExtManager.getUnameByMemberid(shopLevelList.get(i).getMemberId()));
						if(this.followManager.checkFollow(member.getMember_id(),shopLevelList.get(i).getMemberId())==1){
							shoplist.put("guanzhu","1");
						}else{shoplist.put("guanzhu","0");}
						sList.add(shoplist);
						num++;
					}}
				}
			}else{
				for(int i=0;i<total;i++)
				{
					if(num>=listnum){break;}else{
					Map shoplist = new HashMap();
					shoplist.put("memberid", shopLevelList.get(i).getMemberId());
					shoplist.put("memberimg", shopLevelList.get(i).getMemberImg());
					shoplist.put("label", shopLevelList.get(i).getLabel());
					shoplist.put("shopname", shopLevelList.get(i).getShopName());
					shoplist.put("uname", this.memberExtManager.getUnameByMemberid(shopLevelList.get(i).getMemberId()));
					shoplist.put("guanzhu","0");
					sList.add(shoplist);
					num++;}
				}
			}
			result.put("allpage",allPage+1);
			result.put("nowpage",1+1);
			result.put("shopLevelList",sList);
			return result;
		}
	}

	public IMemberShopManager getMemberShopManager()
	{
		return this.memberShopManager;
	}
	
	public void setMemberShopManager(IMemberShopManager memberShopManager)
	{
		this.memberShopManager = memberShopManager;
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
}
