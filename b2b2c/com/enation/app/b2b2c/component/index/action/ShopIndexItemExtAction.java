package com.enation.app.b2b2c.component.index.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.b2b2c.component.index.service.impl.IndexItemsExtManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

import net.sf.json.JSONArray;

public class ShopIndexItemExtAction extends WWAction {
	private IndexItemsExtManager indexItemsExtManager;
	private IDlyTypeManager dlyTypeManager;
	private IPaymentManager paymentManager;
	private IAdminUserManager adminUserManager;
	private IAgentManager agentManager;
	private IBrokerageManager brokerageManager;
	private AdminUser currentUser;
	private String userType;
	private Map orderss;
	private Map goodsss;
	private Map sqlesss;
	private Map creditsss;
	private Map proxysss;
	private String type;
	private String today;
	private String firstday;
	private String lastday;
	private Map statusMap;
	private String status_Json;
	private Map payStatusMap;
	private String payStatus_Json;
	private Map shipMap;
	private String ship_Json;
	private List<DlyType> shipTypeList;
	private List<PayCfg> payTypeList;
	
	
	public String order() {
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.orderss = this.indexItemsExtManager.censusState();
		return "order";
	}

	public String goods() {
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.goodsss = this.indexItemsExtManager.census();
		return "goods";
	}
	
	public String sale() {
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.sqlesss = this.indexItemsExtManager.saleState();
		return "sale";
	}
	
	public String credit(){
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.creditsss = this.indexItemsExtManager.creditState();
		return "credit";
	}
	
	public String proxy(){
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.proxysss = this.indexItemsExtManager.proxyState();
		return "proxy";
	}
	
	public String agentType(){
		this.userType = "guest";
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.currentUser.getFounder()==1) {
			this.userType = "admin";
		}
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.userType = "broker";
		}
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			this.userType = "agent";
		}
		this.json=JsonMessageUtil.getStringJson("result", this.userType);
		return "json_message";
	}

	public String order_detail() {
		Calendar now = Calendar.getInstance();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		this.today = sdf.format(now.getTime());
		now = Calendar.getInstance();  
		now.add(Calendar.MONTH, 0);  
        now.set(Calendar.DAY_OF_MONTH, 1);  
        this.firstday = sdf.format(now.getTime());
        now = Calendar.getInstance();  
        now.add(Calendar.MONTH, 1);  
        now.set(Calendar.DAY_OF_MONTH, 0);  
        this.lastday = sdf.format(now.getTime());
        
        if (this.statusMap == null) {
            this.statusMap = new HashMap();
            this.statusMap = getStatusJson();
            String p = JSONArray.fromObject(this.statusMap).toString();
            this.status_Json = p.replace("[", "").replace("]", "");
          }
          
          if (this.payStatusMap == null) {
            this.payStatusMap = new HashMap();
            this.payStatusMap = getpPayStatusJson();
            String p = JSONArray.fromObject(this.payStatusMap).toString();
            this.payStatus_Json = p.replace("[", "").replace("]", "");
          }
          
         if (this.shipMap == null) {
            this.shipMap = new HashMap();
            this.shipMap = getShipJson();
            String p = JSONArray.fromObject(this.shipMap).toString();
            this.ship_Json = p.replace("[", "").replace("]", "");
          }
          
          this.shipTypeList = this.dlyTypeManager.list();
          this.payTypeList = this.paymentManager.list();
        
		return "order_detail";
	}
	
	private Map getStatusJson()
	{
	 Map orderStatus = new HashMap();
	 
	 orderStatus.put("0", OrderStatus.getOrderStatusText(0));
	 orderStatus.put("9", OrderStatus.getOrderStatusText(9));
	 orderStatus.put("2", OrderStatus.getOrderStatusText(2));
	 orderStatus.put("4", OrderStatus.getOrderStatusText(4));
	 orderStatus.put("5", OrderStatus.getOrderStatusText(5));
	 orderStatus.put("6", OrderStatus.getOrderStatusText(6));
	 orderStatus.put("-2", OrderStatus.getOrderStatusText(-2));
	 orderStatus.put("7", OrderStatus.getOrderStatusText(7));
	 orderStatus.put("-1", OrderStatus.getOrderStatusText(-1));
	 orderStatus.put("8", OrderStatus.getOrderStatusText(8));
	 orderStatus.put("-7", OrderStatus.getOrderStatusText(-7));
	 orderStatus.put("-4", OrderStatus.getOrderStatusText(-4));
	 orderStatus.put("-3", OrderStatus.getOrderStatusText(-3));
	 orderStatus.put("1", OrderStatus.getOrderStatusText(1));
	 return orderStatus;
	}


	private Map getpPayStatusJson()
	{
	 Map pmap = new HashMap();
	 pmap.put("0", OrderStatus.getPayStatusText(0));
	 
	 pmap.put("2", OrderStatus.getPayStatusText(2));
	 pmap.put("3", OrderStatus.getPayStatusText(3));
	 pmap.put("5", OrderStatus.getPayStatusText(5));
	 
	 return pmap;
	}


	private Map getShipJson() {
	 Map map = new HashMap();
	 map.put("0", OrderStatus.getShipStatusText(0));
	 map.put("1", OrderStatus.getShipStatusText(1));
	 map.put("2", OrderStatus.getShipStatusText(2));
	 map.put("3", OrderStatus.getShipStatusText(3));
	 map.put("4", OrderStatus.getShipStatusText(4));
	 map.put("5", OrderStatus.getShipStatusText(5));
	 map.put("3", OrderStatus.getShipStatusText(3));
	 map.put("4", OrderStatus.getShipStatusText(4));
	 map.put("8", OrderStatus.getShipStatusText(8));
	 map.put("9", OrderStatus.getShipStatusText(9));
	 return map;
	}



	public IndexItemsExtManager getIndexItemsExtManager() {
		return indexItemsExtManager;
	}

	public void setIndexItemsExtManager(IndexItemsExtManager indexItemsExtManager) {
		this.indexItemsExtManager = indexItemsExtManager;
	}

	public Map getOrderss() {
		return orderss;
	}

	public void setOrderss(Map orderss) {
		this.orderss = orderss;
	}

	public Map getGoodsss() {
		return goodsss;
	}

	public void setGoodsss(Map goodsss) {
		this.goodsss = goodsss;
	}

	public Map getSqlesss() {
		return sqlesss;
	}

	public void setSqlesss(Map sqlesss) {
		this.sqlesss = sqlesss;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getFirstday() {
		return firstday;
	}

	public void setFirstday(String firstday) {
		this.firstday = firstday;
	}

	public String getLastday() {
		return lastday;
	}

	public void setLastday(String lastday) {
		this.lastday = lastday;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public Map getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}

	public String getStatus_Json() {
		return status_Json;
	}

	public void setStatus_Json(String status_Json) {
		this.status_Json = status_Json;
	}

	public Map getPayStatusMap() {
		return payStatusMap;
	}

	public void setPayStatusMap(Map payStatusMap) {
		this.payStatusMap = payStatusMap;
	}

	public String getPayStatus_Json() {
		return payStatus_Json;
	}

	public void setPayStatus_Json(String payStatus_Json) {
		this.payStatus_Json = payStatus_Json;
	}

	public Map getShipMap() {
		return shipMap;
	}

	public void setShipMap(Map shipMap) {
		this.shipMap = shipMap;
	}

	public String getShip_Json() {
		return ship_Json;
	}

	public void setShip_Json(String ship_Json) {
		this.ship_Json = ship_Json;
	}

	public List<DlyType> getShipTypeList() {
		return shipTypeList;
	}

	public void setShipTypeList(List<DlyType> shipTypeList) {
		this.shipTypeList = shipTypeList;
	}

	public List<PayCfg> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<PayCfg> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
	}

	public AdminUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(AdminUser currentUser) {
		this.currentUser = currentUser;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Map getCreditsss() {
		return creditsss;
	}

	public void setCreditsss(Map creditsss) {
		this.creditsss = creditsss;
	}

	public Map getProxysss() {
		return proxysss;
	}

	public void setProxysss(Map proxysss) {
		this.proxysss = proxysss;
	}
	
}
