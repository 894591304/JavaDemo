package com.enation.app.ext.component.goodsdiscountticketdetail.tag;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class DiscountTicketDetailTag extends BaseFreeMarkerTag{

	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private IGoodsProxyManager goodsProxyManager;
		
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[DiscountTicketDetailTag]");
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
				result.put("re", 2); //参数错误
				return result;
			}
			proxyid = Integer.valueOf(request.getParameter("proxyid"));
			Proxy proxy = this.proxyManager.get(proxyid);
			if(proxy==null){
				result.put("re", 2); //参数错误
				return result;
			}
			GoodsAgent goodsAgent = this.goodsAgentManager.get(proxy.getGoodsId());
			List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getByProxyId(proxy.getId());
			int c = this.goodsDiscountTicketDetailManager.getTicketNum(proxy.getId());
			String ticketOption = goodsAgent.getTicketOption();
			if(ticketOption==null){
				result.put("re", 3); //此商品无法生成现金券
				return result;
			}
			String[] tOption = ticketOption.split("/");
			int tlength =tOption.length;
			List valueList = new ArrayList();
			for(int i=0;i<tlength;i++){
				Map tMap = new HashMap();
				tMap.put("value",tOption[i]);
				valueList.add(tMap);
			}
			Goods goods = this.goodsProxyManager.getGoods(proxy.getGoodsId());
			result.put("re", 1);
			result.put("name",goods.getName());
			result.put("proxyid",proxy.getId());
			result.put("goodsamount",proxy.getGoodsAmount());
			result.put("cancreate",proxy.getOnSale()-c);
			result.put("valuelist",valueList);
			result.put("havecreate",gList.size());
			SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
			String begintime = format.format(Long.valueOf(proxy.getProxyBeginTime())*1000);
			String endtime = format.format(Long.valueOf(proxy.getProxyTestTime())*1000);
			result.put("begintime",begintime);
			result.put("endtime", endtime);
			
		}catch (Exception e){
			this.logger.error("查询失败！", e);
		}
		return result;
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

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

}
