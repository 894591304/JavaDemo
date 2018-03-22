package com.enation.app.b2b2c.component.brokerage.action;

import java.util.HashMap;
import java.util.List;

import com.enation.app.b2b2c.component.brokerage.model.BrokerageUser;
import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.action.WWAction;

public class BrokerageAction extends WWAction {

	private static final long serialVersionUID = -8618054430286090133L;

	private IAdminUserManager adminUserManager;
	private IBrokerageManager brokerageManager;
	private BrokerageUser brokerageUser;
	private List brokerageList;
	private HashMap brokerageMap;
	private String keyword;
	private int user_id;
	private Integer[] userid;
	private String memberids;
	private String username;
	private String password;
	private String name;
	private String address;
	private String mobile;
	private String email;

	public String brokerageList() {
		this.brokerageList = this.brokerageManager.getBrokerageList();
		return "brokerageList";
	}

	public String add_brokerage() {
		return "add_brokerage";
	}

	public String edit_brokerage() {
		this.brokerageUser = this.brokerageManager.get(this.user_id);
		return "edit_brokerage";
	}

	public String detail() {
		this.brokerageUser = this.brokerageManager.get(this.user_id);
		return "brokerage_detail";
	}
	
	public String set_brokerage() {
		this.brokerageList = this.brokerageManager.getBrokerageList();
		return "set_brokerage";
	}
	
	public String brokeragelistJson() {
		this.brokerageMap = new HashMap();
		this.brokerageMap.put("keyword", this.keyword);
		this.webpage = this.brokerageManager.searchBrokerage(this.brokerageMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}

	public String saveBrokerage() {
		int result = this.brokerageManager.checkname(this.brokerageUser.getUsername());
		if (result == 1) {
			showErrorJson("经纪公司用户名已存在");
			return "json_message";
		}
		if (this.brokerageUser != null) {
			AdminUser adminUser = new AdminUser();
			adminUser.setUsername(this.brokerageUser.getUsername());
			adminUser.setPassword(this.brokerageUser.getPassword());
			adminUser.setRealname("经纪公司-" + this.brokerageUser.getName());
			adminUser.setRoleids(new int[] { 7 });
			adminUser.setState(1);
			Integer _userid = this.adminUserManager.add(adminUser);
			this.brokerageUser.setUserid(_userid);
			this.brokerageManager.setBrokerageUser(_userid);
			this.brokerageManager.add(this.brokerageUser);
			showSuccessJson("保存经纪公司成功", _userid);
		}
		return "json_message";
	}

	public String saveEditBrokerage() {
		BrokerageUser oldBrokerageUser = this.brokerageManager.get(this.user_id);
		oldBrokerageUser.setName(this.name);
		oldBrokerageUser.setAddress(this.address);
		oldBrokerageUser.setEmail(this.email);
		oldBrokerageUser.setMobile(this.mobile);
		this.brokerageManager.edit(oldBrokerageUser);
		showSuccessJson("修改经纪公司成功");
		return "json_message";
	}

	public String delete() {
		try {
			if (this.userid != null && !this.userid.equals("")) {
				for (int i = 0; i < this.userid.length; i++) {
					this.adminUserManager.delete(this.userid[i]);
					this.brokerageManager.delete(this.userid[i]);
				}
			}
			showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			showErrorJson("删除失败" + e.getMessage());
		}
		return "json_message";
	}

	public String setBrokerage(){
		this.brokerageManager.setBrokerage(this.memberids,this.user_id);
		showSuccessJson("设置经纪公司成功");
		return "json_message";
	}
	
	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
	}

	public List getBrokerageList() {
		return brokerageList;
	}

	public void setBrokerageList(List brokerageList) {
		this.brokerageList = brokerageList;
	}

	public BrokerageUser getBrokerageUser() {
		return brokerageUser;
	}

	public void setBrokerageUser(BrokerageUser brokerageUser) {
		this.brokerageUser = brokerageUser;
	}

	public HashMap getBrokerageMap() {
		return brokerageMap;
	}

	public void setBrokerageMap(HashMap brokerageMap) {
		this.brokerageMap = brokerageMap;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setUserid(Integer[] userid) {
		this.userid = userid;
	}

	public Integer[] getUserid() {
		return userid;
	}

	public String getMemberids() {
		return memberids;
	}

	public void setMemberids(String memberids) {
		this.memberids = memberids;
	}


}
