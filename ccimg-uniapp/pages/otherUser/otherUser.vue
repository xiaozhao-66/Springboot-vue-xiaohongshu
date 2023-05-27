<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="top">
				<view @click="back">
					<tui-icon name="arrowleft" color="#fff" size="30"></tui-icon>
				</view>
			</view>

			<!-- 显示图片 -->
			<view class="image">
				<image :src="userInfo.cover" v-if="userInfo"  mode="aspectFill"/>
				<image src="/static/images/toast/img_nodata.png" v-else mode="aspectFill" />
			</view>

			<!-- 主体 -->
			<view class="main">
				<view class="top">
					<view class="user">
						<view class="user-left">
							<image :src="userInfo.avatar" class="avatar"  mode="aspectFill"/>
							<view class="user-content">
								<h3>{{ userInfo.username }}</h3>
								<view class="user-id f">id:{{ userInfo.userId }}</view>
								<view class="descrpition f">{{ userInfo.description }} </view>
							</view>

						</view>
						<view class="user-right">
							<view @click="chat(userInfo.id)" class="user-message">
								<tui-icon name="message" size="24" color="#797979"></tui-icon>
							</view>
							
							
							<tui-button v-if="T" @click="clearFollow(userInfo.id)" type="gray" height="66rpx" size="24"
								width="120rpx">已关注</tui-button>
									
								
							<tui-button v-else @click="follow(userInfo.id)" type="danger" height="66rpx" size="24"
								width="120rpx">关注</tui-button>
						

						</view>
					</view>
					<view class="info">
						<p @click="getAllFriend(1)">关注 {{ userInfo.followCount }}</p>
						<p @click="getAllFriend(0)">粉丝 {{ userInfo.fanCount }}</p>
						<p>动态 {{ userInfo.trendCount }}</p>
					</view>
					<view class="">
						<tui-tabs :tabs="tabs" :currentTab="currentTab" @change="change" sliderBgColor="red"
							selectedColor="red" itemWidth="50%"></tui-tabs>
					</view>
				</view>


				<view class="zhuti">
					<view v-if="userInfo">
						<Trend v-if="currentTab == 0" :uid='uid'> </Trend>
						<Album v-if="currentTab == 1" :uid='uid'></Album>
						<Collection v-if="currentTab == 2" :uid='uid'></Collection>
					</view>
					<view class="nologin" v-else>
						<button @click="login">请先登录</button>
					</view>
				</view>
			</view>
		</tui-navigation-bar>
	</view>
</template>

<script>
import Trend from "@/pages/user/trends/trends"
import Album from "@/pages/user/albums/albums"
import Collection from "@/pages/user/collections/collections"
import { getUserInfo } from "@/api/user.js"

import { isFollow, followUser, clearFollow} from "@/api/follow.js"

export default {
	components: {
		Trend,
		Album,
		Collection
	},
	data() {
		return {
			current: 0,
			currentTab: 0,
			tabs: [{
				name: "动态"
			}, {
				name: "专辑"
			}, {
				name: "收藏"
			}],
			userInfo: {},
			seed: 0,
			uid: '',
			T: false,
		}
	},

	onLoad(option) {

		if (typeof option.currentTab != 'undefined' || option.currentTab != null) {
			this.currentTab = option.currentTab
		}
		this.uid = option.uid
	},

	onShow() {

		this.getUserInfo(this.uid)
		this.isFollow()
		this.seed = Math.random()
	},
	methods: {
		back() {
			uni.navigateBack({
				delta: 1
			})
		},
		change(e) {
			this.currentTab = e.index
		},
		login() {
			uni.navigateTo({
				url: "/pages/login/login"
			})
		},
		getUserInfo(uid) {
			let params = {
				uid: uid
			}
			getUserInfo(params).then(res => {

				this.userInfo = res.data
			})
		},
		chat(uid) {
			uni.navigateTo({
				url: "/pages/chat/chat2?toUid=" + this.uid
			})
		},

		isFollow() {
			let uid = uni.getStorageSync("userInfo").id
			let fid = this.uid
			let params = {
				uid: uid,
				fid: fid
			}
			isFollow(params).then(res => {
				if (res.data) {
					this.T = true
				} else {
					this.T = false
				}
			})
		},

		//关注用户
		follow(fid) {
			let curUser = uni.getStorageSync("userInfo")
			let followDTo = {}
			followDTo.uid = curUser.id
			followDTo.fid = fid
			//添加关注
			followUser(followDTo).then(res => {
				this.T = true;
			})
		},
		
		clearFollow(fid) {
		
			let user = uni.getStorageSync("userInfo")
			let followDTo = {}
			followDTo.uid = user.id
			followDTo.fid = fid
			clearFollow(followDTo).then(res => {
				this.T = false;
			})
		},
		
		getAllFriend(type) {
			uni.navigateTo({
				url: "/pages/user/allUser/allUser?type=" + type+"&uid="+this.uid
			})
		}
	}
}
</script>

<style scoped>
.content {
	position: relative;
	width: 100%;
	background-color: #f4f4f4;
}

.top {
	position: relative;
	height: 60rpx;
	z-index: 9999;
}

.top .right {
	padding-top: 5px;
	display: flex;
	justify-content: end;
}

.top .right .item {
	margin-right: 15rpx;
}

.content .image {
	position: relative;
	top: -200rpx;
}

.content .image image {
	width: 100%;
}

.content .main {
	position: relative;
	top: -200rpx;
	width: 100%;
	z-index: 22;
}

.content .main .top {
	width: 100%;
	height: 340rpx;
	display: block;
	background-color: #fff;
	border-radius: 10px;
}

.content .main .top .user {
	width: 100%;
	display: flex;
	justify-content: space-between;
	align-items: center;
	height: 200rpx;

}

.content .main .top .user .user-left {
	width: 500rpx;
}

.content .main .top .user .user-left image {
	position: absolute;
	left: 60rpx;
	top: -50rpx;
	width: 120rpx;
	height: 120rpx;
	border-radius: 50%
}

.user-content {
	margin-top: 80rpx;
	margin-left: 60rpx;

}

.user-id {
	margin-top: 5rpx;
	margin-bottom: 5rpx;
	color: #5c5c5c;
}

.f{
	font-size: 28rpx;
}

.content .main .top .user .user-right {
	display: flex;
	justify-content: space-between;
	align-items: center;
	height: 80rpx;
	margin-right: 20rpx;
	/* width: 200rpx; */
}

.user-message{
	 width: 80rpx;
	 height: 60rpx;
	 text-align: center;
	 background-color: #f0f0f0;
	 line-height: 60rpx;
	 border-radius: 14px;
	 margin-right: 5px;
}

.content .main .top .user .user-right button {
	height: 60rpx;
	width: 150rpx;
	background-color: #ff0000;
	font-size: 16px;
	color: #fff;
	line-height: 60rpx;
	border-radius: 10px;
}

.content .main .top .info {
	display: flex;
	height: 100rpx;
	margin-left: 80rpx;
	margin-right: 80rpx;
	justify-content: space-between;
	align-items: center;
	font-size: 28rpx;
}

.nologin {
	padding-top: 120rpx;
}

.nologin button {

	width: 300rpx;
	height: 80rpx;
	color: #fff;
	background-color: #ff0000;
	border-radius: 20px;
	line-height: 80rpx;
}</style>
