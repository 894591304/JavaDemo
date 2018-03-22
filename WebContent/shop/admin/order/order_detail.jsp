<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<script type="text/javascript" src="../../adminthemes/new/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../../adminthemes/new/js/eop-min.js"></script>
<script type="text/javascript" src="../../adminthemes/new/js/jquery-form-2.33.js"></script>

<link href="../../adminthemes/default/css/dialog.css" rel="stylesheet" type="text/css" />
<link href="../../adminthemes/new/css/global.css" rel="stylesheet" type="text/css" />
<link href="../../adminthemes/new/css/main.css" rel="stylesheet" type="text/css" />

<script	type="text/javascript" src="/editor/ckeditor362/ckeditor.js"></script>
<script	type="text/javascript" src="/editor/ckeditor362/adapters/jquery.js"></script>

<script type="text/javascript" src="../../adminthemes/new/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="js/OrderDetail.js"></script>
<style>
<!--
.box {width:250px;display:block;float:left}

.division {
	background: none repeat scroll 0 0 #FFFFFF;
	border-color: #CCCCCC #BEC6CE #BEC6CE #CCCCCC;
	border-style: solid;
	border-width: 1px 2px 2px 1px;
	line-height: 150%;
	margin: 10px;
	padding: 5px;
	white-space: normal;
}

.division table {
	margin: 0;
	padding: 0;
	width:100%
}

.orderdetails_basic th {
	color: #336699;
	text-align: left;
	white-space: nowrap;
}

.division th {
	background: none repeat scroll 0 0 #E2E8EB;
	border-right: 1px solid #CCCCCC;
	font-size: 14px;
	text-align: right;
	white-space: nowrap;
	width: 140px;
}

.division th,.division td {
	border-color: #FFFFFF #DBE2E7 #DDDDDD #FFFFFF;
	border-left: 1px solid #FFFFFF;
	border-right: 1px solid #DBE2E7;
	border-style: solid;
	border-width: 1px;
	padding: 5px;
	vertical-align: top;
}

.tableform {
	background: none repeat scroll 0 0 #EFEFEF;
	border-color: #DDDDDD #BEC6CE #BEC6CE #DDDDDD;
	border-style: solid;
	border-width: 1px;
	margin: 10px;
	padding: 5px;
}

h5 {
	font-size: 1em;
	font-weight: bold;
}

h1,h2,h3,h4,h5,h6 {
	clear: both;
	color: #111111;
	margin: 0.5em 0;
}

#order_dialog .con {
	background: none repeat scroll 0 0 #FFFFFF;
	overflow-x: hidden;
	overflow-y: auto;
	height: 400px;
	visibility: visible;
	opacity: 1;
	position: relative;
}

.allo_box {
	background: none repeat scroll 0 0 #EEEEEE;
	border: 1px solid #CCCCCC;
	position: absolute;
	width: 300px;
	display: none;
}

.allo_box li {
	line-height: 30px;
	border-bottom: 1px solid #ccc
}

.allo_items li.selected {
	background-color: #00EE76
}

;
.close_box a {
	float: right
}
-->
</style>

<div class="main">
	<div class="toolbar">
		<form id="nextForm" action="" method="post">
			<input type="button"  id=confirmorder value="确认订单" />
			<input type="button"  id="pay" value="确认收款" />
	
			<input type="button"  id="rog" value="确认收货" disabled="disabled"/>
			<input type="button"  id="returned" value="退货" />
			<input type="button"  id="cancel" value="作废" />
			<span style="float:right">
				<input type="button" class="button blueButton" id="cancel" value="打印快递单" />
				<input type="button" class="button blueButton" id="cancel" value="打印发货单" />
			</span>
			<span id="nextorder">
				<input type="hidden" name="orderId" value="${orderId}"> 
				<input type="hidden" name="sn" value="${sn}">
				<input type="hidden" name="logi_no" value="${logi_no}"> 
				<input type="hidden" name="uname" value="${uname}">
				<input type="hidden" name="ship_name" value="${ship_name}">
				<input type="hidden" name="status" value="${status}">
				<input type="hidden" id="alert_null" value="${alert_null}">
				<input id="nextvalue" type="hidden" name="next" value="">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="previous" class="button blueButton" type="button" value="上一订单">
				<input id="next" class="button blueButton" type="button"	value="下一订单">
			</span>
		</form>
		<div style="clear: both"></div>
	</div>
	<div id="memberpricedlg"></div>
	<div class="input">
		<form id="order_form">
			<div style="display: block;" class="order_detail">
				<div class="contentTab" >
					<ul >
						<c:forEach var="tab" items="${pluginTabs}" varStatus="status">
							<li tabid="${tab.key }" <c:if test="${status.index==0 }">class="contentTabS"</c:if>>${tab.value }</li>
						</c:forEach>
					</ul>
				</div>
				<div class="shadowBoxWhite wf100 whiteBox">
					<div class="text">
						<div class="tab-page">
						 	<c:forEach var="content" items="${pluginHtmls}" varStatus="status">
						 	  	<div tabid="${content.key }" <c:if test="${status.index!=0 }">style="display:none"</c:if>  class="tab-panel">
						 	  		${content.value }
						 	  	</div>
						 	</c:forEach>
						</div>
						<div id="order_dialog" class="bottomButtonArea" align="center">
							<form id="order_form">
								<input type="hidden" id="orderid" name="orderId" value="${orderId }" />
								<div class="con"></div>
							</form>
							<table>
								<tr>
									<td >
							 			<input name="submit" type="button" value="确    定 " id="goodsinput" class="button blueButton" />
							  		</td>
							 	</tr>
						 	</table>
						</div>
					</div>	 	  		
				</div>
			</div>
		</form>
	</div>
</div>

<script>


$(function(){
	var instance = CKEDITOR.instances['intro']; 
	if (instance) { CKEDITOR.remove(instance); }
	$('#intro').ckeditor(); 
	 
 	 $(".order_detail .contentTab>ul>li").click(function(){
 	 	 var tabid=$(this).attr("tabid");
 	 	
 	 	 $(".order_detail .contentTab>ul>li").removeClass("contentTabS");
 	 	 $(this).addClass("contentTabS");
 	 	 $(".tab-page .tab-panel").hide();
 	 	 $(".tab-panel[tabid="+tabid+"]").show();
 	 });
	 	 
	if($("#alert_null").val() == 'kong'){
		alert("已经是最后一条！");
	}
	OrderDetail.init(${orderId},${ord.status},${ord.pay_status},${ord.ship_status},${ord.isCod},${ord.payment_id});
	$("#previous").click( function(){
		$("#nextvalue").val('previous');
		$("#nextForm").attr("action","order!nextDetail.do?rand="+new Date().getTime());
		$("#nextForm").submit(); 
		});
	$("#next").click( function(){
		$("#nextvalue").val('next');
		$("#nextForm").attr("action","order!nextDetail.do?rand="+new Date().getTime());
		$("#nextForm").submit(); 
		});
});
 
</script>