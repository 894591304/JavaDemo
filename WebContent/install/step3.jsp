<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.enation.eop.sdk.context.EopSetting" %>
<%
String runmode = EopSetting.RUNMODE;
%>
<%@ include file="header.jsp" %>
<script>
(function(a){function c(){var b=a("#loading");if(b.size()==0)b=a('<div id="loading" class="loading" style=\'z-index:3000\' />').appendTo(a("body")).hide();return b}a.Loading=a.Loading||{};a.Loading.show=function(b){var d=c();b&&d.html(b);a('<div style="height: 100%; width: 100%; position: fixed; left: 0pt; top: 0pt; z-index: 2999; opacity: 0.4;" class="jqmOverlay"></div>').appendTo(a("body"));d.show()};a.Loading.hide=function(){c().hide();a(".jqmOverlay").remove()}})(jQuery);
function do_install(str){
	if($.trim($('#ipt_uname').val())==''){
		alert('管理员用户名不能为空。');
		return false;
	}
	if($.trim($('#ipt_passwd').val())==''){
		alert('管理员密码不能为空。');
		return false;
	}
	if($('#ipt_passwd').val()!=$('#ipt_re_passwd').val()){
		alert('两次输入密码不一致。');
		return false;
	}
	return true;
}
function bindEvent(){
 $("#installBtn").click(function(){
	if(!do_install(""))
		return false;	 
	 $.Loading.show('正在安装，可能花费很长时间，请稍候...');
		var options = {
			url :"install!doInstall.${ext}",
			cache:false,
			type : "POST",
			dataType : 'json',
			success : function(result) {				
				 if(result.result==1){
						 $("#installFrm").submit();
					 }else{
						 alert("安装失败");
	 				 }
				$.Loading.hide();
			},
			error : function(e) {
				 alert(e.messages);
			}
		};

	$("#installFrm").ajaxSubmit(options);		 
 });	
}
function checkReady(){
	$.ajax({
		url :"install!testReady.${ext}",
			cache:false,
			type : "POST",
			dataType : 'json',
			success : function(result) {				
				 if(result.result==1){
					 bindEvent();	  
				 }else{
					alert("初始化失败，不能继续，可尝试重起web容器后重试.");	 
	 			}
				 $.Loading.hide();
			},error:function(){
				alert("初始化失败，不能继续，可尝试重起web容器后重试.");	 
				 $.Loading.hide();
			}
	});	
}
$(function(){
	$.Loading.show('正初始化，请稍后...');
	setTimeout('checkReady()',1000);
		  
});
</script>
	<div class="title">
		<h1><span class="title_bg">配置数据库信息</span></h1>
	</div>
	<div class="content">
		<div class="content_bg">
			<div class="content_bg02">
				<span class="success">数据库已设置并连接成功! </span>
					<div class="same">
						<span class="step07">操作系统</span>
						<span class="step08">${osVersion }</span>
					</div>
					<div class="same">
						<span class="step07">Java的运行环境</span>
						<span class="step08">${javaVersion }</span>
					</div>
					<form  action="install!installSuccess.${ext }" method="post" id="installFrm">
						<input type="hidden" name="productid" value="simple">
						<ul class="admin_list">
							<li class="same">
								<span class="step07">管理员用户名</span>
								<input type="text" tabindex="2" value="admin" id="ipt_uname" name="uname" class="page03">
							</li>
							<li class="same">
								<span class="step07">管理员密码</span>
								<input type="password" tabindex="3" id="ipt_passwd" name="pwd" class="page03">
							</li>
							<li class="same">
								<span class="step07">再输入一次密码</span>
								<input type="password" tabindex="4" id="ipt_re_passwd" name="re_passwd" class="page03">
							</li>
						</ul>
					</form>
					<div  class="start_install"><input id="installBtn" type="button"  value="开始安装" class="button_next"></div>
				</div>
			</div>
		</div>
	<table style="display:none">
  		<tbody>
   			<input type="hidden" name="productid" value="eopsaler"  >
  			<input type="hidden" name="domain" value="${domain }"  >
		</tbody>
	</table>
<jsp:include page="footer.jsp"></jsp:include>
 