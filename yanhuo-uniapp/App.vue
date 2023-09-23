<script>
import {
		tokenUtil
	} from "@/utils/token.js"
	export default {
		
		onLaunch: function() {
			       
				   let token = tokenUtil.get()
				   console.log("toekn",token)
				   		       
				   if(token){
					   plus.navigator.closeSplashscreen();
					   
					   
					   if(typeof uni.getStorageSync("imgPublish")=='undefined'||uni.getStorageSync("imgPublish")==null){
						   uni.showToast({
							title: "有图片在上传中",
							icon:'none'
						   })
						   return
					   }
					   
					   
					   uni.onTabBarMidButtonTap(()=>{
					   		
					           // 这里可以根据 个人需求 做点击处理，
					           // 本人需进行页面跳转。
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
					   	})
				   }else{
					   uni.reLaunch({
							url: "/pages/login/login",
							success: () => {
								// #ifdef APP-PLUS
								plus.navigator.closeSplashscreen();
								// #endif
							}
						})

				   }		
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
