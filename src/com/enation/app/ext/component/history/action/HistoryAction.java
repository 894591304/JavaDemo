package com.enation.app.ext.component.history.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.history.model.History;
import com.enation.app.ext.component.history.service.IHistoryManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class HistoryAction extends WWAction{

	private static final long serialVersionUID = 4884330376801875490L;

	private IHistoryManager historyManager;
	
	private int proxyid=-1;
	private int goodsid=-1;
	
	public String look(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		int mid = member.getMember_id();
		int pid = this.proxyid;
		int gid = this.goodsid;
		if(pid==-1||gid==-1){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		History history = new History();
		history.setMemberId(mid);
		history.setProxyId(pid);
		history.setGoodsId(gid);
		history.setLookNum(1);
		this.historyManager.add(history);	
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public String clean(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member==null){
			this.json=JsonMessageUtil.getStringJson("result", "0");
			return "json_message";
		}
		int mid = member.getMember_id();
		this.historyManager.deleteByMemberid(mid);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}
	
	public IHistoryManager getHistoryManager() {
		return historyManager;
	}
	public void setHistoryManager(IHistoryManager historyManager) {
		this.historyManager = historyManager;
	}
	public int getProxyid() {
		return proxyid;
	}
	public void setProxyid(int proxyid) {
		this.proxyid = proxyid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

}
