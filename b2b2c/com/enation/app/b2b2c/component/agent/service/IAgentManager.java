package com.enation.app.b2b2c.component.agent.service;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.agent.model.AgentUser;
import com.enation.framework.database.Page;


public interface IAgentManager {
	
	/**
	 * 设置所有管理员用户为非商品企业用户
	 */
	public abstract void setNotAgentUser();
	
	/**
	 * 设置用户为商品企业用户
	 */
	public void setAgentUser(int userid);
	
	/**
	 * 检查用户是否是商品企业用户
	 * @param userid
	 * @return
	 */
	public int checkAgentUser(int userid);
	
	/**
	 * 获取所有的商品企业用户
	 * @return
	 */
	public List<AgentUser> getAgentList();
	
	/**
	 * 删除所有商品企业用户
	 */
	public void delteAllAgent();
	
	/**
	 * 获取商品企业用户
	 * @param userid
	 * @return
	 */
	public AgentUser get(int userid);
	
	/**
	 * 查询商品企业
	 * @param memberMap
	 * @param page
	 * @param pageSize
	 * @param other
	 * @param order
	 * @return
	 */
	public Page searchAgent(Map agentMap, Integer page, Integer pageSize, String other, String order);
	
	/**
	 * 新增商品企业用户
	 * @param agent
	 * @return
	 */
	public int add(AgentUser agent);
	
	/**
	 * 修改商品企业用户
	 * @param agent
	 * @return
	 */
	public AgentUser edit(AgentUser agent);
	
	/**
	 * 删除商品企业用户
	 * @param userid
	 * @return
	 */
	public void delete(Integer userid);
	
	/**
	 * 检查用户名
	 * @param name
	 * @return
	 */
	public int checkname(String username);
}
