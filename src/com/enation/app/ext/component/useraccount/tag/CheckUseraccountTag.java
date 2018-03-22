package com.enation.app.ext.component.useraccount.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CheckUseraccountTag extends BaseFreeMarkerTag{

	private IProxyManager proxyManager;
	private IUserAccountManager userAccountManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[CheckUseraccountTag]");
		}
		UserAccount userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		if(userAccount==null){
			return 0;
		}
		List<Proxy> pList = this.proxyManager.getAllByMemberid(member.getMember_id());
		int total = pList.size();
		for(int i=0;i<total;i++){
			userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
			Proxy proxy = pList.get(i);
			if(proxy.getStatus()==5){
				if(proxy.getProxyTestTime()!=proxy.getProxyEndTime()){
					proxy.setStatus(0);
					userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
					userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn());
					this.userAccountManager.update(userAccount);
					proxy.setFrozenDeposit(0);
					proxy.setFrozenCredit(0);
					this.proxyManager.edit(proxy);
				}else{
					proxy.setStatus(0);
					Long sale = Long.valueOf(proxy.getSale());
					Long goodsAmount =Long.valueOf(proxy.getGoodsAmount());
					if(sale>=goodsAmount*0.5){
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn());
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						this.proxyManager.edit(proxy);
					}else if(goodsAmount*0.3<=sale&&sale<goodsAmount*0.5){
						proxy.setFrozenEarn(proxy.getFrozenEarn()-proxy.getFrozenCredit());
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn());
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						this.proxyManager.edit(proxy);						
					}else if(sale<goodsAmount*0.3&&sale>=goodsAmount*0.1){
						proxy.setFrozenEarn(proxy.getFrozenEarn()-proxy.getFrozenCredit());
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn());
						Long nowTime = System.currentTimeMillis();
						int nTime = Integer.valueOf(String.valueOf(nowTime))/1000;
						int eTime = nTime+60*60*24*60;
						if(userAccount.getRepayTime()!=null){
							if(Integer.valueOf(userAccount.getRepayTime())>=eTime){
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRepayTime(String.valueOf(eTime));
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						this.proxyManager.edit(proxy);
					}else{
						proxy.setFrozenEarn(proxy.getFrozenEarn()-proxy.getFrozenCredit());
						userAccount.setRemainCredit(userAccount.getRemainCredit()+proxy.getFrozenCredit());
						userAccount.setWaitCash(userAccount.getWaitCash()+proxy.getFrozenEarn());
						Long nowTime = System.currentTimeMillis();
						int nTime = Integer.valueOf(String.valueOf(nowTime))/1000;
						int eTime = nTime+60*60*24*30;
						if(userAccount.getRepayTime()!=null){
							if(Integer.valueOf(userAccount.getRepayTime())>=eTime){
								userAccount.setRepayTime(String.valueOf(eTime));
							}
						}else{
							userAccount.setRepayTime(String.valueOf(eTime));
						}
						this.userAccountManager.update(userAccount);
						proxy.setFrozenDeposit(0);
						proxy.setFrozenCredit(0);
						this.proxyManager.edit(proxy);						
					}
				}
				
			}			
		}
		userAccount = this.userAccountManager.getByMemberId(member.getMember_id());
		float credit = userAccount.getCredit();
		float remainCredit = userAccount.getRemainCredit();
		float waitCash = userAccount.getWaitCash();
		float repay = userAccount.getRepay();
		float newRemainCredit=0;
		float newWaitCash=0;
		float newRepay=0;
		if(waitCash>=credit-remainCredit&&userAccount.getRepayTime()!=null&&userAccount.getRepayTime()!=""){
			newRemainCredit = credit;
			newWaitCash = waitCash-(credit-remainCredit);
			userAccount.setRemainCredit(newRemainCredit);
			userAccount.setWaitCash(newWaitCash);
			userAccount.setRepay(newRepay);
			if(newRepay==0){
				userAccount.setRepayTime(null);
			}
			this.userAccountManager.update(userAccount);
		}else if(userAccount.getRepayTime()!=null&&userAccount.getRepayTime()!=""){
			newWaitCash = 0;
			newRemainCredit = remainCredit+waitCash;
			newRepay=credit-newRemainCredit;
			userAccount.setRemainCredit(newRemainCredit);
			userAccount.setWaitCash(newWaitCash);
			userAccount.setRepay(newRepay);
			if(newRepay==0){
				userAccount.setRepayTime(null);
			}
			this.userAccountManager.update(userAccount);
		}else{
			
		}		
		return 1;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

}
