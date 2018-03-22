package com.enation.app.b2b2c.component.creditmember.service;

import java.util.Map;
import com.enation.framework.database.Page;


public interface ICreditMemberManager {
	

	/**
	 * 查询网红列表
	 * @param memberMap
	 * @param page
	 * @param pageSize
	 * @param other
	 * @param order
	 * @return
	 */
	public Page searchCreditMember(Map memberMap, Integer page, Integer pageSize, String other, String order);
	
	/**
	 * 审核用户
	 * @param memberid
	 * @param review
	 */
	public void creditMember(int memberid,int review);
	
	/**
	 * 统计网红代理销量
	 * @param memberMap
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page searchCreditProxy(Map memberMap, Integer page, Integer pageSize);
	
}
