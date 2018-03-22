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
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.ext.core.service.INewGoodsCatManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAgentSearchGoodsTag extends BaseFreeMarkerTag{
	
	private IGoodsProxyManager goodsProxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private IProxyManager proxyManager;
	private INewGoodsCatManager newGoodsCatManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyAgentOnSaleListTag]");
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
			String keyword = null;
			request.setCharacterEncoding("utf-8");
			keyword = new String(request.getParameter("keyword").getBytes("iso8859-1"),"utf-8");
			if(keyword==null){
				result.put("search",2);
				return result;
			}
			List<Goods> gList= this.goodsProxyManager.search(keyword);
			if(gList.size()==0){
				result.put("search",0);
				return result;
			}
			int total = gList.size();
			List goodsList = new ArrayList();
			List proxyGoods = new ArrayList();
			List proxyEndGoods = new ArrayList();
			int total1 = 0;
			int total2 = 0;
			int num = 0;
			for(int i=0;i<total;i++){
				Map gMap = new HashMap();
				Goods goods = gList.get(i);
				if(this.proxyManager.checkProxy(goods.getGoods_id(), member.getMember_id())!=0)
				{
					List<Proxy> proxyList= this.proxyManager.getByGoodsIdAndMemberId(goods.getGoods_id(),member.getMember_id());
					int t = proxyList.size();
					if(t!=0){result.put("search",1);}
					for(int i1=0;i1<t;i1++){
						if(this.proxyManager.checkProxyCanBuy(proxyList.get(i1).getId())!=0){
							Map msgMap = new HashMap();
							Goods goods2 = this.goodsProxyManager.getGoods(proxyList.get(i1).getGoodsId());
							GoodsAgent goodsAgent = this.goodsAgentManager.get(proxyList.get(i1).getGoodsId());
							msgMap.put("proxyid", String.valueOf(proxyList.get(i1).getId()));
							msgMap.put("goodsimg", goods2.getOriginal());
							msgMap.put("goodsname", goods2.getName());
							msgMap.put("goodsid",String.valueOf( proxyList.get(i1).getGoodsId()));
							msgMap.put("price", goodsAgent.getPrice());
							msgMap.put("expectearn",goodsAgent.getPrice()*proxyList.get(1).getGoodsAmount()-proxyList.get(i1).getFrozenCredit()*10);
							msgMap.put("sold", proxyList.get(i1).getSale());
							msgMap.put("notsold",proxyList.get(i1).getOnSale()-proxyList.get(i1).getSale());
							msgMap.put("notonsale",proxyList.get(i1).getGoodsAmount()-proxyList.get(i1).getOnSale());
							msgMap.put("earn",proxyList.get(i1).getFrozenEarn());
							msgMap.put("testtime",proxyList.get(i1).getProxyTestTime());
							msgMap.put("endtime",proxyList.get(i1).getProxyEndTime());
							Long nTime = System.currentTimeMillis()/1000;
							Long lTime =(System.currentTimeMillis()+1000*60*60*24*2)/1000;			/*当前时间之前的三天*/ 
							Long tTime =Long.valueOf(proxyList.get(i1).getProxyTestTime());
							Long eTime =Long.valueOf(proxyList.get(i1).getProxyEndTime());
							msgMap.put("testlosttime", (tTime-nTime)/60/60/24);
							msgMap.put("losttime", (eTime-nTime)/60/60/24);
							if(proxyList.get(i1).getProxyTestTime().equals(proxyList.get(i1).getProxyEndTime())){
								msgMap.put("test",2);			//不可延期
							}else if(lTime<tTime){
								msgMap.put("test",0);			//不可延期
							}else{
								msgMap.put("test",1);			//可以延期
							}
							proxyGoods.add(msgMap);							
							total1++;
						}else{
							Goods goods2 = this.goodsProxyManager.getGoods(proxyList.get(i1).getGoodsId());
							GoodsAgent goodsAgent = this.goodsAgentManager.get(proxyList.get(i1).getGoodsId());
							Map msgMap = new HashMap();
							msgMap.put("proxyid", String.valueOf(proxyList.get(i).getId()));
							msgMap.put("goodsimg", goods2.getOriginal());
							msgMap.put("goodsname", goods2.getName());
							msgMap.put("goodsid", String.valueOf(proxyList.get(i).getGoodsId()));
							msgMap.put("price", goodsAgent.getPrice());
							msgMap.put("sold", proxyList.get(i).getSale());
							msgMap.put("notsold",proxyList.get(i).getOnSale()-proxyList.get(i).getSale());
							msgMap.put("notonsale",proxyList.get(i).getGoodsAmount()-proxyList.get(i).getOnSale());
							msgMap.put("endtime", proxyList.get(i).getProxyEndTime());
							msgMap.put("earn",proxyList.get(i).getFrozenEarn());
							proxyEndGoods.add(msgMap);	
							total2++;
						}
					}
				}
			}
			result.put("total1", total1);
			result.put("total2", total2);
			result.put("proxylist",proxyGoods);
			result.put("proxyendlist",proxyEndGoods);
			return result;			
		}
		catch (Exception e)
		{
			this.logger.error("查询失败！", e);
			result.put("search", 3);
		}
		return result;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
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

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public INewGoodsCatManager getNewGoodsCatManager() {
		return newGoodsCatManager;
	}

	public void setNewGoodsCatManager(INewGoodsCatManager newGoodsCatManager) {
		this.newGoodsCatManager = newGoodsCatManager;
	}

}
