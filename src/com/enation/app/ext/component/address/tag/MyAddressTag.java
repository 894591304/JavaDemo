package com.enation.app.ext.component.address.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.address.model.Address;
import com.enation.app.ext.component.address.service.IAddressManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAddressTag extends BaseFreeMarkerTag{

	private IAddressManager addressManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[GoodsAgentListTag]");
		}
		Map result = new HashMap();
		List nameList = new ArrayList();
		List detailList = new ArrayList();
		List<Address> aList = this.addressManager.getByMemberid(member.getMember_id());
		int count = aList.size();
		for(int i=0;i<count;i++){
			Map nameMap = new HashMap();
			Map detailMap = new HashMap();
			nameMap.put("id",String.valueOf(aList.get(i).getId()));
			nameMap.put("addressname",aList.get(i).getAddressName());
			nameList.add(nameMap);
			detailMap.put("id", String.valueOf(aList.get(i).getId()));
			detailMap.put("addressname",aList.get(i).getAddressName());
			detailMap.put("name",aList.get(i).getName());
			detailMap.put("tel",aList.get(i).getTel());
			detailMap.put("province",aList.get(i).getProvince());
			detailMap.put("city",aList.get(i).getCity());
			detailMap.put("area",aList.get(i).getArea());
			detailMap.put("address",aList.get(i).getAddress());
			detailList.add(detailMap);
		}
		result.put("namelist",nameList);
		result.put("detailList",detailList);		
		return result;
	}

	public IAddressManager getAddressManager() {
		return addressManager;
	}

	public void setAddressManager(IAddressManager addressManager) {
		this.addressManager = addressManager;
	}

	
}
