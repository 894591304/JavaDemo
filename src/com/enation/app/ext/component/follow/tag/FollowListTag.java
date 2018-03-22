package com.enation.app.ext.component.follow.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.follow.model.Follow;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class FollowListTag extends BaseFreeMarkerTag{

	private IFollowManager followManager;
	private IMemberExtManager memberExtManager;
	private IMemberShopManager memberShopManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[FollowListTag]");
		}
		Map result = new HashMap();
		int listnum = 10;
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
			List uList = new ArrayList();
			int memberId = member.getMember_id().intValue();
			List<Follow> followList = this.followManager.getFollowListByMemberId(memberId);
			int total = followList.size();
			int allPage = total/listnum;
			int num=0;
			int pn = 0;
			int msgnum = 0;
			for(int i=0;i<total;i++)
			{
				msgnum = msgnum+followList.get(i).getMsg();
				if(num>=listnum){}else{
				if(pn<(page-1)*listnum){pn++;}else{
				Map mlist = new HashMap();
				int fid = followList.get(i).getFollowId();
				mlist.put("followid",String.valueOf(fid));
				mlist.put("shopname",this.memberShopManager.get(fid).getShopName());
				mlist.put("uname",this.memberExtManager.getUnameByMemberid(fid));
				mlist.put("memberimg", this.memberShopManager.get(fid).getMemberImg());
				mlist.put("label", this.memberShopManager.get(fid).getLabel());
				mlist.put("msg", followList.get(i).getMsg());
				uList.add(mlist);
				num++;}}
			}
			result.put("allpage",String.valueOf(allPage+1));
			result.put("nowpage",String.valueOf(page+1));
			result.put("total", total);
			result.put("msgnum", String.valueOf(msgnum));
			result.put("memberlist", uList);
			return result;
		}catch (Exception e){
			List uList = new ArrayList();
			int memberId = member.getMember_id().intValue();
			List<Follow> followList = this.followManager.getFollowListByMemberId(memberId);
			int total = followList.size();
			int allPage = total/listnum;
			int num=0;
			int pn = 0;
			int msgnum = 0;
			for(int i=0;i<total;i++)
			{
				msgnum = msgnum+followList.get(i).getMsg();
				if(num>=listnum){}else{
				Map mlist = new HashMap();
				int fid = followList.get(i).getFollowId();
				mlist.put("followid",String.valueOf(fid));
				mlist.put("shopname",this.memberShopManager.get(fid).getShopName());
				mlist.put("uname",this.memberExtManager.getUnameByMemberid(fid));
				mlist.put("memberimg", this.memberShopManager.get(fid).getMemberImg());
				mlist.put("label", this.memberShopManager.get(fid).getLabel());
				mlist.put("msg", followList.get(i).getMsg());
				uList.add(mlist);
				num++;}
			}
			result.put("allpage",String.valueOf(allPage+1));
			result.put("nowpage",1+1);
			result.put("total", total);
			result.put("msgnum", msgnum);
			result.put("memberlist", uList);
			return result;			
		}
	}
	
	public IFollowManager getFollowManager()
	{
		return this.followManager;
	}
	
	public void setFollowManager(IFollowManager followManager)
	{
		this.followManager = followManager;
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}
}
