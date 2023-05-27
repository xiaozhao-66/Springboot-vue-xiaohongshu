<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
				<h3>设置</h3>
			</view>
		</tui-navigation-bar>

		<view class="update_pwd">
			<view class="info-item-zhuti" @click="updatePassword">
				
				<view class="title" >修改密码</view>
				
			
				<!-- <view class="cont">xiaozhao</view> -->
				<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
			</view>
		</view>

		<view class="info">
			<ul>
				<li class="info-item">
					<view class="info-item-zhuti">
						<view class="title">软件设置</view>

						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>

				</li>
				<li class="info-item">
					<view class="info-item-zhuti">
						<view class="title">绑定手机号</view>

						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti">
						<view class="title">绑定邮箱</view>

						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti">
						<view class="title">项目地址</view>

						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti">
						<view class="title">项目简介</view>

						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>
				</li>
			</ul>
		</view>

		<view class="fotter">
			<view class="f-btn">
				<button @click="loginOut">退出登录</button>
			</view>

		</view>
	</view>
</template>

<script>
import { loginOut } from "@/api/login.js"
export default {
	components: {

	},
	data() {
		return {

		}
	},
	onLoad(options) {

	},
	onShow(options) {

	},
	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		loginOut() {

			let user = uni.getStorageSync("userInfo")

			loginOut(user).then(res => {

				uni.removeStorageSync("userInfo")
                
				setTimeout(() => {
					uni.navigateTo({
						url:"/pages/login/login?close="+true
					})
				}, 500)
				
			

			})

		},
		
		updatePassword(){
			
			uni.navigateTo({
				url:"/pages/setting/updatePossword/updatePossword"
			})
		}
	}
}
</script>

<style scoped>
ul {
	padding-left: 0rpx;
}

li {
	list-style: none;
}

a {
	text-decoration: none;
	color: black;
}

.content {

	height: 100vh;
	background-color: #f4f4f4;

}

.content .nav {
	display: flex;
	align-items: center;
	background-color: #fff;
	height: 80rpx;
}

.content .nav a {
	margin-left: 10px;
	display: inline-block;
	font-size: 20px;
}

.update_pwd {
	margin-top: 20px;
	padding: 0 20px;
	background-color: #fff;
}

.info {
	background-color: #fff;
	margin-top: 20px;
}



.info-item {
	padding: 0 20px;
}

.info-item-zhuti {
	height: 100rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1px #f4f4f4 solid;
}

.title {
	color: #707070;
}

.fotter {
	width: 100%;
	position: fixed;
	left: 0px;
	bottom: 0px;
}

.f-btn {

	margin: auto;
	text-align: center;
}

button {
	width: 500rpx;
	color: #fff;
	background-color: coral;
	border-radius: 20px;
}</style>
