<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<ul>
				<li v-for="(item, index) in dataList" :key="index">
					<view class="item">
						<view class="top">
							<view class="avatar-user">
								<view class="avatar">
									<image :src="userInfo.avatar" mode="aspectFill"  :lazy-load='true'/>
								</view>
								<view class="user">
									<view class="name">{{ userInfo.username }}</view>
									<view class="time">{{ item.time }}</view>
								</view>
							</view>
						</view>
						<view class="content">
							{{ item.content }}
						</view>
						<view class="main">

							<view class="img-list" @click="toMain(item.mid)">
								<view v-for="(img, index) in item.imgsUrl" :key="index">
									<image :src="img" mode="aspectFill"  :lazy-load='true' class="fadeImg"/>
								</view>
							</view>

							<view class="collection-album" @click="toAlbum(item.albumId)">
								<view class="left">
									<tui-icon name="upload" size="18" color="#fd6d49"></tui-icon>
									<view class="content1">更新专辑:</view>
									<view class="content2">{{ item.albumName }}</view>
								</view>
								<view class="right">
									<tui-icon name="arrowright" size="24"></tui-icon>
								</view>
							</view>

						</view>
						<view class="fotter"></view>
					</view>
				</li>
			</ul>

			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
	</view>
</template>

<script>
import { getTrendByUser } from "@/api/user.js"
import { getUserInfo } from "@/api/user.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
import {timeAgo} from "@/utils/webUtils.js"
export default {
	props: {
		uid: String,
	},
	data() {
		return {
			page: 1,
			limit: 3,
			userInfo: {},
			dataList: [],
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			total: 0,

		}
	},
	
	created() {
		this.getUserInfo(this.uid)
	},
	methods: {

		getTrendByUser() {

			let params = {
				userId: this.userInfo.id
			}
			getTrendByUser(this.page, this.limit, params).then(res => {
                console.log(res.data)
				res.data.forEach(e=>{
					e.time = timeAgo(e.time)
					e.imgsUrl = JSON.parse(e.imgsUrl)
					
					this.dataList.push(e)
				})
				this.total = res.data.length
			})
		},
		getUserInfo(uid) {
			let params = {
				uid: uid
			}
			getUserInfo(params).then(res => {
				this.userInfo = res.data
				this.getTrendByUser()
			})
		},

		loadData() {
            console.log("122")
			this.loading = true
			setTimeout(() => {
				if (this.total<this.limit) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;
				let params = {
					userId: this.userInfo.id
				}

				getTrendByUser(this.page, this.limit, params).then(res => {

					res.data.forEach(e=>{
						e.time = timeAgo(e.time)
						e.imgsUrl = JSON.parse(e.imgsUrl)
						
						this.dataList.push(e)
					})
					this.total = res.data.length
				})
			}, 1000)
		},

		toMain(mid) {
			let data = {}
			data.uid = this.userInfo.id
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid
				})
			})
		},

		toAlbum(aid) {
			uni.navigateTo({
				url: "/pages/user/albums/albumInfo?albumId=" + aid
			})
		}
	}
}
</script>

<style scoped>
ul {
	padding: 0rpx;
}

li {

	list-style: none;
}

.container {
	margin-top: 40rpx;
	background-color: #f4f4f4;
}

.page {
	height: 91vh;
}

.item {
	background-color: #fff;
	border-bottom: 1px solid #f4f4f4;
}

li:not(:first-child) {
	margin-top: 20px;
}



.avatar-user {
	display: flex;
	align-items: center;
	margin-left: 10px;
	padding-top: 10px;

}

.avatar-user image {
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
}

.name {
	font-size: 35rpx;
}

.time {
	color: #9c9c9c;
	font-size: 24rpx;
}

.main {
	padding-top: 10px;
}


.img-list {
	margin-left: 10px;
	display: flex;
	flex-wrap: wrap;
}

.img-list image {
	width: 230rpx;
	height: 230rpx;
	margin-right: 5px;
	border-radius: 8px;
}

.collection-album {
	display: flex;
	height: 60rpx;
	align-items: center;
	margin-left: 10px;
	margin-right: 10px;
	justify-content: space-between;
}

.collection-album .left {
	display: flex;
	align-items: center;
	font-size: 24rpx;
}

.content1 {
	margin: 0 5rpx;
	color: #9c9c9c;
}

.content2 {

	color: #fd6d49;
}

.fotter {
	width: 94%;
	height: 40rpx;
	margin: auto;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.content {
	margin-left: 10px;
	font-size: 32rpx;
	clear: both;
	/* 清除左右浮动 */
	word-break: break-word;
	/* 文本行的任意字内断开，就算是一个单词也会分开 */

	word-wrap: break-word;
	/* IE */

	white-space: -moz-pre-wrap;
	/* Mozilla */

	white-space: -hp-pre-wrap;
	/* HP printers */

	white-space: -o-pre-wrap;
	/* Opera 7 */

	white-space: -pre-wrap;
	/* Opera 4-6 */

	white-space: pre;
	/* CSS2 */

	white-space: pre-wrap;
	/* CSS 2.1 */

	white-space: pre-line;
	/* CSS 3 (and 2.1 as well, actually) */
}

.icon {
	display: flex;
	align-items: center;

}

.count {
	color: #5c5c5c;
	font-size: 30rpx;
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
