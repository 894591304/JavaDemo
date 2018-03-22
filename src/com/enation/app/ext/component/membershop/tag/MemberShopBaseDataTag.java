package com.enation.app.ext.component.membershop.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberShopBaseDataTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	private IFollowManager followManager;
	private IMemberExtManager memberExtManager;
	
	protected Object exec(Map params) throws TemplateModelException {
		try {
			Member member = UserServiceFactory.getUserService().getCurrentMember();
			Integer membershopId = getMemberShopId();
			MemberShop memberShop = this.memberShopManager.get(membershopId);
			Map result = new HashMap();	
			if(memberShop.getShowOrHide()==0){
				result.put("show",0);
				return result;
			}
			result.put("show",1);
			if(memberShop==null){
				throw new UrlNotFoundException();
			}
			int fans = this.followManager.getFansNum(membershopId);
			Member member2 = this.memberExtManager.getByMemberid(memberShop.getMemberId());
			int regtime = Integer.valueOf(String.valueOf(member2.getRegtime()));
			int mid = member2.getMember_id();
			int r1 = regtime/100%4800+200;
			int r2 = regtime/100/mid%2490+10;
			int level = this.userAccountManager.getLevel(membershopId);
			int AllSale = this.proxyManager.getAllSaleByMemberid(membershopId);
			
			String uname = this.memberExtManager.getUnameByMemberid(memberShop.getMemberId());
			List levellist = new ArrayList();
			List levellist2 = new ArrayList();
			Map list1 = new HashMap();
			Map list2 = new HashMap();
			for(int i=0;i<level+2;i++){
				list1.put("level",i);
				levellist.add(list1);
				}
			for(int j=0;j<3-level;j++){
				list2.put("level", j);
				levellist2.add(list2);
			}
			if(member!=null){
				int memberid = member.getMember_id();
				if(memberid==memberShop.getMemberId()){
					result.put("follow", 2);
				}else{
					if(this.followManager.checkFollow(memberid, memberShop.getMemberId())==1){
						result.put("follow",1);
					}else{
						result.put("follow",0);
					}
				}
			}else{
				result.put("follow",0);
			}
			List<Proxy> plist = this.proxyManager.getAllByMemberid(memberShop.getMemberId());
			result.put("uname", uname);
			result.put("shopname",memberShop.getShopName());
			result.put("shopid",String.valueOf(memberShop.getMemberId()));
			result.put("shoplabel",memberShop.getLabel());
			result.put("shopintro", memberShop.getShopIntro());
			result.put("memberimg", memberShop.getMemberImg());
			result.put("shopimg", memberShop.getShopImg());
			result.put("proxynum", plist.size());
			result.put("level", level);
			result.put("levellist", levellist);
			result.put("levellist2", levellist2);
			result.put("fans",fans*5+r1);
			result.put("allsale", AllSale*3+r2);
			getRequest().setAttribute("shopid",memberShop.getMemberId());
			return result;
		}
		catch (ObjectNotFoundException e) {
			throw new UrlNotFoundException();
		}
	} 
	
	private Integer getMemberShopId()
	{
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String membershopId = paseMemberShopId(url);
		return Integer.valueOf(membershopId);
	}
	
	private static String paseMemberShopId(String url){
		String pattern = "(-)(\\d+)";
		String value = null;
		Pattern p = Pattern.compile(pattern,34);
		Matcher m = p.matcher(url);
		if(m.find()){
			value = m.group(2);
		}
		return value;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IFollowManager getFollowManager() {
		return followManager;
	}

	public void setFollowManager(IFollowManager followManager) {
		this.followManager = followManager;
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}
}
