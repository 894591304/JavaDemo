package com.enation.app.b2b2c.component.creditmember.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.b2b2c.component.creditmember.service.ICreditMemberManager;
import com.enation.app.b2b2c.util.ExcelReader;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.ext.component.mobile.service.IMobileManager;
import com.enation.app.ext.component.mobile.service.IMobileMemberManager;
import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;

public class CreditMemberAction extends WWAction {

	private static final long serialVersionUID = -8618054430286090133L;

	private ICreditMemberManager creditMemberManager;
	private IMemberShopManager memberShopManager;
	private ICreditManager creditManager;
	private IMemberManager memberManager;
	private IMobileManager mobileManager;
	private IAdminUserManager adminUserManager;
	private IMobileMemberManager mobileMemberManager;
	private IUserAccountManager userAccountManager;
	private IBrokerageManager brokerageManager;
	private AdminUser currentUser;
	private Member member;
	private Credit credit;
	private List creditMemberList;
	private HashMap memberMap;
	private Integer sex = Integer.valueOf(2);
	private String uname;
	private Integer mobile;
	private String email;
	private String start_time;
	private String end_time;
	private Integer stype;
	private String keyword;
	private Integer province_id;
	private Integer city_id;
	private Integer region_id;
	private Integer lvId;
	private Integer review;
	private String birthday;
	private String[] idImgList;
	private int memberId;
	private String idCard;
	private String idImg;
	private String weibo;
	private String fans;
	private String weixin;
	private String friends;
	private String live;
	private String liveId;
	private String liveFans;
	private String accountId;
	private int creditLevel;
	private String credit_num;
	private String import_file;
	private String type;
	private String today;
	private String firstday;
	private String lastday;
	private int showOrHide;
	private String memberids;
	private MemberShop memberShop;
	private String shopName;
	private String label;
	private String shopIntro;
	private String memberImg;
	private String shopImg;
	private String qq;
	private String phone;

	public String creditMemberList() {
		this.currentUser = this.adminUserManager.getCurrentUser();
		return "creditMemberList";
	}

	public String creditList() {
		return "creditList";
	}

	public String add_creditMember() {
		return "add_creditMember";
	}

	public String import_creditMember() {
		return "import_creditMember";
	}
	
	public String creditProxyList() {
		Calendar now = Calendar.getInstance();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		this.today = sdf.format(now.getTime());
		now = Calendar.getInstance();  
		now.add(Calendar.MONTH, 0);  
        now.set(Calendar.DAY_OF_MONTH, 1);  
        this.firstday = sdf.format(now.getTime());
        now = Calendar.getInstance();  
        now.add(Calendar.MONTH, 1);  
        now.set(Calendar.DAY_OF_MONTH, 0);  
        this.lastday = sdf.format(now.getTime());
		return "creditProxyList";
	}

	public String importCreditMember() {
		ActionContext ac = ActionContext.getContext();   
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);   
        String path = sc.getRealPath("/"); 
		String targetPath = path + this.import_file;
		try {
			ExcelReader excelReader = new ExcelReader();
			InputStream is = new FileInputStream(targetPath);
			Map<Integer, List<String>> map = excelReader.readExcelContent(is);
			this.creditManager.importCredit(map);
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
		showSuccessJson("导入会员成功");
		return "json_message";
	}

	public String edit_creditMember() {
		this.member = this.memberManager.get(this.memberId);
		this.credit = this.creditManager.get(this.memberId);
		String idImg = this.credit.getIdImg();
		if (idImg != null) {
			this.idImgList = idImg.split(",");
		}
		this.memberShop = this.memberShopManager.get(this.memberId);
		if (this.memberShop==null) {
			this.memberShop = new MemberShop();
			memberShop.setMemberId(this.memberId);
			memberShop.setLevel(9);
			memberShop.setAgencyId(-1);
			this.memberShopManager.add(memberShop);
		}
		return "edit_creditMember";
	}

	public String detail() {
		this.member = this.memberManager.get(this.memberId);
		return "creditMember_detail";
	}

	public String setShopShowHide() {
		return "set_shop_show_hide";
	}
	
	public String saveShopShowHide(){
		this.memberShopManager.setShowOrHide(this.showOrHide,this.memberids);
		showSuccessJson("设置网红店铺"+(this.showOrHide==1?"可见":"不可见")+"成功");
		return "json_message";
	}
	
	public String creditDetail() {
		this.member = this.memberManager.get(this.memberId);
		this.credit = this.creditManager.get(this.memberId);
		String idImg = this.credit.getIdImg();
		if (idImg != null) {
			this.idImgList = idImg.split(",");
		}
		this.memberShop = this.memberShopManager.get(this.memberId);
		if (this.memberShop==null) {
			this.memberShop = new MemberShop();
			memberShop.setMemberId(this.memberId);
			memberShop.setLevel(9);
			memberShop.setAgencyId(-1);
			this.memberShopManager.add(memberShop);
		}
		return "credit_detail";
	}

	public String credit() {
		try {
			if (this.review == 1) {
				UserAccount ua = new UserAccount();
				ua.setAccountId(this.accountId);
				ua.setCredit(Float.parseFloat(this.credit_num));
				ua.setCreditLevel(this.creditLevel);
				ua.setMemberId(this.memberId);
				ua.setRemainCredit(Float.parseFloat(this.credit_num));
				ua.setAddEarn(0);
				ua.setRepay(0);
				ua.setWaitCash(0);
				ua.setWaitMoney(0);
				ua.setRepayTime(null);
				this.userAccountManager.add(ua);
			}
			this.creditMemberManager.creditMember(this.memberId, this.review);
			this.memberShop = new MemberShop();
			memberShop.setMemberId(this.memberId);
			memberShop.setLevel(9);
			memberShop.setAgencyId(-1);
			this.memberShopManager.add(memberShop);
			showSuccessJson("审核授信成功", this.memberId);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorJson("审核授信失败");
		}
		return "json_message";
	}

	public String creditMemberlistJson() {
		this.memberMap = new HashMap();
		this.memberMap.put("stype", this.stype);
		this.memberMap.put("keyword", this.keyword);
		this.memberMap.put("uname", this.uname);
		this.memberMap.put("mobile", this.mobile);
		this.memberMap.put("lvId", this.lvId);
		this.memberMap.put("email", this.email);
		this.memberMap.put("sex", this.sex);
		this.memberMap.put("start_time", this.start_time);
		this.memberMap.put("end_time", this.end_time);
		this.memberMap.put("province_id", this.province_id);
		this.memberMap.put("city_id", this.city_id);
		this.memberMap.put("region_id", this.region_id);
		this.memberMap.put("review", 1);
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.memberMap.put("brokerageId", currentUser.getUserid());
		}
		this.webpage = this.creditMemberManager.searchCreditMember(this.memberMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}

	public String creditWaitedlistJson() {
		this.memberMap = new HashMap();
		this.memberMap.put("stype", this.stype);
		this.memberMap.put("keyword", this.keyword);
		this.memberMap.put("uname", this.uname);
		this.memberMap.put("mobile", this.mobile);
		this.memberMap.put("lvId", this.lvId);
		this.memberMap.put("email", this.email);
		this.memberMap.put("sex", this.sex);
		this.memberMap.put("start_time", this.start_time);
		this.memberMap.put("end_time", this.end_time);
		this.memberMap.put("province_id", this.province_id);
		this.memberMap.put("city_id", this.city_id);
		this.memberMap.put("region_id", this.region_id);
		this.memberMap.put("review", 0);
		this.webpage = this.creditMemberManager.searchCreditMember(this.memberMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}
	
	
	public String creditProxylistJson() {
		this.memberMap = new HashMap();
		this.memberMap.put("start_time", this.start_time);
		this.memberMap.put("end_time", this.end_time);
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			this.memberMap.put("brokerageId", currentUser.getUserid());
		}
		this.webpage = this.creditMemberManager.searchCreditProxy(this.memberMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()));
		showGridJson(this.webpage);
		return "json_message";
	}

	public String savecreditMember() {
		if ((this.member.getUname().length() < 2) || (this.member.getUname().length() > 20)) {
			showErrorJson("用户名的长度为2-20个字符！");
			return "json_message";
		}
		if (this.member.getMobile() == null) {
			showErrorJson("注册手机号不能为空！");
			return "json_message";
		}
		if (!this.member.getMobile().matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
			showErrorJson("手机号输入有误，请重新输入！");
			return "json_message";
		}
		if (StringUtil.isEmpty(this.member.getPassword()) || this.member.getPassword().equals("请输入登录密码")) {
			showErrorJson("密码不能为空！");
			return "json_message";
		}
		if (this.memberManager.checkname(this.member.getUname()) > 0) {
			showErrorJson("此用户名已经存在，请您选择另外的用户名！");
			return "json_message";
		}
		if (this.mobileMemberManager.checkmobile(this.member.getMobile()) > 0) {
			showErrorJson("此手机号已经注册，请您选择另外的手机号！");
			return "json_message";
		}
		this.member.setBirthday(Long.valueOf(DateUtil.getDatelineLong(this.birthday)));
		this.member.setPassword(this.member.getPassword());
		this.member.setRegtime(Long.valueOf(DateUtil.getDatelineLong()));
		this.memberManager.add(this.member);
		int member_Id = this.mobileMemberManager.getMemberidByMobile(this.member.getMobile());
		Credit credit = new Credit();
		credit.setMemberId(member_Id);
		credit.setMobile(this.member.getMobile());
		credit.setName(this.member.getName());
		credit.setIdCard(this.idCard);
		credit.setIdImg(this.idImg);
		credit.setWeibo(this.weibo);
		credit.setFans(Integer.parseInt(this.fans));
		credit.setWeixin(this.weixin);
		credit.setFriends(Integer.parseInt(this.friends));
		credit.setLive(this.live);
		credit.setLiveId(this.liveId);
		credit.setLiveFans(Integer.parseInt(this.liveFans));
		this.currentUser = this.adminUserManager.getCurrentUser();
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			credit.setBrokerageId(currentUser.getUserid());
		}
		this.creditManager.add(credit);
		showSuccessJson("保存网红用户成功，请审核该用户！", member_Id);
		return "json_message";
	}

	public String saveEditcreditMember() {
		credit = this.creditManager.get(this.memberId);
		credit.setIdCard(this.idCard);
		credit.setIdImg(this.idImg);
		credit.setWeibo(this.weibo);
		credit.setFans(Integer.parseInt(this.fans));
		credit.setWeixin(this.weixin);
		credit.setFriends(Integer.parseInt(this.friends));
		credit.setLive(this.live);
		credit.setLiveId(this.liveId);
		credit.setLiveFans(Integer.parseInt(this.liveFans));
		this.creditManager.edit(credit);
		this.memberShop = this.memberShopManager.get(this.memberId);
		memberShop.setMemberId(this.memberId);
		memberShop.setShopName(this.shopName);
		memberShop.setLabel(this.label);
		memberShop.setShopIntro(this.shopIntro);
		memberShop.setMemberImg(this.memberImg);
		memberShop.setShopImg(this.shopImg);
		memberShop.setMobile(this.phone);
		memberShop.setQq(this.qq);
		this.memberShopManager.edit(this.memberShop);
		showSuccessJson("修改网红用户成功！", this.memberId);
		return "json_message";
	}

	public ICreditMemberManager getCreditMemberManager() {
		return creditMemberManager;
	}

	public void setCreditMemberManager(ICreditMemberManager creditMemberManager) {
		this.creditMemberManager = creditMemberManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List getCreditMemberList() {
		return creditMemberList;
	}

	public void setCreditMemberList(List creditMemberList) {
		this.creditMemberList = creditMemberList;
	}

	public HashMap getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(HashMap memberMap) {
		this.memberMap = memberMap;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Integer getMobile() {
		return mobile;
	}

	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}

	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public Integer getLvId() {
		return lvId;
	}

	public void setLvId(Integer lvId) {
		this.lvId = lvId;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public IMobileManager getMobileManager() {
		return mobileManager;
	}

	public void setMobileManager(IMobileManager mobileManager) {
		this.mobileManager = mobileManager;
	}

	public IMobileMemberManager getMobileMemberManager() {
		return mobileMemberManager;
	}

	public void setMobileMemberManager(IMobileMemberManager mobileMemberManager) {
		this.mobileMemberManager = mobileMemberManager;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIdImg() {
		return idImg;
	}

	public void setIdImg(String idImg) {
		this.idImg = idImg;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getLive() {
		return live;
	}

	public void setLive(String live) {
		this.live = live;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public String getLiveFans() {
		return liveFans;
	}

	public void setLiveFans(String liveFans) {
		this.liveFans = liveFans;
	}

	public String[] getIdImgList() {
		return idImgList;
	}

	public void setIdImgList(String[] idImgList) {
		this.idImgList = idImgList;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(int creditLevel) {
		this.creditLevel = creditLevel;
	}

	public String getCredit_num() {
		return credit_num;
	}

	public void setCredit_num(String credit_num) {
		this.credit_num = credit_num;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
	}

	public AdminUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(AdminUser currentUser) {
		this.currentUser = currentUser;
	}

	public String getImport_file() {
		return import_file;
	}

	public void setImport_file(String import_file) {
		this.import_file = import_file;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getFirstday() {
		return firstday;
	}

	public void setFirstday(String firstday) {
		this.firstday = firstday;
	}

	public String getLastday() {
		return lastday;
	}

	public void setLastday(String lastday) {
		this.lastday = lastday;
	}

	public IMemberShopManager getMemberShopManager() {
		return memberShopManager;
	}

	public void setMemberShopManager(IMemberShopManager memberShopManager) {
		this.memberShopManager = memberShopManager;
	}

	public int getShowOrHide() {
		return showOrHide;
	}

	public void setShowOrHide(int showOrHide) {
		this.showOrHide = showOrHide;
	}

	public String getMemberids() {
		return memberids;
	}

	public void setMemberids(String memberids) {
		this.memberids = memberids;
	}

	public MemberShop getMemberShop() {
		return memberShop;
	}

	public void setMemberShop(MemberShop memberShop) {
		this.memberShop = memberShop;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getShopIntro() {
		return shopIntro;
	}

	public void setShopIntro(String shopIntro) {
		this.shopIntro = shopIntro;
	}

	public String getMemberImg() {
		return memberImg;
	}

	public void setMemberImg(String memberImg) {
		this.memberImg = memberImg;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
