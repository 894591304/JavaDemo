package com.enation.app.ext.component.vipleveldetail.action;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;
import com.enation.app.ext.component.vipleveldetail.service.IVipLevelDetailManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

public class VipInfoAction extends WWAction{

	private static final long serialVersionUID = 5703780176308442120L;
	
	private IVipLevelDetailManager vipLevelDetailManager;
	private IUserAccountManager userAccountManager;
	
	private String vip1name;
	private String vip1info;
	private String vip2name;
	private String vip2info;
	private String vip3name;
	private String vip3info;

	public String saveVipInfo(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			this.json=JsonMessageUtil.getStringJson("result", "2");
			return "json_message";
		}
		int level = this.userAccountManager.getLevel(member.getMember_id());
		if(level==0){
			this.json=JsonMessageUtil.getStringJson("result", "3");
			return "json_message";
		}
		VipLevelDetail vipLevelDetail = this.vipLevelDetailManager.getByMemberId(member.getMember_id());
		if(vipLevelDetail==null){
			vipLevelDetail = new VipLevelDetail();
			vipLevelDetail.setProxyMemberId(member.getMember_id());
			vipLevelDetail.setV1Name(vip1name);
			vipLevelDetail.setV1Content(vip1info);
			vipLevelDetail.setV2Name(vip2name);
			vipLevelDetail.setV2Content(vip2info);
			vipLevelDetail.setV3Name(vip3name);
			vipLevelDetail.setV3Content(vip3info);
			this.vipLevelDetailManager.add(vipLevelDetail);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";
		}else{
			vipLevelDetail.setV1Name(vip1name);
			vipLevelDetail.setV1Content(vip1info);
			vipLevelDetail.setV2Name(vip2name);
			vipLevelDetail.setV2Content(vip2info);
			vipLevelDetail.setV3Name(vip3name);
			vipLevelDetail.setV3Content(vip3info);
			this.vipLevelDetailManager.update(vipLevelDetail);
			this.json=JsonMessageUtil.getStringJson("result", "1");
			return "json_message";
		}
	}

	public IVipLevelDetailManager getVipLevelDetailManager() {
		return vipLevelDetailManager;
	}

	public void setVipLevelDetailManager(
			IVipLevelDetailManager vipLevelDetailManager) {
		this.vipLevelDetailManager = vipLevelDetailManager;
	}

	public String getVip1name() {
		return vip1name;
	}

	public void setVip1name(String vip1name) {
		this.vip1name = vip1name;
	}

	public String getVip1info() {
		return vip1info;
	}

	public void setVip1info(String vip1info) {
		this.vip1info = vip1info;
	}

	public String getVip2name() {
		return vip2name;
	}

	public void setVip2name(String vip2name) {
		this.vip2name = vip2name;
	}

	public String getVip2info() {
		return vip2info;
	}

	public void setVip2info(String vip2info) {
		this.vip2info = vip2info;
	}

	public String getVip3name() {
		return vip3name;
	}

	public void setVip3name(String vip3name) {
		this.vip3name = vip3name;
	}

	public String getVip3info() {
		return vip3info;
	}

	public void setVip3info(String vip3info) {
		this.vip3info = vip3info;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}
}
