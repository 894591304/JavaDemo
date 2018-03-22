package com.enation.app.b2b2c.component.agent.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.agent.model.AgentUser;
import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

public class AgentManager extends BaseSupport<AgentUser> implements IAgentManager{

	@Override
	public void setNotAgentUser() {
		this.baseDaoSupport.execute("update es_adminuser set isagent='0' where isagent is null", new Object[0]);
	}
	
	@Override
	public List<AgentUser> getAgentList() {
		String sql = "select * from es_agentuser";
		return this.daoSupport.queryForList(sql, AgentUser.class,new Object[0]);
	}

	@Override
	public void delteAllAgent() {
		this.baseDaoSupport.execute("delete from es_adminuser where isagent='1'", new Object[0]);
	}

	@Override
	public Page searchAgent(Map agentMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSql(agentMap);
		sql = sql + " order by " + other + " " + order;
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(), new Object[0]);
		return webpage;
	}

	
	
	private String createTemlSql(Map agentMap){
		String sql = "select * from es_agentuser where 1=1";
		String keyword = (String)agentMap.get("keyword");
		if(keyword!=null){
			sql = sql + " and (username like '%" + keyword + "%'";
			sql = sql + " or name like '%" + keyword + "%')";
		}
		return sql;
	}

	@Override
	public int add(AgentUser agent) {
		if(agent == null){
			throw new IllegalArgumentException("agent is null");
		}
		if(agent.getName()==null){
			throw new IllegalArgumentException("agent's name is null");
		}
		
		this.baseDaoSupport.insert("agentuser", agent);
		return 1;
	}

	@Override
	public AgentUser edit(AgentUser agent) {
		this.baseDaoSupport.update("agentuser", agent, "userid=" + agent.getUserid());
		return null;
	}

	@Override
	public void delete(Integer userid) {
		  if (userid == null)
		    return;
		  String sql = "delete from agentuser where userid = ?";
		  this.baseDaoSupport.execute(sql, new Object[]{userid});
		}

	@Override
	public AgentUser get(int userid) {
		String sql = "select * from agentuser where userid=?";
		AgentUser order = (AgentUser)this.baseDaoSupport.queryForObject(sql, AgentUser.class, new Object[] { userid });
		return order;
	}
	
	@Override
	public int checkname(String name) {
		  String sql = "select count(0) from agentuser where username=?";
		  int count = this.baseDaoSupport.queryForInt(sql, new Object[] { name });
		  count = count > 0 ? 1 : 0;
		  return count;
		}

	@Override
	public void setAgentUser(int userid) {
		this.baseDaoSupport.execute("update es_adminuser set isagent='1' where userid = ?", new Object[]{userid});
	}

	@Override
	public int checkAgentUser(int userid) {
		String sql = "select count(0) from adminuser where userid=? and isagent='1'";
		  int count = this.baseDaoSupport.queryForInt(sql, new Object[] { userid });
		  count = count > 0 ? 1 : 0;
		  return count;
	}
}
