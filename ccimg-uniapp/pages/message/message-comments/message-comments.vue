<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
				<view>评论和@</view>
				
			</view>
		</tui-navigation-bar>
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-list-view>
				<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">
					<view class="tui-item-box" >
						<view class="tui-msg-box">
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill" :lazy-load='true' @click="getUserInfo(item.uid)"/>
							<view class="tui-msg-item" @click="getComment(item.mid, item.id, item.replyId)">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view v-if="item.replyUid!=uid" class="tui-msg-type-item">评论您的笔记</view>
									<view v-else class="tui-msg-type-item">回复您的评论</view>
									<view class="tui-msg-time">{{ item.time }}</view>
								</view>
								<view class="tui-msg-content">{{ item.replyContent }}</view>
								<view class="tui-msg-content " v-if="item.replyId != 0">
									<view  class="reply" style="display: flex;">
										 <view v-if="item.replyUid!=uid">{{item.replyName}}:</view>
										 <view>{{ item.content }}</view>
									</view>
								</view>
							</view>

						</view>
						<view class="tui-msg-right">
							<image :src="item.cover" class="tui-msg-img" mode="aspectFit" :lazy-load='true' />
						</view>
					</view>
				</tui-list-cell>
			</tui-list-view>

			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
	</view>
</template>

<script>
import { getAllReplyComment } from "@/api/comment.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
export default {
	data() {
		return {
			page: 1,
			limit: 7,
			total: 0,
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			dataList: [],
			uid: '',
		}
	},
	created() {
		this.uid = uni.getStorageSync("userInfo").id
		this.getAllReplyComment()
	},
	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAllReplyComment() {
			let params = {
				uid: this.uid
			}
			getAllReplyComment(this.page, this.limit, params).then(res => {
				console.log(res.data)
				this.dataList = res.data.records;
				this.total = res.data.total
			})
		},

		getComment(mid, cid, rid) {
			let data = {}
			data.uid = this.uid
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid + "&cid=" + cid + "&rid=" + rid
				})
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
			getAllReplyComment(this.page, this.limit, params).then(res => {
				this.dataList.push(...res.data.records)

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
}

.tui-msg-box {
	display: flex;
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
	height: 100rpx;
	border-radius: 5px;
	display: block;
	margin-right: 24rpx;
	flex-shrink: 0;
}

.tui-msg-item {
	width: 400rpx;
	min-height: 100rpx;
	overflow: hidden;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
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

button {
	background: #f1f1f1;
	color: #5c5c5c;
	border: 0rpx solid #f1f1f1;
	font-size: 18rpx;
	width: 150rpx;
	height: 35rpx;
	margin-left: 0rpx;
	line-height: 34rpx;
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
	color: #5c5c5c;
}

.reply {
	margin-left: 10px;
	color: #bfbfbf;
}

.reply::before {
	content: "|";
	color: #ff0000;
	font-weight: bold;
}

.tui-msg-right {
	max-width: 120rpx;
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

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}</style>
