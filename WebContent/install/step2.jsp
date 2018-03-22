<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.enation.eop.sdk.context.EopSetting" %>
<%
String runmode = EopSetting.RUNMODE;
%>
<%@ include file="header.jsp" %>

	<div class="title">
			<h1><span class="title_bg">配置数据库信息</span></h1>
		</div>


<form id="db_setting" action="install!step3.${ext }" method="post" onsubmit="return checkInput();">
<div class="content" style="text-align:left;">
			<div class="content_bg">
				<div class="content_bg02">
				<ul class="list">
						<li>
							<span class="step07">数据库类型</span>
							<!-- 使用easyui后，高度与实际差2px，宽度与实际差22px，计算好差值即可。 -->
							<select id="dbtype" name="dbtype" class="easyui-combobox database" style="height:39px;width:262px;">
								
								<option value="mysql">MySQL</option>
								<option value="oracle">Oracle</option>
								<option value="sqlserver">SQLServer</option>
							</select>
						</li>
												<li class="icon">
							<span class="step03">数据库主机</span>
							<input type="text" value="localhost:3306"  name="dbhost" id="db_host" class="page02" />
						</li>
						
												<li>
							<span class="step07">数据库用户名</span>
							<input type="text" value=""  name="uname" class="page03" id="db_uname" />
						</li>
						
												<li>
							<span class="step07">数据库密码</span>
							<input type="password" value=""  name="pwd" class="page03" id="db_passwd">
						</li>
										<li class="notice">
							<span class="step07">数据库名</span>
							<input type="text" value="" size="25" name="dbname" id="db_name" style="width: 120px;" class="page03" />
							<span class="wran01">警告！如果您制定的数据库名称已存在，此安装有可能破坏原有库中的数据！</span>
						</li>
					</ul>
					<div class="next">
						<input type="button" id="submitBtn" value="下一步"  />
					</div>
					<!-- 需要在SAAS版本中测试 -->
		<table border="0" width="100%" style="display:none;">
			<% if( runmode.equals("2") ){ %>
			<tr>
				<th align="right" scope="row"><label>域名:</label></th>
				<td><input type="text" value="" size="25" name="domain"  id="domain" class="txt" /></td>
				<td width="48%">全域名如:www.enationsoft.com，不要带端口号</td>
			</tr>
			<tr>
				<th align="right" scope="row"><label>解决方案磁盘目录:</label></th>
				<td><input type="text" value="" size="25" id="solutionpath" name="solutionpath" class="txt" /></td>
				<td>解决方案在磁盘的存储目录</td>
			</tr>    
			<%} %>
 
			<tr>
				<th align="right" scope="row"><label>模式:</label></th>
				<td>
					<input type="radio" name="devmodel" id="dev_yes"  value="1" checked="true" autocomplete="off"/>开发模式&nbsp;&nbsp;
					<input type="radio" name="devmodel" id="dev_no" value="0" autocomplete="off"/>上线运行模式
				</td>
				<td><a href="http://www.javamall.com.cn/help/index.php/开发模式和上线运行模式有什么区别" target="_blank">两个模式有什么区别？</a></td>
			</tr>
						
			<tr id="resmode_tr" style="display:none">
				<th align="right" scope="row"><label>静态资源分离:</label></th>
				<td>
					<input type="radio" name="resourcemode" id="res_no" value="2" checked="true" autocomplete="off"/>否&nbsp;&nbsp;
					<input type="radio" name="resourcemode" id="res_yes" value="1" autocomplete="off"/>是
				</td>
			</tr>
			<tr class="res_tr" style="display:none">
				<th align="right" scope="row"><label >静态服务磁盘目录:</label></th>
				<td><input type="text" value="" size="25" name="staticpath" id="staticpath" class="txt" /></td><td>默认静态资源将被安装至此目录。注意：如目录中有\请以\\代换</td>
			</tr>
			<tr class="res_tr" style="display:none">
				<th align="right" scope="row"><label>静态服务域名:</label></th>
				<td><input type="text" value="" size="25" name="staticdomain" id="staticdomain" class="txt" /></td>
				<td>使此域名可访问上述的"静态服务磁盘目录"</td>
			</tr>
		</tbody>
	</table>
	</div>
	</div> 
	</div>  
</form> 
<script type="text/javascript">
(function(a){function c(){var b=a("#loading");if(b.size()==0)b=a('<div id="loading" class="loading" style=\'z-index:3000\' />').appendTo(a("body")).hide();return b}a.Loading=a.Loading||{};a.Loading.show=function(b){var d=c();b&&d.html(b);a('<div style="height: 100%; width: 100%; position: fixed; left: 0pt; top: 0pt; z-index: 2999; opacity: 0.4;" class="jqmOverlay"></div>').appendTo(a("body"));d.show()};a.Loading.hide=function(){c().hide();a(".jqmOverlay").remove()}})(jQuery);
function checkInput(){
	if($.trim( $("#db_host").val())=='' ){
		
		alert("请输入数据库主机");
		$("#db_host")[0].focus();
		return false;
	}

	if($.trim( $("#db_uname").val() )=='' ){
		alert("请输入数据库用户名");
		$("#db_uname")[0].focus();
		return false;
	}
 
	if($.trim( $("#db_passwd").val() )=='' ){
		alert("请输入数据库密码");
		$("#db_passwd")[0].focus();
		return false;
	}

	if($.trim( $("#db_name").val() )=='' ){
		alert("请输入数据库名");
		$("#db_name")[0].focus();
		return false;
	}
	
	<% if( runmode.equals("2") ){ %>
	if($.trim( $("#domain").val() )=='' ){
		alert("请输入域名");
		$("#domain")[0].focus();
		return false;
	}

	if($.trim( $("#solutionpath").val() )=='' ){
		alert("请输入解决方案磁盘目录");
		$("#solutionpath")[0].focus();
		return false;
	}
	<%}%>
	
	if( $("#res_yes").attr("checked") ){
		if($.trim( $("#staticpath").val() )=='' ){
			alert("请输入静态资源磁盘目录");
			$("#staticpath")[0].focus();
			return false;
		}
	
		if($.trim( $("#staticdomain").val() )=='' ){
			alert("请输入入静态资源域名");
			$("#staticdomain")[0].focus();
			return false;
		}	
	}
}

function checkAndSubmit(){
	$.Loading.show('正在检测数据库连接，请稍候...');
	var options = {
		url :"install!testConnection.${ext}",
		cache:false,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			$.Loading.hide();
			if(result.result==1){
				$.Loading.show('数据库连接成功，转入下一步...');
				$("#db_setting")[0].submit();
			}else{
				alert("数据库连接失败，请检查您输入的用户名或密码。");
			}
		},
		error : function(e) {
			alert("出错啦:(");
		}
	};
	
	$("#db_setting").ajaxSubmit(options);	
}

function resctrl(){
	if( $("#res_yes").attr("checked") ){
		$(".res_tr").show();
	}
	if( $("#res_no").attr("checked") ){
		$(".res_tr").hide();
	}
}

 //db_setting
$(function(){
	$("#res_no").attr("checked",true);
	$("#submitBtn").click(function(){
		checkAndSubmit();
	});
	$("#dbtype").change(function(){
		if($("#dbtype").val()=="mysql")
			$("#db_host").val("localhost:3306");
		else if($("#dbtype").val()=="oracle")
			$("#db_host").val("localhost:1521");
		else if($("#dbtype").val()=="sqlserver")
			$("#db_host").val("localhost:1433");
	});
	
	
	$("input[name=resourcemode]").click(function(){
		resctrl();
	}); 
	
	
	$("#dev_yes").click(function(){
		$("#resmode_tr").hide();	
		 $("#res_no").attr("checked",true);
		resctrl();
	});
	
	$("#dev_no").click(function(){
		$("#resmode_tr").show();
		$("#res_no").attr("checked",true);
		resctrl();
	});
});
</script>
<script>
	$(function(){
		$(".combo-text").css({"height":"36px","line-height":"36px","text-align":"left"});
	})
</script>
<jsp:include page="footer.jsp"></jsp:include>