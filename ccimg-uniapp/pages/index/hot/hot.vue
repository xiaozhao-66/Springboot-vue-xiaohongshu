<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="item-hot" v-for="(item, index) in dataList" :key="index">
				<view class="item-hot-left">{{ index + 1 }}</view>
				<view class="item-hot-right">
					<view class="item-hot-top">
						<view class="top-left">
							<view class="avatar" @click="getUserInfo(item.userId)">
								<image :src="item.avatar" mode="aspectFill" :lazy-load='true'/>
							</view>
							<view class="username">
								{{ item.username }}
							</view>
						</view>
						<view class="top-right">
							{{ item.agreeCount }} <tui-icon name="like-fill" size="14" color="#ff5500"></tui-icon>
						</view>
					</view>

					<view class="content">
						{{ item.content }}
					</view>

					<view class="images" @click="toMain(item.id)">

						<view v-for="(img, index) in item.imgsUrl " v-show="index < 3">
							
							<image :src="img" mode="aspectFill"  :lazy-load='true'  class="fadeImg" />
						</view>

						<view class="nums" v-if="item.count > 3">+{{ item.count - 3 }}</view>
					</view>

				</view>
			</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
	</view>
</template>

<script>
import { getHot } from '@/api/imgDetail.js'
import { addBrowseRecord } from "@/api/browseRecord.js"
export default {

	data() {
		return {
			page: 1,
			limit: 10,
			total: 0,
			dataList: [],
			isEnd: false,
		}
	},
	created() {
		this.getHot()
	},
	methods: {
		getHot() {
			getHot(this.page, this.limit).then(res => {
				this.dataList = res.data.records
				this.total = res.data.total
			})
		},
		loadData() {

			if (this.dataList.length >= this.total) {
				this.isEnd = true
				return
			}
			this.page = this.page + 1;

			getHot(this.page, this.limit).then(res => {

				this.dataList.push(...res.data.records)
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
		toMain(mid) {
			let data = {}
			data.uid = uni.getStorageSync("userInfo").id
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid
				})
			})
		},
	}
}
</script>

<style scoped>
.container {
	margin-top: 160rpx;
}

.page {
	height: 90vh;
}

.item-hot {
	display: flex;
	justify-content: flex-start;
	padding-top: 20rpx;
	
}

.item-hot-left {
	width: 90rpx;
	text-align: center;
	color: #f90000;
}

.item-hot-right {
	width: 620rpx;
	padding-right: 40rpx;
	border-bottom: 1px solid #e7e7e7;
}

.item-hot-top {
	display: flex;
	justify-content: space-between;
	align-items: center;

}

.top-left {
	display: flex;
	align-items: center;
}
.top-left .username{
	font-size: 28rpx;
}

.top-right {
	color: #ff5500;
	font-size: 28rpx;
}

.avatar image {

	width: 80rpx;
	height: 80rpx;
	margin-right: 5px;
	border-radius: 50%;
}

.content {
	font-size: 28rpx;
	clear: both;
	/* 清除左右浮动 */
	word-break: break-word;

	text-overflow: -o-ellipsis-lastline;
	/* 溢出内容隐藏 */
	overflow: hidden;
	/* 文本溢出部分用省略号表示		 */
	text-overflow: ellipsis;
	/* 特别显示模式	 */
	display: -webkit-box;
	/* 行数		 */
	-webkit-line-clamp: 2;
	line-clamp: 2;
	-webkit-box-orient: vertical;

	/* CSS 3 (and 2.1 as well, actually) */
	word-break: break-all
}

.images {
	margin: 8rpx;
	display: flex;
	position: relative;
}

.images image {

	width: 200rpx;
	height: 200rpx;
	margin-right: 5px;
	border-radius: 8px;
}

.nums {
	position: absolute;
	top: 150rpx;
	left: 540rpx;
	background-color: #7a7a7a;
	width: 60rpx;
	height: 45rpx;
	text-align: center;
	color: #fff;
	opacity: 0.5;
	border-radius: 4px;
	z-index: 999;
}

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}


/* 图片淡入淡出 */
.fadeImg {
width: 100px;
height: 100px;
background: #fff;
-webkit-animation: fadeinout 2s linear forwards;
animation: fadeinout 2s linear forwards;
}

@-webkit-keyframes fadeinout {
0%{ opacity: 1; }
50% { opacity: 0.5; }
100% { opacity: 0; }
}

@keyframes fadeinout {
0%{ opacity: 1; }
50% { opacity: 0.5; }
100% { opacity: 0; }
}


@-webkit-keyframes fadeinout {
0%{ opacity: 0; }
50% { opacity: 0.5; }
100% { opacity: 1; }
}

@keyframes fadeinout {
0%{ opacity:0; }
50% { opacity: 0.5; }
100% { opacity: 1; }
}

@-webkit-keyframes fadeinout {
0%{ opacity: 0; }
50% { opacity:1; }
}

@keyframes fadeinout {
0%{ opacity: 0; }
50% { opacity:1; }
}

</style>
