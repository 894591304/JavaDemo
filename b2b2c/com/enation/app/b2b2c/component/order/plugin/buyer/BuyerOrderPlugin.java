package com.enation.app.b2b2c.component.order.plugin.buyer;

import java.util.Map;

import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.plugin.order.IOrderTabShowEvent;
import com.enation.app.shop.core.plugin.order.IShowOrderDetailHtmlEvent;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.plugin.AutoRegisterPlugin;

public class BuyerOrderPlugin extends AutoRegisterPlugin
		implements IOrderTabShowEvent, IShowOrderDetailHtmlEvent {

	private ITicketDetailManager ticketDetailManager;

	@Override
	public String onShowOrderDetailHtml(Order order) {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setClz(getClass());
		Map<String,Object> ticketDetail = this.ticketDetailManager.getTicketByOrderId(order.getOrder_id());
		freeMarkerPaser.putData("ticketDetail", ticketDetail);
		freeMarkerPaser.setPageName("buyer_info");
		return freeMarkerPaser.proessPageContent();
	}

	@Override
	public boolean canBeExecute(Order arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String getTabName(Order arg0) {
		// TODO Auto-generated method stub
		return "收货人信息";
	}

	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

}
