package com.enation.app.b2b2c.component.order.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.b2b2c.component.order.service.IOrderExtManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrderExtAction extends WWAction {
	private IOrderManager orderManager;
	private IOrderExtManager orderExtManager;
	private IAdminUserManager adminUserManager;
	private IAgentManager agentManager;
	private Map orderMap;
	private String sn;
	private String start_time;
	private String end_time;
	private String ship_name;
	private Integer status = null;
	private String status_arr;
	private Integer paystatus = null;
	private Integer shipstatus = null;
	private String shipstatus_arr;
	private Integer shipping_type = null;
	private Integer payment_id = null;
	private Integer stype;
	private String keyword;
	private String complete;

	public String listJson() {
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		this.orderMap = new HashMap();
		this.orderMap.put("stype", this.stype);
		this.orderMap.put("keyword", this.keyword);
		this.orderMap.put("start_time", this.start_time);
		this.orderMap.put("end_time", this.end_time);
		this.orderMap.put("status", this.status);
		this.orderMap.put("sn", this.sn);
		this.orderMap.put("ship_name", this.ship_name);
		this.orderMap.put("paystatus", this.paystatus);
		this.orderMap.put("shipstatus", this.shipstatus);
		this.orderMap.put("shipping_type", this.shipping_type);
		this.orderMap.put("payment_id", this.payment_id);
		this.orderMap.put("order_state", requst.getParameter("order_state"));
		this.orderMap.put("complete", this.complete);
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.orderMap.put("agentid", currentUser.getUserid());
		}
		this.webpage = this.orderExtManager.listOrder(this.orderMap, getPage(), getPageSize(), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}
	
	public String listJson2() {
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		this.orderMap = new HashMap();
		this.orderMap.put("stype", this.stype);
		this.orderMap.put("keyword", this.keyword);
		this.orderMap.put("start_time", this.start_time);
		this.orderMap.put("end_time", this.end_time);
		this.orderMap.put("status", this.status_arr);
		this.orderMap.put("shipstatus", this.shipstatus_arr);
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.orderMap.put("agentid", currentUser.getUserid());
		}
		this.webpage = this.orderExtManager.listOrder2(this.orderMap, getPage(), getPageSize(), getSort(), getOrder());
		JSONArray result = JSONArray.fromObject(this.webpage.getResult());
		JSONArray newResult = new JSONArray();
		for(int i=0;i<result.size();i++){
			JSONObject obj = JSONObject.fromObject(result.get(i));
			String order_id = obj.get("order_id").toString();
			String uname = this.orderExtManager.getMemberNameByOrderId(order_id);
			obj.put("uname", uname);
			newResult.add(obj);
		}
		this.json = ("{\"total\":" + this.webpage.getTotalCount() + ",\"rows\":" + newResult.toString() + "}");
		return "json_message";
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IOrderExtManager getOrderExtManager() {
		return orderExtManager;
	}

	public void setOrderExtManager(IOrderExtManager orderExtManager) {
		this.orderExtManager = orderExtManager;
	}

	public Map getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map orderMap) {
		this.orderMap = orderMap;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	public Integer getShipstatus() {
		return shipstatus;
	}

	public void setShipstatus(Integer shipstatus) {
		this.shipstatus = shipstatus;
	}

	public Integer getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(Integer shipping_type) {
		this.shipping_type = shipping_type;
	}

	public Integer getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}

	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public String getStatus_arr() {
		return status_arr;
	}

	public void setStatus_arr(String status_arr) {
		this.status_arr = status_arr;
	}

	public String getShipstatus_arr() {
		return shipstatus_arr;
	}

	public void setShipstatus_arr(String shipstatus_arr) {
		this.shipstatus_arr = shipstatus_arr;
	}

}
