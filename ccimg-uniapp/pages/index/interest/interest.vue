<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData" refresher-enabled="true"
			:refresher-triggered="triggered" @refresherrefresh="onRefresh" :scroll-top="scrollTop" @scroll="scroll">
			<ul>
				<li v-for="(item, index) in dataList" :key="index">
					<view class="item">
						<view class="top">
							<view class="avatar-user">
								<view class="avatar" @click="getUserInfo(item.userId)">
									<image :src="item.avatar" mode="aspectFill" :lazy-load='true' />
								</view>
								<view class="user">
									<view class="name">{{ item.username }}</view>
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
									<image :src="img" mode="aspectFill" :lazy-load='true'/>
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


						<view class="fotter">
							<view class="icon" >
								<tui-icon name="agree-fill" size="16" v-if="item.isAgree" @click="cancelAgreeImg(item, index)"  ></tui-icon>
								<tui-icon name="agree" size="16" v-else   @click="agreeImg(item, index)"></tui-icon>
								<view class="count">{{ item.agreeCount }}</view>
							</view>
							<view class="icon" @click="getComment(item.mid)">
								<tui-icon name="message" size="16"></tui-icon>
								<view class="count">{{ item.commentCount }}</view>
							</view>
							<view class="icon">
                                 <tui-icon name="more" size="16" ></tui-icon>
							</view>
						</view>
						<!-- 底部聊天 -->
					</view>
				</li>
			</ul>

         <view class="loadStyle" v-if="!isEnd && loading">
			<tui-icon name="loading" :size="18"></tui-icon>
		</view>
		<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>

		</scroll-view>

		<trend-comment :popupShow="popupShow" @popup="popup" :mid="mid" :seed="seed"></trend-comment>

	</view>
</template>

<script>
import { getAllFollowTrends } from "@/api/interest.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
import TrendComment from "@/components/trendComment.vue"
import { agree,cancelAgree} from "@/api/agree.js"
export default {
	components: {
		TrendComment
	},
	data() {
		return {
			userInfo: {},
			triggered: false,
			page: 1,
			limit: 4,
			userInfo: {},
			dataList: [],
			popupShow: false,
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			total: 0,
			mid: '',
			seed: 0,
			scrollTop: 0,
			old: {
				scrollTop: 0
			},
		}
	},
	created() {
		this.userInfo = uni.getStorageSync("userInfo")
		if (typeof this.userInfo == 'undefined' || this.userInfo == null || this.userInfo == '') {
			uni.showToast({
			   title:"用户未登录",
			   icon:'none',
			})
			return;
		} else {
			this.getAllFollowTrends()
		}

	},
	methods: {
		getComment(mid) {

			this.mid = mid
			this.seed = Math.random()
			this.popupShow = true;
			uni.hideTabBar()
		},
		popup(popupShow) {

			this.popupShow = popupShow
		},
		getAllFollowTrends() {

			let params = {
				userId: this.userInfo.id
			}
			getAllFollowTrends(this.page, this.limit, params).then(res => {
                console.log(res.data)
				this.dataList = res.data
				this.total = res.data.length

			})
		},

		onRefresh() {
			this.triggered = true;

			setTimeout(() => {
				this.triggered = false;
			}, 1000)
			this.page = 1
			this.getAllFollowTrends()
		},


		loadData() {

			this.loading = true
			setTimeout(() => {
				if (this.total == 0) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;
				let params = {
					userId: this.userInfo.id
				}

				getAllFollowTrends(this.page, this.limit, params).then(res => {

					this.dataList.push(...res.data)
					this.total = res.data.length

				})
			}, 1000)
		},

		getUserInfo(uid) {
			if (uid == this.userInfo.id) {
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
		},

		agreeImg(item, index) {
			let data = {}
			data.uid = uni.getStorageSync("userInfo").id
			data.type = 1
			data.agreeId = item.mid
			data.agreeUid = item.userId

			agree(data).then(res => {
				this.dataList[index].agreeCount = this.dataList[index].agreeCount * 1 + 1
				this.dataList[index].isAgree = true
			})
		},

        cancelAgreeImg(item,index){
		  	
			let data = {}
			data.uid = uni.getStorageSync("userInfo").id
			data.agreeId = item.mid
			data.agreeUid = item.userId
			data.type = 1
			cancelAgree(data).then(res=>{
				this.dataList[index].agreeCount = this.dataList[index].agreeCount * 1 - 1
				this.dataList[index].isAgree = false
			})
		},
		scroll(e) {

			this.old.scrollTop = e.detail.scrollTop
		},

	}
}
</script>

<style >
ul {
	padding: 0rpx;
}

li {

	list-style: none;
}

.container {
	background-color: #f4f4f4;
	margin-top: 160rpx;
}

.page {
	height: 90vh;
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
	height: 240rpx;
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
}

.content2 {
	color: #fd6d49;
}

.fotter {
	width: 94%;
	height: 80rpx;
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
}</style>
