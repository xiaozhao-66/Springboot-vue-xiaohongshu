<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData" refresher-enabled="true"
			:refresher-triggered="triggered" @refresherrefresh="onRefresh" :scroll-top="scrollTop" @scroll="scroll">
			<ul>

				<!-- <uni-list :data='dataList' :total='total'> -->
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

										<image :src="img" mode="aspectFill" :lazy-load='true' class="fadeImg" />
										<!-- <ImgFade :src="img" ></ImgFade> -->
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
								<view class="icon">
									<tui-icon name="agree-fill" size="16" v-if="item.isAgree"
										@click="cancelAgreeImg(item, index)"></tui-icon>
									<tui-icon name="agree" size="16" v-else @click="agreeImg(item, index)"></tui-icon>
									<view class="count">{{ item.agreeCount }}</view>
								</view>
								<view class="icon" @click="getComment(item.mid)">
									<tui-icon name="message" size="16"></tui-icon>
									<view class="count">{{ item.commentCount }}</view>
								</view>
								<view class="icon">
									<tui-icon name="more" size="16"></tui-icon>
								</view>
							</view>
							<!-- 底部聊天 -->
						</view>
					</li>

				<!-- </uni-list> -->
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
	import {
		getAllFollowTrends
	} from "@/api/interest.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import TrendComment from "@/components/trendComment.vue"
	import {
		agree,
		cancelAgree
	} from "@/api/agreeCollect.js"
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	export default {
		components: {
			TrendComment,
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
					title: "用户未登录",
					icon: 'none',
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
					uid: this.userInfo.id
				}
				getAllFollowTrends(this.page, this.limit, params).then(res => {
					res.data.forEach(e => {
						e.time = timeAgo(e.time)
						e.imgsUrl = JSON.parse(e.imgsUrl)

						this.dataList.push(e)
					})

					this.total = res.data.length

				})
			},

			onRefresh() {
				this.triggered = true;

				setTimeout(() => {
					this.triggered = false;
				}, 1000)
				this.page = 1

				//刷新数据
				let params = {
					uid: this.userInfo.id
				}
				getAllFollowTrends(this.page, this.limit, params).then(res => {

					let list = []
					res.data.forEach(e => {
						e.time = timeAgo(e.time)
						e.imgsUrl = JSON.parse(e.imgsUrl)

						list.push(e)
					})

					this.dataList = list

					this.total = res.data.length

				})
			},


			loadData() {

				this.loading = true
				setTimeout(() => {
					if (this.total < this.limit) {
						this.isEnd = true
						return
					}
					this.page = this.page + 1;
					let params = {
						uid: this.userInfo.id
					}

					getAllFollowTrends(this.page, this.limit, params).then(res => {

						res.data.forEach(e => {
							e.time = timeAgo(e.time)
							e.imgsUrl = JSON.parse(e.imgsUrl)

							this.dataList.push(e)
						})

						this.total = res.data.length

					})
				}, 100)
			},

			getUserInfo(uid) {
				uni.navigateTo({
					url: "/pages/otherUser/otherUser?uid=" + uid
				})
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
				data.agreeCollectId = item.mid
				data.agreeCollectUid = item.userId

				agree(data).then(res => {
					this.dataList[index].agreeCount = this.dataList[index].agreeCount * 1 + 1
					this.dataList[index].isAgree = true
				})
			},

			cancelAgreeImg(item, index) {

				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.agreeCollectId = item.mid
				data.agreeCollectUid = item.userId
				data.type = 1
				cancelAgree(data).then(res => {
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

<style scoped>
	@import url(./interest.css);
</style>