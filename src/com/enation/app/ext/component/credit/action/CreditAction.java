package com.enation.app.ext.component.credit.action;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.mobile.service.IMobileManager;
import com.enation.app.ext.component.mobile.util.MsgSendClient;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

public class CreditAction extends WWAction{

	private static final long serialVersionUID = -2948718143215651627L;
	private ICreditManager creditManager;
	private IMobileManager mobileManager;
	private int memberId;
	private String mobile;
	private String checkcode;
	private String name;
	private String idCard;
	private String idImg;
	private String weibo;
	private String fans;
	private String weixin;
	private String friends;
	private String live;
	private String liveId;
	private String liveFans;
	
	public String submit()
	{
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能申请授信！");
			return "json_message";
		}
		if(this.creditManager.checkReviewByMemberId(member.getMember_id())==1){
			showErrorJson("已通过授信，无法修改授信资料！");
			return "json_message";
		}
		Credit credit = new Credit();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		setMemberId(member.getMember_id());
		if(StringUtil.isEmpty(this.mobile)){
			showErrorJson("手机号不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.name)){
			showErrorJson("姓名不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.idCard)){
			showErrorJson("身份证号不能为空！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.idImg)){
			showErrorJson("请上传身份证图片！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.checkcode)){
			showErrorJson("请输入验证码！");
			return "json_message";
		}
		if(!MsgSendClient.checkMsg(this.mobile, this.checkcode)){
			showErrorJson("验证码输入有误或已超时！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.weibo)){
			this.weibo = "无";
		}
		if(StringUtil.isEmpty(this.weibo)){
			this.weibo = "无";
		}
		if(StringUtil.isEmpty(this.fans)){
			this.fans = "0";
		}
		if(StringUtil.isEmpty(this.friends)){
			this.friends = "0";
		}
		if(StringUtil.isEmpty(this.liveFans)){
			this.liveFans = "0";
		}
		if(StringUtil.isEmpty(this.weixin)){
			this.weixin = "无";
		}
		if(StringUtil.isEmpty(this.live)){
			this.live = "无";
		}
		if(StringUtil.isEmpty(this.liveId)){
			this.liveId = "无";
		}
		if(Integer.parseInt(this.fans)>=99999999){
			this.liveId = "无";
		}
		credit.setMemberId(this.memberId);
		credit.setMobile(this.mobile);
		credit.setName(this.name);
		credit.setIdCard(this.idCard);
		credit.setIdImg(this.idImg);
		credit.setWeibo(this.weibo);
		credit.setFans(Integer.parseInt(this.fans));
		credit.setWeixin(this.weixin);
		credit.setFriends(Integer.parseInt(this.friends));
		credit.setLive(this.live);
		credit.setLiveId(this.liveId);
		credit.setLiveFans(Integer.parseInt(this.liveFans));
		this.creditManager.add(credit);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		this.mobileManager.delete(this.mobile);
		return "json_message";
	}
	
	public ICreditManager getCreditManager() {
		return this.creditManager;
	}
	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}
	public int getMemberId() {
		return this.memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return this.idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getWeibo() {
		return this.weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getFans() {
		return this.fans;
	}
	public void setFans(String fans) {
		this.fans = fans;
	}
	public String getWeixin() {
		return this.weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getFriends() {
		return this.friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
	}
	public String getLive() {
		return this.live;
	}
	public void setLive(String live) {
		this.live = live;
	}
	public String getLiveId() {
		return this.liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	public String getLiveFans() {
		return this.liveFans;
	}
	public void setLiveFans(String liveFans) {
		this.liveFans = liveFans;
	}
	public IMobileManager getMobileManager() {
		return mobileManager;
	}
	public void setMobileManager(IMobileManager mobileManager) {
		this.mobileManager = mobileManager;
	}
	public String getCheckcode() {
		return this.checkcode;
	}
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public String getIdImg() {
		return idImg;
	}

	public void setIdImg(String idImg) {
		this.idImg = idImg;
	}	
}
