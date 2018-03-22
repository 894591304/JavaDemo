package com.enation.app.ext.component.mobile.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.mobile.model.Mobile;
import com.enation.app.ext.component.mobile.service.IMobileManager;
import com.enation.eop.sdk.database.BaseSupport;

public class MobileManager extends BaseSupport<Mobile> implements IMobileManager 
{
	private IMobileManager mobileManager;

	@Transactional(propagation=Propagation.REQUIRED)
	public int newNum(String mobile) {
		System.out.println("创建"+mobile+"的验证码。");
		Mobile mb = new Mobile();
		int result = 0;
		String sql = "select count(0) from mobile where mobile = "+mobile;
		int count = this.baseDaoSupport.queryForInt(sql, new Object[0]);
		int ranNum = (int)((Math.random()*9+1)*100000);
		String checkNum = String.valueOf(ranNum);
		try {
			System.out.println("六位验证码为："+checkNum);
			result = 1;
		} catch (Exception e) {
			result = 0;
			return result;
		}		
		Long lTime =(System.currentTimeMillis()+3*60*1000);/*短信验证码3分钟内有效*/ 
		String outTime = String.valueOf(lTime);	
		mb.setMobile(mobile);
		mb.setCheckNum(checkNum);
		mb.setOutTime(outTime);
		count = count > 0 ? 1 : 0;
		if(count == 1)
		{updateNum(mb);}
		else
		{this.baseDaoSupport.insert("mobile", mb);}
		autoDelete();
		return result;
	}
	
	public void updateNum(Mobile mobile) {
		this.baseDaoSupport.update("mobile",mobile ,"mobile="+mobile.getMobile());
	}
	
	public int checkNum(String mobile, String checkNum) {
		String sql = "select count(0) from mobile where mobile = "+mobile+" and checkNum = "+checkNum;
		int count = this.baseDaoSupport.queryForInt(sql, new Object[0]);
		if(count == 1)
		{
			String sql2 = "select * from mobile where mobile = "+mobile+" and checkNum = "+checkNum;
			List<Mobile> list = this.baseDaoSupport.queryForList(sql2, Mobile.class,new Object[0]);
			Mobile m = (Mobile)list.get(0);
			Long nowTime = System.currentTimeMillis();				/*取当前时间*/
			Long outTime = Long.valueOf(m.getOutTime());
			if(nowTime>outTime)
			{
				System.out.println("验证码超时！");
				count=0;
			}
			return count;
		}
		return count;
	}

	public void delete (String mobile)
	{
		if((mobile == null)||(mobile.equals("")))
			return;
		String sql = "delete from mobile where mobile = "+mobile;
		this.baseDaoSupport.execute(sql, new Object[0]);
	}
	
	public void autoDelete(){
		Long nowTime = System.currentTimeMillis();
		String sql = "delete from mobile where outTime < "+nowTime;
		this.baseDaoSupport.execute(sql);
	}
	
	public IMobileManager getMobileManager()
	{
		return this.mobileManager;
	}
}
