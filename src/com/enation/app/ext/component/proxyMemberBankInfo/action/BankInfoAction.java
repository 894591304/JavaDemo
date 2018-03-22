package com.enation.app.ext.component.proxyMemberBankInfo.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.bankselect.model.BankSelect;
import com.enation.app.ext.component.bankselect.service.IBankSelectManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;
import com.enation.app.ext.component.proxyMemberBankInfo.service.IProxyMemberBankInfoManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;


public class BankInfoAction extends WWAction
{

	private static final long serialVersionUID = -7301623565108970504L;

	private IProxyMemberBankInfoManager proxyMemberBankInfoManager;
	private IBankSelectManager bankSelectManager;
	private IUserAccountManager userAccountManager;
	
	private int infoid;
	private String payeebankcard;
	private int payeebankaccounttype;
	private String payeebanknum;
	private String payeebankprovincename;
	private String payeebankcityname;
	private String payeecardholder;
	private String payeebankbranchname;
	private int defaultc;
	
	public String saveBankInfo(){
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
		if(infoid==0){
			ProxyMemberBankInfo proxyMemberBankInfo = new ProxyMemberBankInfo();
			proxyMemberBankInfo.setPayeeBankCard(payeebankcard);
			proxyMemberBankInfo.setPayeeBankAccountType(payeebankaccounttype);
			proxyMemberBankInfo.setPayeeBankNum(payeebanknum);
			proxyMemberBankInfo.setPayeeBankProvinceName(payeebankprovincename);
			proxyMemberBankInfo.setPayeeBankCityName(payeebankcityname);
			proxyMemberBankInfo.setPayeeCardHolder(payeecardholder);
			proxyMemberBankInfo.setPayeeBankBranchName(payeebankbranchname);
			proxyMemberBankInfo.setProxyMemberId(member.getMember_id());
			this.proxyMemberBankInfoManager.add(proxyMemberBankInfo);
			ProxyMemberBankInfo pMBInfo = this.proxyMemberBankInfoManager.getByCard(proxyMemberBankInfo.getPayeeBankCard());
			BankSelect bankSelect = new BankSelect();
			bankSelect.setBankInfoId(pMBInfo.getId());
			bankSelect.setProxyMemberId(pMBInfo.getProxyMemberId());
			List<ProxyMemberBankInfo> pList = this.proxyMemberBankInfoManager.getByMemberId(member.getMember_id());
			if(defaultc==1){
				this.bankSelectManager.setAllStatusTo0(member.getMember_id());
				bankSelect.setStatus(1);
			}
			this.bankSelectManager.add(bankSelect);
			this.json=JsonMessageUtil.getStringJson("result", "1");
		}else{
			ProxyMemberBankInfo proxyMemberBankInfo = this.proxyMemberBankInfoManager.get(infoid);
			if(proxyMemberBankInfo.getProxyMemberId()!=member.getMember_id()){
				this.json=JsonMessageUtil.getStringJson("result", "4");
				return "json_message";
			}
			proxyMemberBankInfo.setPayeeBankCard(payeebankcard);
			proxyMemberBankInfo.setPayeeBankAccountType(payeebankaccounttype);
			proxyMemberBankInfo.setPayeeBankNum(payeebanknum);
			proxyMemberBankInfo.setPayeeBankProvinceName(payeebankprovincename);
			proxyMemberBankInfo.setPayeeBankCityName(payeebankcityname);
			proxyMemberBankInfo.setPayeeCardHolder(payeecardholder);
			proxyMemberBankInfo.setPayeeBankBranchName(payeebankbranchname);
			this.proxyMemberBankInfoManager.update(proxyMemberBankInfo);
			if(defaultc==1){
				this.bankSelectManager.setAllStatusTo0(member.getMember_id());
				BankSelect bankSelect = this.bankSelectManager.getByInfoId(infoid);
				bankSelect.setStatus(1);
				this.bankSelectManager.update(bankSelect);
			}
			this.json=JsonMessageUtil.getStringJson("result", "5");
		}
		return "json_message";
	}

	public IProxyMemberBankInfoManager getProxyMemberBankInfoManager() {
		return proxyMemberBankInfoManager;
	}

	public void setProxyMemberBankInfoManager(
			IProxyMemberBankInfoManager proxyMemberBankInfoManager) {
		this.proxyMemberBankInfoManager = proxyMemberBankInfoManager;
	}

	public String getPayeebankcard() {
		return payeebankcard;
	}

	public void setPayeebankcard(String payeebankcard) {
		this.payeebankcard = payeebankcard;
	}

	public int getPayeebankaccounttype() {
		return payeebankaccounttype;
	}

	public void setPayeebankaccounttype(int payeebankaccounttype) {
		this.payeebankaccounttype = payeebankaccounttype;
	}

	public String getPayeebankprovincename() {
		return payeebankprovincename;
	}

	public void setPayeebankprovincename(String payeebankprovincename) {
		this.payeebankprovincename = payeebankprovincename;
	}

	public String getPayeebankcityname() {
		return payeebankcityname;
	}

	public void setPayeebankcityname(String payeebankcityname) {
		this.payeebankcityname = payeebankcityname;
	}

	public int getInfoid() {
		return infoid;
	}

	public void setInfoid(int infoid) {
		this.infoid = infoid;
	}

	public String getPayeebankbranchname() {
		return payeebankbranchname;
	}

	public void setPayeebankbranchname(String payeebankbranchname) {
		this.payeebankbranchname = payeebankbranchname;
	}

	public String getPayeecardholder() {
		return payeecardholder;
	}

	public void setPayeecardholder(String payeecardholder) {
		this.payeecardholder = payeecardholder;
	}

	public IBankSelectManager getBankSelectManager() {
		return bankSelectManager;
	}

	public void setBankSelectManager(IBankSelectManager bankSelectManager) {
		this.bankSelectManager = bankSelectManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public int getDefaultc() {
		return defaultc;
	}

	public void setDefaultc(int defaultc) {
		this.defaultc = defaultc;
	}

	public String getPayeebanknum() {
		return payeebanknum;
	}

	public void setPayeebanknum(String payeebanknum) {
		this.payeebanknum = payeebanknum;
	}
}
