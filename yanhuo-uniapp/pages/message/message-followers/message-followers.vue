<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
				<view>新增关注</view>
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
									<view class="tui-msg-type-item">开始关注您了</view>
									<view class="tui-msg-time">{{ item.time }}</view>
								</view>
							</view>
						</view>
						<view class="tui-msg-right">

							<tui-button v-if="item.isfollow" @click="clearFollow(item.uid,index)" type="gray" height="50rpx"
								size="22" width="100rpx" shape="circle" plain>已关注</tui-button>

							<tui-button v-else @click="follow(item.uid,index)" type="danger" height="50rpx" size="22"
								width="100rpx" shape="circle" plain>关注</tui-button>
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
		getAllFriend,
		followUser,
		clearFollow
	} from "@/api/follow.js"
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				dataList: [],
				uid: '',
				T: false,
			}
		},
		created() {
			this.uid = uni.getStorageSync("userInfo").id
			this.getAllFanUser()
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			getAllFanUser() {
				let params = {
					uid: this.uid,
					type: 0
				}
				getAllFriend(this.page, this.limit, params).then(res => {
					res.data.records.forEach((e) => {
						e.time = timeAgo(e.time)
						this.dataList.push(e)
					})

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
					type: 0
				}
				getAllFanUser(this.page, this.limit, params).then(res => {
					res.data.records.forEach((e) => {
						e.time = timeAgo(e.time)
						this.dataList.push(e)
					})
				})
			},

			//关注用户
			follow(fid ,index) {

				let followDTo = {}
				followDTo.uid = this.uid
				followDTo.fid = fid
				//添加关注
				followUser(followDTo).then(res => {
					this.dataList[index].isfollow = true
				})
			},

			clearFollow(fid,index) {

				let user = uni.getStorageSync("userInfo")
				let followDTo = {}
				followDTo.uid = user.id
				followDTo.fid = fid
				clearFollow(followDTo).then(res => {
					this.dataList[index].isfollow = false
				})
			},

			getUserInfo(uid) {
				uni.navigateTo({
					url: "/pages/otherUser/otherUser?uid=" + uid
				})
			},

		}
	}
</script>

<style scoped>
	@import url(./message-followers.css);
</style>