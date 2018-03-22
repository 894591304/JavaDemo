package com.enation.app.ext.component.bankinfo.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.bankinfo.model.BankInfo;
import com.enation.app.ext.component.bankinfo.service.IBankInfoManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BankInfoListTag extends BaseFreeMarkerTag{

	private IBankInfoManager bankInfoManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[BankInfoListTag]");
		}
		List<BankInfo> bList = this.bankInfoManager.getAll();
		Map result = new HashMap();
		List bankList = new ArrayList();
		int count = bList.size();
		for(int i=0;i<count;i++){
			Map bMap = new HashMap();
			BankInfo bankInfo = bList.get(i);
			bMap.put("banknum", bankInfo.getId());
			bMap.put("bankname",bankInfo.getBankname());
			bankList.add(bMap);
		}
		result.put("banklist",bankList);
		return result;
	}

	public IBankInfoManager getBankInfoManager() {
		return bankInfoManager;
	}

	public void setBankInfoManager(IBankInfoManager bankInfoManager) {
		this.bankInfoManager = bankInfoManager;
	}

}
