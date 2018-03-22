package com.enation.app.ext.component.goodsdiscountticketdetail.tag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyDiscountTicketListTag extends BaseFreeMarkerTag{

	private IGoodsAgentManager goodsAgentManager;
	private IGoodsManager goodsManager;
	private IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager;
	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[DiscountTicketDetail1Tag]");
		}
		Map result = new HashMap();
		List<GoodsDiscountTicketDetail> gList = this.goodsDiscountTicketDetailManager.getByMemberId(member.getMember_id());
		List notUsedList = new ArrayList();
		int notUsedcount = 0;
		List outOfTimeList = new ArrayList();
		int outOfTimecount = 0;
		List usedList = new ArrayList();
		int usedcount = 0;
		int count = gList.size();
		for(int i=0;i<count;i++){
			Map tMap = new HashMap();
			GoodsDiscountTicketDetail goodsDiscountTicketDetail = gList.get(i);
			GoodsAgent goodsAgent = this.goodsAgentManager.get(goodsDiscountTicketDetail.getGoodsId());
			Proxy proxy = this.proxyManager.get(goodsDiscountTicketDetail.getProxyId());
			Goods goods = this.goodsManager.getGoods(goodsDiscountTicketDetail.getGoodsId());
			tMap.put("value",goodsDiscountTicketDetail.getTicketValue());
			tMap.put("name",goods.getName());
			SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
			String endtime = format.format(Long.valueOf(proxy.getProxyTestTime())*1000);
			tMap.put("endtime", endtime);
			tMap.put("proxyid", proxy.getId());
			tMap.put("ticketid", goodsDiscountTicketDetail.getId());
			if(goodsDiscountTicketDetail.getUseStatus()==0){
				notUsedList.add(tMap);
				notUsedcount++;
			}else if(goodsDiscountTicketDetail.getUseStatus()==1){
				usedList.add(tMap);
				usedcount++;
			}else{
				outOfTimeList.add(tMap);
				outOfTimecount++;
			}
		}
		result.put("notuselist",notUsedList);
		result.put("notusecount",notUsedcount);
		result.put("outoftimelist",outOfTimeList);
		result.put("outoftimecount",outOfTimecount);
		result.put("usedlist",usedList);
		result.put("usedcount",usedcount);
		return result;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IGoodsDiscountTicketDetailManager getGoodsDiscountTicketDetailManager() {
		return goodsDiscountTicketDetailManager;
	}

	public void setGoodsDiscountTicketDetailManager(
			IGoodsDiscountTicketDetailManager goodsDiscountTicketDetailManager) {
		this.goodsDiscountTicketDetailManager = goodsDiscountTicketDetailManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
