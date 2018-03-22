package com.enation.app.ext.component.address.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.address.model.Address;
import com.enation.app.ext.component.address.service.IAddressManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

public class AddressAction extends WWAction{

	
	private static final long serialVersionUID = -2148677648570658877L;
	private IAddressManager addressManager;
	
	private int addressid;
	private String addressmark;
	private String name;
	private String tel;
	private String province;
	private String city;
	private String area;
	private String address;
	
	public String saveAddress()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
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
		Address address = new Address();
		address.setMemberId(member.getMember_id());
		address.setAddressName(this.addressmark);
		address.setName(this.name);
		address.setTel(this.tel);
		address.setProvince(this.province);
		address.setCity(this.city);
		address.setArea(this.area);
		address.setAddress(this.address);
		if(this.addressid==0){
			this.addressManager.add(address);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";
		}else{
			address.setId(this.addressid);
			this.addressManager.update(address);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";
		}
	}
	public String delete(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return"json_message";
		}
		if(this.addressid==0){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}else{
			this.addressManager.delete(this.addressid,member.getMember_id());
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";
		}
	}
	
	public IAddressManager getAddressManager() {
		return addressManager;
	}

	public void setAddressManager(IAddressManager addressManager) {
		this.addressManager = addressManager;
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
	
	
	
}
