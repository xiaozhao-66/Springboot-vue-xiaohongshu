<template>
	<view class="container">
			<ul>
				<uni-list :data='dataList' :total='total'>
					<li v-for="(item, index) in dataList" :key="index">
						<view class="item">
							<view class="top">
								<view class="avatar-user">
									<view class="avatar">
										<image :src="userInfo.avatar" mode="aspectFill" :lazy-load='true' />
									</view>
									<view class="user">
										<view class="name">{{ userInfo.username }}</view>
										<view class="time">{{ item.time }}</view>
									</view>
								</view>
								<view class="upload-loading" v-if="item.status==0" @click="cancelUpload(item.mid)">
									上传中...</view>
							</view>
							<view class="content">
								{{ item.content }}
							</view>
							<view class="main">

								<view class="img-list" @click="toMain(item.mid,item.status)">
									<view v-for="(img, index) in item.imgsUrl" :key="index">
										<image :src="img" mode="aspectFill" :lazy-load='true' class="fadeImg" />
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
				</uni-list>
			</ul>

			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
	</view>
</template>

<script>
	import {
		getTrendByUser
	} from "@/api/user.js"
	import {
		getUserInfo
	} from "@/api/user.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	import {
		publish,
		updateImgDetail,
		updateStatus,
		deleteImgs
	} from '@/api/imgDetail.js'
	import {
		appConfig
	} from '@/config/config'
	export default {
		props: {
			uid: String,
			seed:Number,
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
				type: 0,
			
			}
		},
		
		watch:{
			seed(newVal,oldVal){
				this.loadData()
			}
		},

		created() {
			this.getUserInfo(this.uid)
		},
		
		methods: {

			getTrendByUser() {

				let params = {
					userId: this.userInfo.id,
					type: this.type,
				}
				getTrendByUser(this.page, this.limit, params).then(res => {
					
					res.data.records.forEach(e => {
						e.time = timeAgo(e.time)
						e.imgsUrl = JSON.parse(e.imgsUrl)

						this.dataList.push(e)
					})
					this.total = res.data.total
				})
			},
			getUserInfo(uid) {
				let params = {
					uid: uid
				}
				getUserInfo(params).then(res => {
					this.userInfo = res.data
					if (res.data.id == uni.getStorageSync("userInfo").id) {
						this.type = 1
					}
					this.getTrendByUser()

				})
			},

			loadData() {

				this.loading = true
				setTimeout(() => {
					if (this.dataList.length >= this.total) {
						this.isEnd = true
						return
					}
					this.page = this.page + 1;
					let params = {
						userId: this.userInfo.id,
						type: this.type
					}

					getTrendByUser(this.page, this.limit, params).then(res => {

						res.data.records.forEach(e => {
							e.time = timeAgo(e.time)
							e.imgsUrl = JSON.parse(e.imgsUrl)
							this.dataList.push(e)
						})
					})
				}, 200)
			},

			cancelUpload(mid) {
				this.$emit('cancelUp', mid)
			},

			toMain(mid, status) {
				
				if (status == 0) {
					uni.showToast({
						title: "图片正在上传中...",
						icon: "none"
					});
					return
				}

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
	@import url(./trends.css);
</style>