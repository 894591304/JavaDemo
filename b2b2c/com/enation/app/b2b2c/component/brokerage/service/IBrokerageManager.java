package com.enation.app.b2b2c.component.brokerage.service;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.brokerage.model.BrokerageUser;
import com.enation.framework.database.Page;


public interface IBrokerageManager {
	
	/**
	 * 设置用户为经纪公司用户
	 */
	public void setBrokerageUser(int userid);
	
	/**
	 * 检查用户是否是经纪公司用户
	 * @param userid
	 * @return
	 */
	public int checkBrokerageUser(int userid);
	
	/**
	 * 获取所有的经纪公司用户
	 * @return
	 */
	public List<BrokerageUser> getBrokerageList();
	
	/**
	 * 删除所有经纪公司用户
	 */
	public void delteAllBrokerage();
	
	/**
	 * 获取经纪公司用户
	 * @param userid
	 * @return
	 */
	public BrokerageUser get(int userid);
	
	/**
	 * 查询经纪公司
	 * @param memberMap
	 * @param page
	 * @param pageSize
	 * @param other
	 * @param order
	 * @return
	 */
	public Page searchBrokerage(Map brokerageMap, Integer page, Integer pageSize, String other, String order);
	
	/**
	 * 新增经纪公司用户
	 * @param brokerage
	 * @return
	 */
	public int add(BrokerageUser brokerage);
	
	/**
	 * 修改经纪公司用户
	 * @param brokerage
	 * @return
	 */
	public BrokerageUser edit(BrokerageUser brokerage);
	
	/**
	 * 删除经纪公司用户
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

	/**
	 *设置经纪公司
	 */
	public void setBrokerage(String memberids, int user_id);
}
