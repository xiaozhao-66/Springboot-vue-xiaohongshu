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

<script>
	import {
		searchUser
	} from '@/api/user.js'
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

		}
	}
</script>

<style scoped>
	@import url(./addfriend.css);
</style>