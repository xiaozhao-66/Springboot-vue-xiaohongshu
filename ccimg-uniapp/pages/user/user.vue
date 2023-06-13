<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="top">

				<view class="right">
					<view class="item">
						<tui-icon name="manage" class="icon-item-manage" color="#fff" size="30"></tui-icon>
					</view>
					<view class="item">
						<navigator url="/pages/setting/setting">
							<tui-icon name="setup" class="icon-item-setup" color="#fff" size="30"></tui-icon>
						</navigator>
					</view>
				</view>
			</view>

			<!-- 头 -->

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
							<tui-button type="danger" shape="circle" @click="editUserInfo" height="60rpx" width="140rpx"
								:size="28">编辑</tui-button>
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
						<Trend v-if="currentTab == 0" :uid='uid' @cancelUp='cancelUp'> </Trend>
						<Album v-if="currentTab == 1" :seed='seed' :uid='uid'></Album>
						<Collection v-if="currentTab == 2" :uid='uid'></Collection>
					</view>
					<view class="nologin" v-else>
						<button @click="login">请先登录</button>
					</view>
				</view>
			</view>

			<tui-modal :show="show" @click="confirm" @cancel="hide" content="取消上传" :button="radio" width="50%"
				padding="15rpx 40rpx" :fadeIn='true'></tui-modal>
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
		deleteImgs
	} from '@/api/imgDetail.js'
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
				show: false,

				radio: [{
						text: '取消',
						type: 'white',
					},
					{
						text: '确定',
						type: 'red',
					}
				],

				mid: '',

			}
		},
		onLoad(options) {
			if (typeof options.currentTab != 'undefined' || options.currentTab != null) {
				this.currentTab = options.currentTab
			}

		},

		created() {

		},
		onShow() {
			this.getUser()
			this.uid = uni.getStorageSync("userInfo").id
			this.seed = Math.random()
		},
		methods: {
			change(e) {
				this.currentTab = e.index
			},
			setting() {
				uni.navigateTo({
					url: "/pages/setting/setting"
				})
			},

			cancelUp(mid) {

				this.show = true
				this.mid = mid
			},

			confirm(e) {
				let index = e.index;
				if (e.index == 0) {
					this.show = false
				} else {
					let arr = []
					arr.push(this.mid)
					deleteImgs(arr, this.userInfo.id).then(res => {
						uni.showToast({
							title: "取消成功"
						})
						this.show = false
					})
				}
			},
			hide() {
				this.show = false
			},

			editUserInfo() {
				uni.navigateTo({
					url: "/pages/user/info/info?uid= " + this.uid
				})
			},
			login() {
				uni.navigateTo({
					url: "/pages/login/login"
				})
			},
			getUser() {

				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getUserInfo(params).then(res => {

					this.userInfo = res.data
				})

			},

			getAllFriend(type) {
				uni.navigateTo({
					url: "/pages/user/allUser/allUser?type=" + type + "&uid=" + this.userInfo.id
				})
			}
		}
	}
</script>

<style scoped>
	@import url(./user.css);
</style>