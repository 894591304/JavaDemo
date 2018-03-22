package com.enation.app.ext.component.mobile.plugin;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.plugin.member.IMemberRegisterEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

@Component
public class MobilePlugin extends AutoRegisterPlugin implements IMemberRegisterEvent{

	@Override
	public void onRegister(Member arg0) {	
	}

}
