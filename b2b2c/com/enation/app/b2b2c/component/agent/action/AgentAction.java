package com.enation.app.b2b2c.component.agent.action;

import java.util.HashMap;
import java.util.List;

import com.enation.app.b2b2c.component.agent.model.AgentUser;
import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.action.WWAction;

public class AgentAction extends WWAction {

	private static final long serialVersionUID = -8618054430286090133L;

	private IAdminUserManager adminUserManager;
	private IAgentManager agentManager;
	private AgentUser agentUser;
	private List agentList;
	private HashMap agentMap;
	private String keyword;
	private int user_id;
	private Integer[] userid;
	private String username;
	private String password;
	private String name;
	private String address;
	private String mobile;
	private String email;

	public String agentList() {
		this.agentList = this.agentManager.getAgentList();
		return "agentList";
	}

	public String add_agent() {
		return "add_agent";
	}

	public String edit_agent() {
		this.agentUser = this.agentManager.get(this.user_id);
		return "edit_agent";
	}

	public String detail() {
		this.agentUser = this.agentManager.get(this.user_id);
		return "agent_detail";
	}

	public String agentlistJson() {
		this.agentMap = new HashMap();
		this.agentMap.put("keyword", this.keyword);
		this.webpage = this.agentManager.searchAgent(this.agentMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}

	public String saveAgent() {
		int result = this.agentManager.checkname(this.agentUser.getUsername());
		if (result == 1) {
			showErrorJson("企业用户名已存在");
			return "json_message";
		}
		if (this.agentUser != null) {
			AdminUser adminUser = new AdminUser();
			adminUser.setUsername(this.agentUser.getUsername());
			adminUser.setPassword(this.agentUser.getPassword());
			adminUser.setRealname("商品企业-" + this.agentUser.getName());
			adminUser.setRoleids(new int[] { 3, 6 });
			adminUser.setState(1);
			Integer _userid = this.adminUserManager.add(adminUser);
			this.agentUser.setUserid(_userid);
			this.agentManager.setAgentUser(_userid);
			this.agentManager.add(this.agentUser);
			showSuccessJson("保存商品企业成功", _userid);
		}
		return "json_message";
	}

	public String saveEditAgent() {
		AgentUser oldAgentUser = this.agentManager.get(this.user_id);
		oldAgentUser.setName(this.name);
		oldAgentUser.setAddress(this.address);
		oldAgentUser.setEmail(this.email);
		oldAgentUser.setMobile(this.mobile);
		this.agentManager.edit(oldAgentUser);
		showSuccessJson("修改商品企业成功");
		return "json_message";
	}

	public String delete() {
		try {
			if (this.userid != null && !this.userid.equals("")) {
				for (int i = 0; i < this.userid.length; i++) {
					this.adminUserManager.delete(this.userid[i]);
					this.agentManager.delete(this.userid[i]);
				}
			}
			showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			showErrorJson("删除失败" + e.getMessage());
		}
		return "json_message";
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public List getAgentList() {
		return agentList;
	}

	public void setAgentList(List agentList) {
		this.agentList = agentList;
	}

	public AgentUser getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(AgentUser agentUser) {
		this.agentUser = agentUser;
	}

	public HashMap getAgentMap() {
		return agentMap;
	}

	public void setAgentMap(HashMap agentMap) {
		this.agentMap = agentMap;
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

}
