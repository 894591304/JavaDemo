package com.enation.app.ext.component.goodsdiscountticketdetail.tag;

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
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class DiscountTicketDetail3Tag  extends BaseFreeMarkerTag{

	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
		
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
			String ticketOption = goodsAgent.getTicketOption();
			if(ticketOption==null){
				result.put("re", 3); //此商品无法生成现金券
				return result;
			}
			String[] tOption = ticketOption.split("/");
			int tlength =tOption.length;
			if(tlength<3){
				result.put("re", 0); //没有第三个现金券价格
				return result;
			}
			List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getByProxyIdAndTicketValue(proxyid,Float.valueOf(tOption[2]));
			if(gList.size()==0){
				result.put("re",0);  //第三个现金券数量为空
				return result;
			}
			result.put("ticketvalue",tOption[2]);
			result.put("re",1);
			List tList = new ArrayList();
			int count = gList.size();
			int havesend = 0;
			int notsend = 0;
			for(int i=0;i<count;i++){
				GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(i);
				Map tMap = new HashMap();
				tMap.put("id",goodsDiscountTicketDetail.getId());
				tMap.put("ticketid",goodsDiscountTicketDetail.getDiscountTicketId());
				tMap.put("givestatus",goodsDiscountTicketDetail.getGiveStatus());
				tList.add(tMap);
				if(goodsDiscountTicketDetail.getGiveStatus()==0){
					notsend++;
				}else{
					havesend++;
				}
			}
			result.put("tlist",tList);
			result.put("havesend",havesend);
			result.put("notsend",notsend);
			result.put("option",3);
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
	
}
