package com.enation.app.ext.component.usermsg.plugin;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.plugin.order.IOrderPayEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

@Component
public class UserMsgPlugin extends AutoRegisterPlugin implements IOrderPayEvent{

	@Override
	public void pay(Order order, boolean arg1) {
		System.out.println("支付成功事件已调用！");
		
	}

}
