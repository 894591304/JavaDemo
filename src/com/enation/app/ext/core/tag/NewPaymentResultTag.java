package com.enation.app.ext.core.tag;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxyorder.service.INewOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PaymentResult;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.eop.processor.core.Request;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NewPaymentResultTag extends BaseFreeMarkerTag
{

	private INewOrderManager newOrderManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[NewPaymentResultTag]");
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
			String ordersn = null;
			if(request.getParameter("out_trade_no")==null){
				ordersn = request.getParameter("ordersn");
			}else{
				ordersn = request.getParameter("out_trade_no");
			}
			Order order = this.newOrderManager.getBySnAndMemberid(ordersn,member.getMember_id());
			if(order!=null){
				if(order.getNeed_pay_money()<=order.getPaymoney()||order.getPay_status()==2)
				{
					result.put("payment",1);
					//System.out.println("订单已支付！");
				}
				else
				{
					result.put("payment",0);
					//System.out.println("订单未支付！");
				}
			}else{
				result.put("payment",2);
				//System.out.println("未查询到订单信息！");
			}
		}
		catch (Exception e)
		{
			this.logger.error("查询失败！", e);
		}
		return result; 
	}

	public INewOrderManager getNewOrderManager() {
		return newOrderManager;
	}

	public void setNewOrderManager(INewOrderManager newOrderManager) {
		this.newOrderManager = newOrderManager;
	}
}
