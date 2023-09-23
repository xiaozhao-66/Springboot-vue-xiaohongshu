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

				<view class="title">修改密码</view>
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
	import {
		loginOut
	} from "@/api/login.js"
	import {
		tokenUtil
	} from "@/utils/token.js"
	export default {
		data() {
			return {
				currentUser: null
			}
		},
		onShow() {
			this.currentUser = uni.getStorageSync("userInfo");
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			loginOut() {
				if (this.goEasy.getConnectionStatus() === 'disconnected') {
					return
				}
				this.disconnect()
				loginOut(this.currentUser).then(res => {
					uni.removeStorageSync("userInfo")
					tokenUtil.clear()

					setTimeout(() => {
						uni.navigateTo({
							url: "/pages/login/login?close=" + true
						})
					}, 500)
				})

			},

           disconnect(){
			   this.goEasy.disconnect({
			   	onSuccess: function() {
			   		uni.hideLoading();
			   		console.log('注销成功')
			   		getApp().globalData.currentUser = null;
			   		uni.navigateTo({
			   			url: './login'
			   		})
			   	},
			   	onFailed: function(error) {
			   		uni.hideLoading();
			   		uni.showToast({
			   			icon: 'none',
			   			title: '注销超时，请检查网络！（务必确保注销成功才允许客户退出应用，否则有可能会收到上个用户的消息。）',
			   			duration: 6000
			   		});
			   		console.log('注销失败', error);
			   	}
			   });
		   },

			updatePassword() {
				uni.navigateTo({
					url: "/pages/setting/updatePossword/updatePossword"
				})
			}
		}
	}
</script>

<style scoped>
	@import url(./setting.css);
</style>