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
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill" :lazy-load='true'   />
							<view class="tui-msg-item">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view class="tui-msg-type-item">开始关注您了</view>
									<view class="tui-msg-time">{{ item.time }}</view>
								</view>
							</view>
						</view>
						<view class="tui-msg-right">

							<tui-button v-if="item.isfollow" @click="clearFollow(item.uid)" type="gray" height="50rpx" size="22" width="100rpx" shape="circle" plain>已关注</tui-button>
							
							<tui-button  v-else @click="follow(item.uid)" type="danger" height="50rpx" size="22" width="100rpx" shape="circle" plain>关注</tui-button>
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

import { getAllFanUser, followUser,clearFollow} from "@/api/follow.js"

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
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAllFanUser() {
			let params = {
				uid: this.uid
			}
			getAllFanUser(this.page, this.limit, params).then(res => {
				this.dataList = res.data.records;
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
				uid: this.uid
			}
			getAllFanUser(this.page, this.limit, params).then(res => {
				this.dataList.push(...res.data.records)
			})
		},

		//关注用户
		follow(fid) {

			let followDTo = {}
			followDTo.uid = this.uid
			followDTo.fid = fid
			//添加关注
			followUser(followDTo).then(res => {
				this.getAllFanUser()
			})
		},
		
		clearFollow(fid) {
		
			let user = uni.getStorageSync("userInfo")
			let followDTo = {}
			followDTo.uid = user.id
			followDTo.fid = fid
			clearFollow(followDTo).then(res => {
				this.getAllFanUser()
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
.container {
	padding-bottom: env(safe-area-inset-bottom);
}

.nav {
	display: flex;
	align-items: center;
}

.page {
	height: 85vh;
}

.tui-item-box {
	width: 100%;
	display: flex;
	align-items: center;
}

.tui-msg-box {
	display: flex;
	align-items: center;
}

.tui-msg-pic {
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%;
	display: block;
	margin-right: 24rpx;
	flex-shrink: 0;
}

.tui-msg-img {
	width: 120rpx;
	height: 160rpx;
	border-radius: 5px;
	display: block;
	margin-right: 24rpx;
	flex-shrink: 0;
}

.tui-msg-item {
	max-width: 500rpx;
	min-height: 80rpx;
	overflow: hidden;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

button {
	background: #fff;
	border: 1px solid #ff0000;
	color: #ff0000;
	font-size: 20rpx;
	width: 120rpx;
	height: 60rpx;
}

.tui-msg-type {
	display: flex;
	align-items: center;
	justify-content: flex-start;
	font-size: 25rpx;
}

.tui-msg-type-item{
	width: 160rpx;
}

.tui-msg-time {
	width: 140rpx;
	margin-left: 3px;
	font-size: 24rpx;
	line-height: 24rpx;
	color: #9397a4;
}

.tui-msg-name {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	font-size: 30rpx;
	line-height: 1;
	color: #262b3a;
}

.tui-msg-content {
	max-width: 380rpx;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	font-size: 25rpx;
	line-height: 2;
	color: #9397a4;
}

.tui-msg-right {
	max-width: 150rpx;
	margin-left: auto;
	text-align: right;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	align-items: flex-end;
}

.tui-right-dot {
	height: 76rpx !important;
	padding-bottom: 10rpx !important;

}

.

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}</style>
