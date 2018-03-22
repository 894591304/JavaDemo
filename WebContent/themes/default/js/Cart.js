var Cart={
		init:function(staticserver){
			var self=this;			
			this.bindEvent();
		},
		bindEvent:function(){
			var self=this;
			
			//购物数量调整
			$(".numAddIconBox .numAddIconBoxIcon,.numAddIconBox .numAbateIconBoxIcon").click(function(){
				$this = $(this);
				var number = $this.parents(".numAdd");
				var itemid =number.attr("itemid");
				var productid =number.attr("productid");
				var objipt = number.find("input");
				var proxyid = number.attr("proxyid");
				var num=objipt.val();
				num =parseInt(num);
				if (!isNaN(num)){
					if($this.hasClass("numAddIconBoxIcon")){
						num++;
					}
					if($this.hasClass("numAbateIconBoxIcon")){
						 if(num == 1 ){
							 if(confirm("确定要删除该商品?")){
								 self.deleteGoodsItem(itemid);
							 }
							 return false;
						} 
						num--;
					}
					 num = (num <=1 || num > 100000) ? 1 : num;
					 self.updateNum(itemid, num, productid,objipt,proxyid);
				}
			});
			
			//购物数量手工输入
            $(".numAdd input").keydown(function(e){
                var kCode = event.which;
                //判断键值  
                if (((kCode > 47) && (kCode < 58)) 
                    || ((kCode > 95) && (kCode < 106)) 
                    || (kCode == 8) || (kCode == 39) 
                    || (kCode == 37)) { 
                    return true;
                } else{ 
                    return false;  
                }
            }).focus(function() {
                this.style.imeMode='disabled';// 禁用输入法,禁止输入中文字符
            }).keyup(function(){
                var pBuy   = $(this).parent().parent();//获取父节点
                var itemid  = pBuy.attr("itemid");
                var productid  = pBuy.attr("productid");
                var numObj = pBuy.find("input[name='num']");//获取当前商品数量
                var num    = parseInt(numObj.val());
				var proxyid = pBuy.attr("proxyid");
                if (!isNaN(num)) {
                    var numObj = $(this);
                    var num    = parseInt(numObj.val());
                    num = (num <=1 || num > 100000) ? 1 : num;
                    self.updateNum(itemid, num, productid,numObj,proxyid);
                }
            });
            
			//删除商品
			$("#UpinBt .delete").click(function(){
				var cartid = $(this).parents("li").attr("itemid");
				if(confirm("您确实要把该商品移出购物车吗？") ){
					self.deleteGoodsItem(cartid);
				}
			});
			
			//清空购物车
			$("#UpinBt .clean_btn").click(function(){
				if(confirm("您确认要清空购物车吗？") ){
					self.clean();
				}
			});
			
			//继续购物
			$("#UpinBt .returnbuy_btn").click(function(){
				location.href="index.html";
			});
			
			//去结算
			$("#UpinBt .checkout_btn").click(function(){
				if(isLogin){
					location.href="order.html";
				}else{
					self.showLoginWarnDlg();					
				}
			});
		},
		
		//提示登录信息
		showLoginWarnDlg:function(btnx,btny){
			var html = $("#login_tip").html();
			$.dialog({ title:'提示信息',content:html,lock:true,init:function(){
				
				$(".ui_content .btn").jbtn();
				$(".ui_content .to_login_btn").click(function(){
					 location.href="login.html?forward=checkout.html";
				});

				$(".ui_content .to_checkout_btn").click(function(){
					location.href="checkout.html";
				});
				
			}});
		},
		
		//删除一个购物项
		deleteGoodsItem:function(itemid){
			var self=this;
			//$.Loading.show("请稍候...");
			$.ajax({
				url:"api/shop/cart!delete.do?ajax=yes",
				data:"cartid="+itemid,
				dataType:"json",
				success:function(result){
					if(result.result==1){
						self.refreshTotal();
						self.removeItem(itemid);
					}else{
						$.alert(result.message);
					}	
					//$.Loading.hide();
					window.location.reload(); 
				},
				error:function(){
					$.Loading.hide();
					$.alert("出错了:(");
				}
			});
		},
		
		//移除商品项
		removeItem:function(itemid){
			$("#cart_wrapper tr[itemid="+itemid+"]").remove();
		},
		
		//清空购物车
		clean:function(){
			//$.Loading.show("请稍候...");
			var self=this;
			$.ajax({
				url:"/api/shop/cart!clean.do?ajax=yes",
				dataType:"json",
				success:function(result){
					$.Loading.hide();
					if(result.result==1){
						location.href='cart.html';
					}else{
						$.alert("清空失败:"+result.message);
					}				 
				},
				error:function(){
					$.Loading.hide();
					$.alert("出错了:(");
				}
			});		
		},
		

		//更新数量
		updateNum:function(itemid,num,productid,num_input,proxyid){
			var self = this;
			$.ajax({
				url:ctx+"/shop/ext/proxy!checkStore.do",
				data:"proxyid="+proxyid,
				dataType:"json",
				success:function(result){
					if(result.store>=num){
						$.ajax({
							url:"api/shop/cart!updateNum.do?ajax=yes",
							data:"cartid="+itemid +"&num="+num +"&productid="+productid,
							dataType:"json",
							success:function(result){
								if(result.result==1){
									self.refreshTotal();
									var price = $("li[itemid="+itemid+"]").attr("price").replace(/,/g,"");
									//price =price* num;
									price =self.changeTwoDecimal_f(price* num);
									$("li[itemid="+itemid+"] .itemTotal").html("￥"+price);
									num_input.val(num);								
								}else{
									alert("更新失败");
								}
							},
							error:function(){
								alert("出错了:(");
							}
						});
					}else{
						num_input.val(result.store);
						alert("抱歉！您所选择的货品库存不足。");
						return false;
					}
				},
				error:function(){
					alert("出错了:(");
				}
			});
		},		
		
		//刷新价格
		refreshTotal:function(){
			var self = this;
			$.ajax({
				url:"cart/cartTotal.html",
				dataType:"html",
				success:function(html){
					 $(".UpinAll").html(html);
				},
				error:function(){
				}
			});
		},
		
		changeTwoDecimal_f:function(x) {
	        var f_x = parseFloat(x);
	        if (isNaN(f_x)) {
	            alert('参数为非数字，无法转换！');
	            return false;
	        }
	        var f_x = Math.round(x * 100) / 100;
	        var s_x = f_x.toString();
	        var pos_decimal = s_x.indexOf('.');
	        if (pos_decimal < 0) {
	            pos_decimal = s_x.length;
	            s_x += '.';
	        }
	        while (s_x.length <= pos_decimal + 2) {
	            s_x += '0';
	        }
	        return s_x;
	    }
};

$(function(){
	Cart.init();
});