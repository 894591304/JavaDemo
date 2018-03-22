package com.enation.app.b2b2c.component.order.service;

import java.util.Map;

import com.enation.framework.database.Page;

public interface IOrderExtManager {
	public Page listOrder(Map map, int page, int pageSize, String other, String order);
	public Page listOrder2(Map map, int page, int pageSize, String other, String order);
	public String getMemberNameByOrderId(String orderId);
}
