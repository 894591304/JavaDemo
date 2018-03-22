package com.enation.app.ext.component.goodsagent.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class GoodsAgentLatelyListTag extends BaseFreeMarkerTag{

	private IGoodsProxyManager goodsProxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private IProxyManager proxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[GoodsAgentListTag]");
		}
		Map result = new HashMap();
		int listnum = 10;
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
			int page = 1;
			request.setCharacterEncoding("utf-8");
			page = new Integer(request.getParameter("page"));
			int level = this.userAccountManager.getLevel(member.getMember_id());
			List<Goods> gList = this.goodsProxyManager.getLatelyGoods();
			List proxyGoods = new ArrayList();
			int total = gList.size();
			int allPage = total/listnum;
			int num = 0;
			int pn = 0;
			for(int i=0;i<total;i++){
				if(num>=listnum){break;}else{
				if(this.proxyManager.checkProxy(gList.get(i).getGoods_id(), member.getMember_id())!=1&&gList.get(i).getDisabled()!=1)
				{
					if(pn<(page-1)*listnum){
						pn++;
					}else{
							GoodsAgent goodsAgent = this.goodsAgentManager.get(gList.get(i).getGoods_id());
							Map tlist = new HashMap();
							tlist.put("name", gList.get(i).getName());
							tlist.put("Mktprice", goodsAgent.getMktPrice());
							tlist.put("price", goodsAgent.getPrice());
							if(level==1){tlist.put("agentprice",goodsAgent.getGoldPrice());}
							else if(level==2){tlist.put("agentprice",goodsAgent.getPlatinumPrice());}
							else {tlist.put("agentprice",goodsAgent.getBlackPrice());}
							tlist.put("goodsimg",gList.get(i).getOriginal());
							tlist.put("goodsid",String.valueOf(gList.get(i).getGoods_id()));
							tlist.put("stock", gList.get(i).getStore());
							tlist.put("onsale",this.proxyManager.getAllAgentNumByGoodsid(gList.get(i).getGoods_id()));
							proxyGoods.add(tlist);
							num++;
						}
					}
				}
			}
			result.put("allpage",String.valueOf(allPage+1));
			result.put("nowpage",String.valueOf(page+1));
			result.put("total", total);
			result.put("goodslist", proxyGoods);
			return result;
		}catch (Exception e){
			int level = this.userAccountManager.getLevel(member.getMember_id());
			List<Goods> gList = this.goodsProxyManager.getLatelyGoods();
			List proxyGoods = new ArrayList();
			int total = gList.size();
			int allPage = total/listnum;
			int num = 0;
			for(int i=0;i<total;i++){
				if(num>=listnum){break;}else{
				if(this.proxyManager.checkProxy(gList.get(i).getGoods_id(), member.getMember_id())!=1&&gList.get(i).getDisabled()!=1)
				{
						GoodsAgent goodsAgent = this.goodsAgentManager.get(gList.get(i).getGoods_id());
						Map tlist = new HashMap();
						tlist.put("name", gList.get(i).getName());
						tlist.put("Mktprice", goodsAgent.getMktPrice());
						tlist.put("price", goodsAgent.getPrice());
						if(level==1){tlist.put("agentprice",goodsAgent.getGoldPrice());}
						else if(level==2){tlist.put("agentprice",goodsAgent.getPlatinumPrice());}
						else {tlist.put("agentprice",goodsAgent.getBlackPrice());}
						tlist.put("goodsimg",gList.get(i).getOriginal());
						tlist.put("goodsid",String.valueOf(gList.get(i).getGoods_id()));
						tlist.put("stock", gList.get(i).getStore());
						tlist.put("onsale",this.proxyManager.getAllAgentNumByGoodsid(gList.get(i).getGoods_id()));
						proxyGoods.add(tlist);
						num++;
					}
				}
			}
			result.put("allpage",String.valueOf(allPage+1));
			result.put("nowpage",1+1);
			result.put("total", total);
			result.put("goodslist", proxyGoods);
			return result;
		}
	}
	
	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}
