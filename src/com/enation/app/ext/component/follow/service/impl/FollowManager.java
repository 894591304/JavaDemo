package com.enation.app.ext.component.follow.service.impl;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.Count;
import org.apache.struts2.components.Select;
import org.jsoup.select.Evaluator.Id;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.follow.model.Follow;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.eop.sdk.database.BaseSupport;

public class FollowManager extends BaseSupport<Follow> implements IFollowManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public int follow(int memberId, int followId) {
		int result = 0;
		String sql = "select count(*) from follow where memberId=? and followId=?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberId,followId});
		if(count == 0)
		{
			Follow follow = new Follow();
			follow.setMemberId(memberId);
			follow.setFollowId(followId);
			follow.setMsg(0);
			this.baseDaoSupport.insert("follow", follow);
			result = 1;
		}
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int cancelFollow(int memberId, int followId) {
		int result = 0;
		String sql = "select count(*) from follow where memberId=? and followId=?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberId,followId});
		if(count == 1)
		{
			String sql1 = "delete from follow where memberId=? and followId=?";
			this.baseDaoSupport.execute(sql1, new Object[]{memberId,followId});
			result = 1;
		}
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Follow> getFollowListByMemberId(int memberId) {
		String sql = "select * from follow where memberId = ?";
		List<Follow> followList = this.baseDaoSupport.queryForList(sql,Follow.class,new Object[]{memberId});
		return followList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int followMsgPlus(int followId){
		int result = 0;
		String sql1 = "select * from follow where followId = ?";
		List<Follow> followList = this.baseDaoSupport.queryForList(sql1,Follow.class,new Object[]{followId});
		if(followList.size() == 0)
		{
			result = 0;
		}else{			
			for(int i=0;i<followList.size();i++)
			{
				Follow f =null;
				f = followList.get(i);
				int id = f.getId();
				int msg = f.getMsg()+1;
				String sql2 = "update follow set msg = ? where id = ?";
				this.baseDaoSupport.execute(sql2, new Object[]{msg,id});
			}
			result = 1;
		}
		return result;
	}
	
	public int getFansNum(int followid){
		String sql = "select count(*) from follow where followId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, followid);
		return count;
	}
	
	public int checkFollow(int memberid,int followid){
		String sql = "select count(*) from follow where memberId = ? and followId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{memberid,followid});
		if(count!=0){return 1;}
		else{return 0;}
	}

	@Override
	public int getFollowNum(int memberid) {
		String sql = "select count(*) from follow where memberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberid});
		return count;
	}

	@Override
	public void msgUp(int followid) {
		String sql = "select * from follow where followId = ?";
		List<Follow> flist = this.baseDaoSupport.queryForList(sql, Follow.class, new Object[]{followid});
		if(flist.size()!=0){
			for(int i=0;i<flist.size();i++){
				flist.get(i).setMsg(flist.get(i).getMsg()+1);
				this.baseDaoSupport.update("follow", flist.get(i), "id="+flist.get(i).getId());
			}
		}
	}
	
	public void msgClean(int memberid,int followid){
		String sql = "select * from follow where memberId = ? and followId = ?";
		Follow follow = this.baseDaoSupport.queryForObject(sql, Follow.class, new Object[]{memberid,followid});
		if(follow!=null){
			follow.setMsg(0);
			this.baseDaoSupport.update("follow", follow, "id="+follow.getId());
		}
	}
}
