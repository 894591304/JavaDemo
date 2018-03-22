package com.enation.app.ext.component.proxyMemberBankInfo.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BankInfoTag extends BaseFreeMarkerTag{

	private IProxyMemberBankInfoManager proxyMemberBankInfoManager;
	private IBankSelectManager bankSelectManager;
	private IBankInfoManager bankInfoManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[BankInfoTag]");
		}
		Map result = new HashMap();
		try
		{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map<String, String> params = new HashMap();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String)iter.next();
				String[] values = (String[])requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			int infoid = 0;
			if(request.getParameter("infoid")==null){
				result.put("re", 1);
				result.put("infoid", 0);
				result.put("type",1);
				result.put("cardnum","");
				result.put("banknum","");
				result.put("bankname","");
				result.put("holder","");
				result.put("branchname","");
				result.put("provincename","北京市");
				result.put("cityname","北京市市辖区");
				result.put("typename","个人账户");
				result.put("defaultc",0);
				return result;
			}else{
				infoid = Integer.valueOf(request.getParameter("infoid"));
				ProxyMemberBankInfo proxyMemberBankInfo = this.proxyMemberBankInfoManager.get(infoid);
				BankSelect bankSelect = this.bankSelectManager.getByInfoId(infoid);
				BankInfo bankInfo = this.bankInfoManager.getById(Integer.valueOf(proxyMemberBankInfo.getPayeeBankNum()));
				if(proxyMemberBankInfo==null){
					result.put("re", 0);
					return result;
				}
				if(proxyMemberBankInfo.getProxyMemberId()!=member.getMember_id()){
					result.put("re", 0);
					return result;
				}else{
					result.put("re", 1);
					result.put("infoid", proxyMemberBankInfo.getId());
					result.put("type", proxyMemberBankInfo.getPayeeBankAccountType());
					result.put("cardnum",proxyMemberBankInfo.getPayeeBankCard());
					result.put("banknum",proxyMemberBankInfo.getPayeeBankNum());
					result.put("bankname", bankInfo.getBankname());
					result.put("holder",proxyMemberBankInfo.getPayeeCardHolder());
					result.put("branchname",proxyMemberBankInfo.getPayeeBankBranchName());
					result.put("provincename",proxyMemberBankInfo.getPayeeBankProvinceName());
					result.put("cityname",proxyMemberBankInfo.getPayeeBankCityName());
					result.put("defaultc",bankSelect.getStatus());
					String typename = "个人账户";
					if(proxyMemberBankInfo.getPayeeBankAccountType()==2){
						typename = "企业账户";
					}
					result.put("typename", typename);
				}			
			}			
		}catch (Exception e){
			this.logger.error("查询失败！", e);
		}
		return result;
	}

	public IProxyMemberBankInfoManager getProxyMemberBankInfoManager() {
		return proxyMemberBankInfoManager;
	}

	public void setProxyMemberBankInfoManager(
			IProxyMemberBankInfoManager proxyMemberBankInfoManager) {
		this.proxyMemberBankInfoManager = proxyMemberBankInfoManager;
	}

	public IBankSelectManager getBankSelectManager() {
		return bankSelectManager;
	}

	public void setBankSelectManager(IBankSelectManager bankSelectManager) {
		this.bankSelectManager = bankSelectManager;
	}

	public IBankInfoManager getBankInfoManager() {
		return bankInfoManager;
	}

	public void setBankInfoManager(IBankInfoManager bankInfoManager) {
		this.bankInfoManager = bankInfoManager;
	}

}
