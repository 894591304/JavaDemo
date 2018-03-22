/************************关注***************************/
$(function(){
	$(document).on('click','.PlusPeople',function(){
		var open = $(this).find('.fa-plus-circle').length;
		var fid = $(this).attr("fid");
		$.ajax({
				url:"/javamall/shop/ext/follow!follow.do",
				type:"POST",
				data:"followId="+fid,
				dataType:"json",
				success:function(result){
					if(result.result==1){	
					}else if(result.result==0){
					}else if(result.result==2){
						alert("不能关注自己！");
					}else{
						alert("请登录后再关注达人！");
						window.location.href=ctx+"/login.html";
					}
				},
				error:function(e){
					alert("出现错误，请重试");
				}
		})
		$(this).empty().html('<img src="/javamall/themes/default/img/13.png" alt="" >').addClass('gzPeople').removeClass('PlusPeople');
	});	
	$(document).on('click','.gzPeople',function(){
		var open = $(this).find('.fa-plus-circle').length;
		var fid = $(this).attr("fid");
		$.ajax({
				url:"/javamall/shop/ext/follow!cancelFollow.do",
				type:"POST",
				data:"followId="+fid,
				dataType:"json",
				success:function(result){
					if(result.result==1){
					}else if(result.result==0){
					}else if(result.result==2){
					}else{
						alert("请登录后再关注达人！");
						window.location.href=ctx+"/login.html";
					}
				},
				error:function(e){
					alert("出现错误，请重试");
				}
		})
		$(this).empty().html('<img src="/javamall/themes/default/img/12.png" alt="" class="fa-plus-circle">').addClass('PlusPeople').removeClass('gzPeople');
	});	
});