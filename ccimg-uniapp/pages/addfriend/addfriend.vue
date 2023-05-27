<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">

				<view @click="back">
					<tui-icon name="arrowleft" size="25"></tui-icon>
				</view>
				<view class="search">
					<input placeholder="输入内容" class="search-input" v-model="keyword" />
					<tui-button type="danger" @click="searchUser" height="60rpx " width="120rpx">搜索</tui-button>
				</view>
			</view>
		</tui-navigation-bar>


		<tui-list-view>
			<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">
				<view class="tui-item-box">
					<view class="tui-msg-box" @click="getUserInfo(item.id)">
						<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill"></image>
						<view class="tui-msg-item">
							<view class="tui-msg-name">{{ item.username }}</view>
							<view class="tui-msg-type">
								<p>用户id：{{ item.userId }}</p>
								<p>粉丝：{{ item.fanCount }}</p>
							</view>
						</view>
					</view>
					<view class="tui-msg-right">

						<!-- <view v-if="T">
									<tui-tag type="gray">已关注</tui-tag>
								</view>
								<view v-else>
									<button @click="follow(imgInfo.userId)">关注</button>
						</view> -->
					</view>
				</view>
			</tui-list-cell>
		</tui-list-view>
	</view>
</template>

<script >
import { searchUser } from '@/api/user.js'
export default {
	data() {
		return {
			keyword: '',
			dataList: [],
			uid: '',
		}
	},
	created() {
		this.uid = uni.getStorageSync("userInfo").id
	},
	methods: {
		back(){
			
			uni.navigateBack({
				delta:1
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

	}
}
</script>

<style scoped>
a {
	text-decoration: none;
	color: black;
}

.container {
	padding-bottom: env(safe-area-inset-bottom);
}

.nav {
	display: flex;
	align-items: center;
	height: 80rpx;
}

.search {
	display: flex;
}

.search-input {

	width: 500rpx;
	height: 60rpx;
	background-color: #f1f1f1;
	padding-left: 10px;
}

.search button {

	width: 120rpx;
	height: 60rpx;
	background-color: #ec2124;
	font-size: 16px;
	color: #fff;
	line-height: 60rpx;
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
	min-height: 100rpx;
	overflow: hidden;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.tui-msg-right button {
	background: #fff;
	border: 1px solid #ff0000;
	color: #ff0000;
	font-size: 20rpx;
	width: 120rpx;
	height: 60rpx;
}

.tui-msg-type {
	max-width: 280rpx;
	min-height: 60rpx;
	overflow: hidden;
	display: flex;
	flex-direction: column;

	justify-content: space-between;
	font-size: 25rpx;
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
	max-width: 120rpx;
	height: 88rpx;
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

.tui-msg-time {
	width: 100rpx;
	margin-left: 3px;
	font-size: 24rpx;
	line-height: 24rpx;
	color: #9397a4;
}
</style>
