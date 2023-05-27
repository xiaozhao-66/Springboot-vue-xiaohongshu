<template>
	<view class="container">
		<view class="title">登录</view>
		<view class="info">
			<view class="center">
				<input placeholder="输入手机号/邮箱/昵称" class="info-input" v-model="userInfo.username" />
				<input placeholder="输入密码" class="info-input" type="password" v-model="userInfo.password" />
			</view>
			<view class="info-bottom">
				<view @click="loginByCode">验证码登录</view>
				<view class="info_right">
					<view @click="findPassword">忘记密码？</view>
					<view @click="regist">注册</view>
				</view>
				
			</view>
			<view class="login">
				<button @click='login'>登录</button>
			</view>
		</view>
		<view class="fotter">
			<tui-divider width="80%">其他方式登录</tui-divider>
			<view class="other-login">
				<view class="other-item" >
					<view class="icon">
						<image src="/static/images/share/icon_qq.png" data-loginType="qq"  @click="auth"></image>
					</view>
					<view class="font">QQ</view>
				</view>

				<view class="other-item" >
					<view class="icon">
						<image src="/static/images/share/icon_wechat.png" data-loginType="weixin"  @click="auth"></image>
					</view>
					<view class="font">微信</view>
				</view>

				<view class="other-item" >
					<view class="icon">
						<image src="/static/images/share/icon_sina.png" @click="auth"></image>
					</view>
					<view class="font">微博</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
import { login,otherLogin} from "@/api/login.js"
import { tokenUtil } from "@/utils/token.js"
import socket from 'plus-websocket'
import { appConfig } from '@/config/config.js'
export default {
	data() {
		return {
			userInfo: {},
			
		}
	},
	
	methods: {
		login() {

			if (this.userInfo.username == null || this.userInfo.password == null) {
				uni.showToast({
					title: "信息输入有误"
				})

			} else {
				login(this.userInfo).then(res => {

					if (res.data.res == 0) {
						uni.showToast({
							title: res.data.message
						})
					} else {
						let user = res.data.userInfo
						tokenUtil.set(res.data.Jwt_token)
						console.log(res.data)
						uni.setStorageSync("userInfo", user)
						uni.setStorageSync("expiration", res.data.expiration)
						
						setTimeout(() => {
							uni.reLaunch({
								url: "/pages/index/index"
							});
						}, 500)
					}

				})
			}


		},
		regist() {
			uni.navigateTo({
				url: "/pages/regist/regist"
			})
		},
		loginByCode(){
			uni.navigateTo({
				url: "/pages/login/loginByCode"
			})
		},
		findPassword(){
			uni.navigateTo({
				url:"/pages/login/findPassword"
			})
		},
		
		
		auth(e){
			//一定要用that
			let that = this
			
			let loginType = e.currentTarget.dataset.logintype
			
			  uni.login({
			           provider: loginType,
			           success(res) {
						   console.log('登录数据',res)
			             // 登录成功后, 获取用户数据
			             uni.getUserInfo({
			               provider: loginType,
			               success(info) {
			                 var userInfo = info.userInfo
			                 var nickName = ""
			                 var avatarUrl = ""
			                 var openId = ""
			                 if (loginType == "weixin") {
			                   nickName = userInfo.nickName
			                   avatarUrl = userInfo.avatarUrl
			                   openId = userInfo.openId
			                 } else if (loginType == "qq") {
			                   nickName = userInfo.nickname
			                   avatarUrl = userInfo.figureurl_qq_2
			                   // qq返回了多个尺寸的头像, 按需选择
			                   openId = userInfo.openId
			                 }else if (loginType == "sinaweibo") {
			                   nickName = userInfo.nickname
			                   avatarUrl = userInfo.avatar_large
			                   openId = userInfo.id
			                 }  
							 
							 let userOtherLoginRelationDTO = {}
							 userOtherLoginRelationDTO.otherUserId = openId
							 userOtherLoginRelationDTO.otherUsername = nickName
							 userOtherLoginRelationDTO.otherAvatar = avatarUrl
							 userOtherLoginRelationDTO.otherToken = res.access_token
							 
							otherLogin(userOtherLoginRelationDTO).then(res=>{	 
								console.log(res)
								let user = res.data.userInfo
								tokenUtil.set(res.data.Jwt_token)
								uni.setStorageSync("userInfo", user)
								
								
								setTimeout(() => {
									uni.reLaunch({
										url: "/pages/index/index"
									});
								}, 500)
							
						})
							 
				   }
				 })
			   }
			 })
		},
		
		
	}
}
</script>

<style scoped>
.container {
	padding: 100rpx 100rpx;
	height: 100vh;
}

.title {
	text-align: center;
	padding-top: 40px;
	font-size: 30px;
}

.info {
	margin-top: 280rpx;

}

.info-input {
	margin-top: 10px;
	height: 60rpx;
	border-bottom: 1px solid #f4f4f4;
}

.info-bottom {
	margin-top: 10px;
	display: flex;
	justify-content: space-between;
	color: #7a7a7a;
	font-size: 24rpx;
}

.info_right{
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.login {
	margin-top: 80rpx;
}

.fotter {
	margin-top: 300rpx;
}

button {
	color: #fff;
	background-color: coral;
	border-radius: 20px;
}

image {
	width: 60rpx;
	height: 60rpx;
}

.other-login {
	display: flex;
	justify-content: space-between;
	padding: 0 20rpx;
}

.other-item {
	text-align: center;
}</style>
