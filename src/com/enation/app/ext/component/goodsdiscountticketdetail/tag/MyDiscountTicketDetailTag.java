package com.enation.app.ext.component.goodsdiscountticketdetail.tag;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyDiscountTicketDetailTag extends BaseFreeMarkerTag{

	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	private IGoodsManager goodsManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[DiscountTicketDetail1Tag]");
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
			int ticketid = 0;
			if(request.getParameter("ticketid")==null){
				result.put("re", 2); //参数错误
				return result;
			}
			ticketid = Integer.valueOf(request.getParameter("ticketid"));
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.goodsDiscountTicketDetailManager.get(ticketid);
			if(goodsDiscountTicketDetail==null){
				result.put("re", 2); //参数错误
				return result;
			}
			if(goodsDiscountTicketDetail.getBelongMemberId()!=member.getMember_id()){
				result.put("re", 3); //现金券不属于
				return result;
			}
			Proxy proxy = this.proxyManager.get(goodsDiscountTicketDetail.getProxyId());
			Goods goods = this.goodsManager.getGoods(goodsDiscountTicketDetail.getGoodsId());
			GoodsAgent goodsAgent = this.goodsAgentManager.get(goodsDiscountTicketDetail.getGoodsId());
			result.put("re", 1);
			result.put("ticketid",goodsDiscountTicketDetail.getId());
			result.put("img",goods.getOriginal());
			result.put("name",goods.getName());
			result.put("value",goodsDiscountTicketDetail.getTicketValue());
			SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
			String begintime = format.format(Long.valueOf(proxy.getProxyBeginTime())*1000);
			String endtime = format.format(Long.valueOf(proxy.getProxyTestTime())*1000);
			result.put("begintime",begintime);
			result.put("endtime", endtime);
			result.put("proxyid",proxy.getId());
			result.put("code",goodsDiscountTicketDetail.getDiscountTicketId());
			result.put("send",goodsDiscountTicketDetail.getSendStatus());
			result.put("sendcode",goodsDiscountTicketDetail.getSendKey());
			result.put("info",goodsAgent.getTicketCaption());
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

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
}
