package com.enation.app.ext.component.collect.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.collect.service.ICollectManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class CollectAction extends WWAction{

	private static final long serialVersionUID = 7021647312833790590L;
	
	private ICollectManager collectManager;
	private int proxyid;
	
	public String collect()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return"json_message";
		}
		int re = this.collectManager.collect(member.getMember_id(),proxyid);
		if(re == 0){
			this.json=JsonMessageUtil.getStringJson("result", "0");
		}else{
			this.json=JsonMessageUtil.getStringJson("result", "1");
		}	
		return "json_message";
	}
	
	public String clean()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能收藏！");
			return "json_message";
		}
		this.collectManager.deleteAllByMemberid(member.getMember_id());
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public ICollectManager getCollectManager() {
		return collectManager;
	}
	public void setCollectManager(ICollectManager collectManager) {
		this.collectManager = collectManager;
	}
	public int getProxyid() {
		return proxyid;
	}
	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}
	
	

}
