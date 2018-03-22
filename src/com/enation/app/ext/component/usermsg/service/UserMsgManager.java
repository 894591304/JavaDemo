package com.enation.app.ext.component.usermsg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.usermsg.UserMsg;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class UserMsgManager extends BaseSupport<UserMsg> implements IUserMsgManager
{
	@Transactional(propagation=Propagation.REQUIRED)
	public void add(UserMsg userMsg) 
	{
		int check = 0;
		userMsg.setMsgTime(String.valueOf(System.currentTimeMillis()/1000));
		userMsg.setMsgCheck(check);
		this.baseDaoSupport.insert("usermsg", userMsg);
	}


	@Override
	public List getByMemberId(int memberId) {
		List msgList=this.baseDaoSupport.queryForList("select * from usermsg where memberId = ?",new Object[]{memberId});
		if(msgList.size() == 0){
			return null;
		}
		return msgList;
	}
}
