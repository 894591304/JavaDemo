package com.enation.app.ext.component.bankselect.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.bankinfo.model.BankInfo;
import com.enation.app.ext.component.bankinfo.service.IBankInfoManager;
import com.enation.app.ext.component.bankselect.model.BankSelect;
import com.enation.app.ext.component.bankselect.service.IBankSelectManager;
import com.enation.app.ext.component.proxyMemberBankInfo.model.ProxyMemberBankInfo;
import com.enation.app.ext.component.proxyMemberBankInfo.service.IProxyMemberBankInfoManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BankListTag extends BaseFreeMarkerTag{

	private IBankSelectManager bankSelectManager;
	private IProxyMemberBankInfoManager proxyMemberBankInfoManager;
	private IBankInfoManager bankInfoManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[HistoryListTag]");
		}
		Map result = new HashMap();
		List BList = new ArrayList();
		List<BankSelect> bankList = this.bankSelectManager.getByMemberId(member.getMember_id());
		int total = bankList.size();
		for(int i=0;i<total;i++){
			Map bndMap = new HashMap();
			BankSelect bankNotDefault = bankList.get(i);
			ProxyMemberBankInfo pNDefaultBankInfo = this.proxyMemberBankInfoManager.get(bankNotDefault.getBankInfoId());
			BankInfo bankInfo = this.bankInfoManager.getById(Integer.valueOf(pNDefaultBankInfo.getPayeeBankNum()));
			bndMap.put("bankinfoid", bankNotDefault.getBankInfoId());
			bndMap.put("banknum",pNDefaultBankInfo.getPayeeBankNum());
			bndMap.put("bankname",bankInfo.getBankname());
			String num = pNDefaultBankInfo.getPayeeBankCard();
			String num1 = num.substring(num.length()-4,num.length());
			bndMap.put("num",num1);
			String s = "";
			if(bankNotDefault.getStatus()==1){
				s = "Show";
			}else{
				s = "Hide";
			}
			bndMap.put("status", s);
			BList.add(bndMap);
		}
		result.put("blist",BList);
		return result;
	}

	public IBankSelectManager getBankSelectManager() {
		return bankSelectManager;
	}

	public void setBankSelectManager(IBankSelectManager bankSelectManager) {
		this.bankSelectManager = bankSelectManager;
	}

	public IProxyMemberBankInfoManager getProxyMemberBankInfoManager() {
		return proxyMemberBankInfoManager;
	}

	public void setProxyMemberBankInfoManager(
			IProxyMemberBankInfoManager proxyMemberBankInfoManager) {
		this.proxyMemberBankInfoManager = proxyMemberBankInfoManager;
	}

	public IBankInfoManager getBankInfoManager() {
		return bankInfoManager;
	}

	public void setBankInfoManager(IBankInfoManager bankInfoManager) {
		this.bankInfoManager = bankInfoManager;
	}

}
