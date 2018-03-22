<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.enation.eop.sdk.context.EopSetting" %>
<%@ include file="header.jsp" %>

		<div class="title">
			<h1><span class="title_bg">配置数据库信息</span></h1>
		</div>

 		<div class="content">
			<div class="content_bg">
				<div class="content_bg02">
						<span class="install_success">系统安装成功! </span>
						<div class="user_info">
							<span class="shop_info">以下是您的商店系统管理员账户信息：</span>
							<p>用户名：<span>${uname }</span></p>
							<p>密码：<span>${pwd }</span></p>
							
						</div>
						<div class="check">
							<a target="_blank" href="../index.html" class="long_front">查看网站前台</a>
							<a target="_blank" href="../admin" class="long_later">查看网站后台</a>
						</div>
				</div>
			</div>	
		</div>
<jsp:include page="footer.jsp"></jsp:include>