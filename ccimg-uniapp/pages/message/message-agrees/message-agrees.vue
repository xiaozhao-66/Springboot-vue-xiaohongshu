<template>
	<view class="container">

		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="24"  @click="back"></tui-icon>
				<view>赞和收藏</view>
			</view>
		</tui-navigation-bar>


		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-list-view>
				<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">
					<view class="tui-item-box" >
						<view class="tui-msg-box">
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill" @click="getUserInfo(item.uid)"></image>
							<view class="tui-msg-item" @click="getImg(item.mid)">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view v-if="item.type == 2" class="tui-msg-type-item">收藏您的笔记</view>
									<view v-if="item.type == 1" class="tui-msg-type-item">点赞您的笔记</view>
									<view v-if="item.type == 0" class="tui-msg-type-item">点赞您的评论</view>
									<view class="tui-msg-time">{{ item.time }}</view>
								</view>

								<view class="tui-msg-content" v-if="item.type == 0">{{ item.content }}</view>
							</view>
						</view>
						<view class="tui-msg-right">
							<image :src="item.cover"  mode="aspectFit" :lazy-load='true'></image>
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
import { getAllAgreeAndCollection } from "@/api/agree.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
export default {
	data() {
		return {
			dataList: [],
			page: 1,
			limit: 10,
			total: 0,
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			uid: '',
		}
	},
	created() {
		this.uid = uni.getStorageSync("userInfo").id
		this.getAllAgreeAndCollection()
	},
	methods: {
        back(){
		   uni.navigateBack({
			   delta:1
		   })
		},
		getAllAgreeAndCollection() {
			let params = {
				uid: this.uid
			}
			getAllAgreeAndCollection(this.page, this.limit, params).then(res => {

				this.dataList = res.data.records;
				this.total = res.data.total
			})
		},
		getImg(mid) {

			let data = {}
			data.uid = this.uid
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid
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
			getAllAgreeAndCollection(this.page, this.limit, params).then(res => {
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
	width: 400rpx;

	min-height: 80rpx;
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
	margin-left: 10px;
}

.tui-msg-content::before {
	content: "|";
	color: #f90000;
}

.tui-msg-right {
	width: 120rpx;
	height: 88rpx;
	margin-left: auto;
	text-align: right;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	align-items: flex-end;
}

.tui-msg-right image {
	  max-width: 88rpx;
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
}
</style>
