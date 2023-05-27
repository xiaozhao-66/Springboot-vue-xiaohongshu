<script>

	export default {
		
		onLaunch: function() {
				   uni.onTabBarMidButtonTap(()=>{
				   		
				           // 这里可以根据 个人需求 做点击处理，
				           // 本人需进行页面跳转。
						   if(uni.getStorageSync('userInfo')==''){
							   uni.showToast({
							   	   title:"用户未登录",
								   icon:'none'
							   })
						   }else{
							  uni.chooseImage({
							      count: 9, // 最多可以选择的图片张数，默认9
							      sizeType: ['original'], //original 原图，compressed 压缩图，默认二者都有
							      sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
							      success: function(res) {	  		
							  		 uni.setStorageSync("imgList",JSON.stringify(res.tempFilePaths))
							  		 
							  		  uni.navigateTo({
							  			url:"/pages/push/push"
							  		  })
							  	}
							  }) 
						   }			   				
				   		
				   	})
				   		
		},
		
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},	
	}

</script>

<style>

</style>
