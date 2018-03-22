package com.enation.app.ext.component.collect.service.impl;

import java.util.List;

import com.enation.app.ext.component.collect.model.Collect;
import com.enation.app.ext.component.collect.service.ICollectManager;
import com.enation.eop.sdk.database.BaseSupport;

public class CollectManager extends BaseSupport<Collect> implements ICollectManager{

	public int add(int memberid,int proxyid) {
		if(checkCollect(memberid,proxyid)==0){
			Collect collect = new Collect();
			collect.setMemberId(memberid);
			collect.setProxyId(proxyid);
			this.baseDaoSupport.insert("collect",collect);
			return 1;
		}else{
			return 0;
		}
		
	}

	public int checkCollect(int memberid, int proxyid) {
		Collect collect = getCollect(memberid, proxyid);
		if(collect!=null){
			return 1;
		}else{
			return 0;
		}
	}

	public int cancel(int memberid, int proxyid) {
		if(checkCollect(memberid,proxyid)!=0){
			delete(memberid, proxyid);
			return 1;
		}else{
			return 0;
		}
	}

	
	public Collect getCollect(int memberid, int proxyid) {
		String sql = "select * from collect where memberId = ? and proxyId = ?";
		Collect collect = this.baseDaoSupport.queryForObject(sql,Collect.class,new Object[]{memberid,proxyid});
		if(collect!=null){
			return collect;
		}else{
			return null;
		}
	}


	public void delete(int memberid, int proxyid) {
		String sql = "delete from collect where memberId=? and proxyId=?";
		this.baseDaoSupport.execute(sql, new Object[]{memberid,proxyid});
	}

	public int getAllCollect(int proxyid) {
		String sql = "select count(*) from collect where proxyId=?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{proxyid});
		return count;
	}

	public int collect(int memberid, int proxyid) {
		int result = checkCollect(memberid, proxyid);
		if(result==1){
			delete(memberid, proxyid);
			return 1;
		}else{
			add(memberid, proxyid);
			return 0;
		}
	}

	public List<Collect> getAllByMemberid(int memberid) {
		String sql = "select * from collect where memberid = ?";
		List<Collect> clist = this.baseDaoSupport.queryForList(sql,Collect.class,new Object[]{memberid});
		return clist;
	}

	
	public void deleteAllByMemberid(int memberid) {
		String sql = "delete from collect where memberid = ?";
		this.baseDaoSupport.execute(sql, new Object[]{memberid});
	}
}
