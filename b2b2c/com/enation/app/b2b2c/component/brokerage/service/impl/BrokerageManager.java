package com.enation.app.b2b2c.component.brokerage.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.brokerage.model.BrokerageUser;
import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

public class BrokerageManager extends BaseSupport<BrokerageUser> implements IBrokerageManager{
	
	@Override
	public List<BrokerageUser> getBrokerageList() {
		String sql = "select * from es_brokerageuser";
		return this.daoSupport.queryForList(sql, BrokerageUser.class,new Object[0]);
	}

	@Override
	public void delteAllBrokerage() {
		this.baseDaoSupport.execute("delete from es_adminuser where isagent='2'", new Object[0]);
	}

	@Override
	public Page searchBrokerage(Map brokerageMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSql(brokerageMap);
		sql = sql + " order by " + other + " " + order;
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(), new Object[0]);
		return webpage;
	}

	
	
	private String createTemlSql(Map brokerageMap){
		String sql = "select * from es_brokerageuser where 1=1";
		String keyword = (String)brokerageMap.get("keyword");
		if(keyword!=null){
			sql = sql + " and (username like '%" + keyword + "%'";
			sql = sql + " or name like '%" + keyword + "%')";
		}
		return sql;
	}

	@Override
	public int add(BrokerageUser brokerage) {
		if(brokerage == null){
			throw new IllegalArgumentException("brokerage is null");
		}
		if(brokerage.getName()==null){
			throw new IllegalArgumentException("brokerage's name is null");
		}
		
		this.baseDaoSupport.insert("brokerageuser", brokerage);
		return 1;
	}

	@Override
	public BrokerageUser edit(BrokerageUser brokerage) {
		this.baseDaoSupport.update("brokerageuser", brokerage, "userid=" + brokerage.getUserid());
		return null;
	}

	@Override
	public void delete(Integer userid) {
		  if (userid == null)
		    return;
		  String sql = "delete from brokerageuser where userid = ?";
		  this.baseDaoSupport.execute(sql, new Object[]{userid});
		}

	@Override
	public BrokerageUser get(int userid) {
		String sql = "select * from brokerageuser where userid=?";
		BrokerageUser order = (BrokerageUser)this.baseDaoSupport.queryForObject(sql, BrokerageUser.class, new Object[] { userid });
		return order;
	}
	
	@Override
	public int checkname(String name) {
		  String sql = "select count(0) from brokerageuser where username=?";
		  int count = this.baseDaoSupport.queryForInt(sql, new Object[] { name });
		  count = count > 0 ? 1 : 0;
		  return count;
		}

	@Override
	public void setBrokerageUser(int userid) {
		this.baseDaoSupport.execute("update es_adminuser set isagent='2' where userid = ?", new Object[]{userid});
	}

	@Override
	public int checkBrokerageUser(int userid) {
		String sql = "select count(0) from adminuser where userid=? and isagent='2'";
		  int count = this.baseDaoSupport.queryForInt(sql, new Object[] { userid });
		  count = count > 0 ? 1 : 0;
		  return count;
	}

	@Override
	public void setBrokerage(String memberids, int user_id) {
		String sql = "update es_credit set brokerageId='"+user_id+"' where memberId in (" + memberids + ")";
		this.baseDaoSupport.execute(sql, new Object[0]);
	}
}
