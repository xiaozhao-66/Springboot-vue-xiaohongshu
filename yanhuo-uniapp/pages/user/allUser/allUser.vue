<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
				<view class="search">
					<input placeholder="输入内容" class="search-input" v-model="keyword" />
					<tui-button type="danger" @click="searchUser" height="60rpx " width="120rpx">搜索</tui-button>
				</view>
			</view>
		</tui-navigation-bar>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-list-view>
				<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">
					<view class="tui-item-box">
						<view class="tui-msg-box" @click="getUserInfo(item.uid)">
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill" :lazy-load='true' />
							<view class="tui-msg-item">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<p>用户id：{{ item.userId }}</p>
									<p>粉丝：{{ item.fanCount }}</p>
								</view>
							</view>
						</view>
						<view class="tui-msg-right" v-if="uid==loginUid">
							<view v-if="type == 1">
								<tui-button @click="clearFollow(item.uid, index)" height="60rpx" width="120rpx"
									type="gray" :size="24">取消关注</tui-button>
							</view>
							<view v-if="type == 0">
								<tui-button v-if='!item.isfollow' @click="follow(item.uid ,index)" height="60rpx"
									width="120rpx" type="danger" :size="24">关注</tui-button>
							</view>
						</view>
					</view>
				</tui-list-cell>
			</tui-list-view>
		</scroll-view>

		<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
		<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
	</view>
</template>

<script>
	import {
		searchUser
	} from '@/api/user.js'
	import {
		getAllFriend,
		clearFollow,
		followUser
	} from '@/api/follow.js'
	export default {
		data() {
			return {
				keyword: '',
				dataList: [],
				uid: '',
				type: 0,
				key: 0,
				T: false,
				loginUid: '',
				page: 1,
				limit: 10,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
			}
		},
		onLoad(option) {
			this.uid = option.uid
			this.type = option.type
			this.loginUid = uni.getStorageSync("userInfo").id

			this.getAllFriend()

		},
		created() {},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			searchUser() {
				let params = {
					keyword: this.keyword
				}

				searchUser(params).then(res => {
					this.dataList = res.data
				})
			},
			getUserInfo(uid) {
				if (uid == uni.getStorageSync("userInfo").id) {
					uni.switchTab({
						url: "/pages/user/user"
					})
				} else {
					uni.navigateTo({
						url: "/pages/otherUser/otherUser?uid=" + uid
					})
				}
			},
			getAllFriend() {
				let params = {
					uid: this.uid,
					type: this.type,
				}
				getAllFriend(this.page, this.limit, params).then(res => {

					this.dataList = res.data.records
					this.total = res.data.total
				})
			},


			loadData() {
				this.page = this.page + 1

				if (this.dataList.length >= this.total) {
					this.isEnd = true
					return
				}

				let params = {
					uid: this.uid,
					type: this.type,
				}
				getAllFanUser(this.page, this.limit, params).then(res => {
					this.dataList.push(...res.data.records)
				})
			},


			follow(fid, index) {
				let user = uni.getStorageSync("userInfo")
				let followDTo = {}
				followDTo.uid = user.id
				followDTo.fid = fid
				//添加关注
				followUser(followDTo).then(res => {
					this.getAllFriend(user.id, 0)
				})
			},

			clearFollow(fid, index) {

				let user = uni.getStorageSync("userInfo")
				let followDTo = {}
				followDTo.uid = user.id
				followDTo.fid = fid
				clearFollow(followDTo).then(res => {
					this.dataList.splice(index, 1)
				})
			}

		}
	}
</script>

<style scoped>
	@import url(./allUser.css)
</style>