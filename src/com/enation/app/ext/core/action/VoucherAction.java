package com.enation.app.ext.core.action;

import java.util.Date;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxyorder.model.ProxyOrder;
import com.enation.app.ext.component.proxyorder.service.IProxyOrderManager;
import com.enation.app.ext.component.ticketgive.model.TicketGive;
import com.enation.app.ext.component.ticketgive.service.ITicketGiveManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

public class VoucherAction extends WWAction{

	
	private static final long serialVersionUID = -7148690879657164684L;

	private ITicketDetailManager ticketDetailManager;
	private IProxyOrderManager proxyOrderManager;
	private ITicketGiveManager ticketGiveManager;
	
	private int ticketid;
	private int addressid;
	private String addressmark;
	private String name;
	private String tel;
	private String province;
	private String city;
	private String area;
	private String address;
	private String remark;
	
	public String getVoucher()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return"json_message";
		}
		int check = checkTicket(this.ticketid,member.getMember_id());
		if(check==0){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return"json_message";
		}
		if(check==2){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return"json_message";
		}
		if(StringUtil.isEmpty(this.addressmark)){
			showErrorJson("地址简称不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.name)){
			showErrorJson("收货人姓名不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.tel)){
			showErrorJson("收货人电话不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.province)){
			showErrorJson("省/市不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.city)){
			showErrorJson("城市不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.area)){
			showErrorJson("地区不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.address)){
			showErrorJson("详细地址不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.remark)){
			remark="未备注";
		}
		ProxyOrder proxyOrder = this.proxyOrderManager.getByTicketId(ticketid);
		TicketDetail ticketDetail = this.ticketDetailManager.get(ticketid);
		long limittime = new Long(ticketDetail.getEnddate());
		long begintime = new Long(ticketDetail.getStartdate());
		Date ndate = new Date();
		long nowtime = ndate.getTime();
		if(nowtime>limittime){
			this.json=JsonMessageUtil.getStringJson("result", "4");
			return "json_message";
		}else if(nowtime<begintime){
			this.json=JsonMessageUtil.getStringJson("result", "5");
			return "json_message";
		}
		if(check==3){
			proxyOrder.setStatus(5);
		}else{
			proxyOrder.setStatus(1);
		}		
		this.proxyOrderManager.update(proxyOrder);
		String addressAll = this.province+"-"+this.city+"-"+this.area+"-"+this.address;
		ticketDetail.setRecName(this.name);
		ticketDetail.setRecTel(this.tel);
		ticketDetail.setRecAddress(addressAll);
		ticketDetail.setRemark(this.remark);
		this.ticketDetailManager.update(ticketDetail);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}

	public int checkTicket(int ticketid,int memberid){
		ProxyOrder proxyOrder = this.proxyOrderManager.getByTicketId(ticketid);
		TicketGive ticketGive = this.ticketGiveManager.getByProxyOrderId(proxyOrder.getId());
		int m1=0;
		if(ticketGive!=null){
			m1=ticketGive.getMemberId();
		}
		if(proxyOrder.getMemberId()==memberid){
			if(proxyOrder.getStatus()==0){
				return 1;
			}
			return 2;
		}else if(m1==memberid){
			if(proxyOrder.getStatus()==4){
				return 3;
			}
			return 2;
		}
		return 0;
	}
	
	
	
	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

	public IProxyOrderManager getProxyOrderManager() {
		return proxyOrderManager;
	}

	public void setProxyOrderManager(IProxyOrderManager proxyOrderManager) {
		this.proxyOrderManager = proxyOrderManager;
	}

	public int getTicketid() {
		return ticketid;
	}

	public void setTicketid(int ticketid) {
		this.ticketid = ticketid;
	}

	public int getAddressid() {
		return addressid;
	}

	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}

	public String getAddressmark() {
		return addressmark;
	}

	public void setAddressmark(String addressmark) {
		this.addressmark = addressmark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ITicketGiveManager getTicketGiveManager() {
		return ticketGiveManager;
	}

	public void setTicketGiveManager(ITicketGiveManager ticketGiveManager) {
		this.ticketGiveManager = ticketGiveManager;
	}
	
	
}
