package com.enation.app.ext.component.memberproxyvip.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ProxyVIPListTag extends BaseFreeMarkerTag{

	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private IMemberProxyVipManager memberProxyVipManager;
	private IMemberManager memberManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		if(member == null){
			result.put("re", 0); //未登录
		}
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
			int ticketid = 0;
			if(request.getParameter("ticketid")==null){
				result.put("re", 2); //参数错误
				return result;
			}
			ticketid = Integer.valueOf(request.getParameter("ticketid"));
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.get(ticketid);
			if(goodsDiscountTicketDetail.getGiveStatus()!=0){
				result.put("re", 3); //已发送
				return result;
			}
			result.put("re",1);
			List<MemberProxyVip> mList = this.memberProxyVipManager.getByProxyMemberId(goodsDiscountTicketDetail.getProxyMemberId());
			List mpList = new ArrayList();
			int count = mList.size();
			for(int i=0;i<count;i++){
				Map mMap = new HashMap();
				MemberProxyVip memberProxyVip = mList.get(i);
				Member m = this.memberManager.get(memberProxyVip.getMemberId());
				String img = m.getFace();
				if(img==null){
					img = "/img/face/default.jpg";
				}
				if(memberProxyVip.getV3LT().equals("0")!=true){
					mMap.put("level",3);
				}else if(memberProxyVip.getV2LT().equals("0")!=true){
					mMap.put("level",2);
				}else if(memberProxyVip.getV1LT().equals("0")!=true){
					mMap.put("level",1);
				}else{
					mMap.put("level",0);
				}
				mMap.put("img",m.getFace());
				mMap.put("name",m.getName());
				mMap.put("id",m.getMember_id());
				mMap.put("exp",memberProxyVip.getVipEX());
				mpList.add(mMap);
			}
			result.put("proxyid",goodsDiscountTicketDetail.getProxyId());
			result.put("ticketid",ticketid);
			result.put("mplist",mpList);
		}catch (Exception e){
			this.logger.error("查询失败！", e);
		}
		return result;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
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
