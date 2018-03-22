package com.enation.app.ext.component.follow.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.follow.service.IFollowManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class FollowAction extends WWAction{

	private static final long serialVersionUID = 4531307993023272018L;
	
	private IFollowManager followManager;
	private int memberId;
	private int followId;
	
	public String follow()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return"json_message";
		}
		if(member.getMember_id()==followId){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return"json_message";
		}
		int result = this.followManager.follow(member.getMember_id(), this.followId);
		if(result == 0){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			showErrorJson("您已关注该店铺！");
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "1");
			showSuccessJson("关注成功！");
		}		
		return "json_message";
	}
	
	public String cancelFollow()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return "json_message";
		}
		if(member.getMember_id()==followId){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return"json_message";
		}
		int result = this.followManager.cancelFollow(member.getMember_id(), this.followId);
		if(result == 0){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			showErrorJson("您未关注该店铺！");
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "1");
			showSuccessJson("取消关注成功！");
		}		
		return "json_message";
	}
	
	public String msgClean()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		this.followManager.msgClean(member.getMember_id(), this.followId);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public IFollowManager getFollowManager()
	{
		return this.followManager;
	}

	public void setFollowManager(IFollowManager followManager)
	{
		this.followManager = followManager;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getFollowId() {
		return followId;
	}

	public void setFollowId(int followId) {
		this.followId = followId;
	}
	
	
}
