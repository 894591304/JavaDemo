package com.enation.app.ext.component.usermsg.plugin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.ext.component.usermsg.UserMsg;
import com.enation.app.ext.component.usermsg.service.IUserMsgManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.plugin.order.IAfterOrderCreateEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

@Component
public class UserTestMsgPlugin extends AutoRegisterPlugin implements IAfterOrderCreateEvent
{
	private IUserMsgManager userMsgManager;
	
	public void onAfterOrderCreate(Order order, List<CartItem> list, String sessionid) 
	{
		System.out.println("订单创建插件调用！");
		int size =list.size();
		for(int i=0;i<size;i++)
		{
			UserMsg userMsg = new UserMsg();
			int memberid;
			int goodsid;
			goodsid=list.get(i).getGoods_id();
			memberid=order.getMember_id();
			String msg = "订单已付款！";
			String url = Integer.toString(memberid);
			userMsg.setMemberId(memberid);
			userMsg.setMemberMsg(msg);
			userMsg.setGoodsId(url);
			System.out.println("1");
			this.userMsgManager.add(userMsg);
			System.out.println("4");
		}
	}
	public IUserMsgManager getUserMsgManager()
	{
		return this.userMsgManager;
	}
	
	public void setUserMsgManager(IUserMsgManager userMsgManager)
	{
		this.userMsgManager = userMsgManager;
	}
}
