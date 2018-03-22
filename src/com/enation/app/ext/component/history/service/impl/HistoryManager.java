package com.enation.app.ext.component.history.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.history.model.History;
import com.enation.app.ext.component.history.service.IHistoryManager;
import com.enation.eop.sdk.database.BaseSupport;


@Component
public class HistoryManager extends BaseSupport<History> implements IHistoryManager{


	public List<History> getAllByMemberid(int memberid) {
		String sql = "select * from history where memberId = ? group by id desc";
		List<History> hList = this.baseDaoSupport.queryForList(sql, History.class, new Object[]{memberid});
		return hList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(History history) {
		history.setLookTime(String.valueOf(System.currentTimeMillis()/1000));
		String sql = "select count(*) from history where memberId = ? and proxyId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{history.getMemberId(),history.getProxyId()});
		if(count==0){
			this.baseDaoSupport.insert("history", history);
		}else{
			History h1= getByMemberidAndProxyId(history.getMemberId(), history.getProxyId());
			h1.setLookNum(h1.getLookNum()+1);
			edit(h1);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteByMemberid(int memberid) {
		String sql = "delete from history where memberId = ?";
		this.baseDaoSupport.execute(sql, new Object[]{memberid});
	}
	
	public History getByMemberidAndProxyId(int memberid,int proxyid){
		String sql = "select * from history where memberId=? and proxyId=?";
		History history = this.baseDaoSupport.queryForObject(sql, History.class, new Object[]{memberid,proxyid});
		if(history==null){
			return null;
		}else{
			return history;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(History history){
		this.baseDaoSupport.update("history", history, "id="+history.getId());
	}
}
