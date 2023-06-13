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
				<image :src="userInfo.cover" v-if="userInfo" mode="aspectFill" />
				<image src="/static/images/toast/img_nodata.png" v-else mode="aspectFill" />
			</view>

			<!-- 主体 -->
			<view class="main">
				<view class="top">
					<view class="user">
						<view class="user-left">
							<image :src="userInfo.avatar" class="avatar" mode="aspectFill" />
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
	import {
		getUserInfo
	} from "@/api/user.js"

	import {
		isFollow,
		followUser,
		clearFollow
	} from "@/api/follow.js"

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
					url: "/pages/user/allUser/allUser?type=" + type + "&uid=" + this.uid
				})
			}
		}
	}
</script>

<style scoped>
	@import url(./otherUser.css);
</style>