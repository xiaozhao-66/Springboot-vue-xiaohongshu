<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
					<view>专辑详情</view>

				</view>
				<view><tui-icon name="more-fill" size="20" @click="popup"></tui-icon></view>
			</view>
		</tui-navigation-bar>
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="info">
				<view class="info-center">
					<view class="title">{{ albumInfo.name }}专辑</view>
					<view class="count">{{ albumInfo.imgCount }}张图片</view>
					<view class="avatar">
						<image :src="albumInfo.avatar" mode="aspectFill" :lazy-load='true' />
					</view>
					<view class="username">{{ albumInfo.username }}</view>
				</view>
			</view>

			<view class="body">

				<tui-grid :unlined='true'>
					<block v-for="(item, index) in dataList" :key="index">
						<tui-grid-item :cell="2" @click="detail" :border="false" :bottomLine="false">
							<view class="card">
								<view class="del-title">

									<view class="del-cicle" v-if="isshow && !delMids.includes(item.id)">
										<tui-icon name="circle" color="#f4f4f4" size="24"
											@click="delAffirm(item.id)"></tui-icon>
									</view>
									<view class="del-affirm" v-if="isshow && delMids.includes(item.id)"><tui-icon
											name="circle-fill" color="#ff626c" size="24"
											@click="delCancel(item.id)"></tui-icon>
									</view>
								</view>

								<view @click="getImgInfo(item.id,item.status)">
									<image class="cover" :src="item.cover" mode="aspectFill" :lazy-load='true' />
								</view>
								<view class="card-nums">
									{{ item.count }}
								</view>
								<view class="cont">
									<view class="content">{{ item.content }}</view>
									<view class="userInfo">
										<view class="avatar-usrname">
											<image :src="item.avatar" mode="aspectFill" :lazy-load='true' />
											<view>{{ item.username }}</view>
										</view>
										<view><tui-icon name="star" size="10"></tui-icon>{{ item.agreeCount }}</view>
									</view>

								</view>
							</view>
						</tui-grid-item>
					</block>
				</tui-grid>
			</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>

		<view class="fotter" v-if="isshow">
			<view class="f-left">
				<view class="f-del-cicle" v-if="T">
					<tui-icon name="circle" color="#d6d6d6" size="28" @click="isAll"></tui-icon>
				</view>
				<view class="del-affirm" v-else><tui-icon name="circle-fill" color="#ff626c" size="28"
						@click="cancalAll"></tui-icon></view>
				<view>全选</view>

			</view>
			<view class="f-right">
				<tui-button type="danger" height="60rpx" @click="delRecord" :size="28">删除</tui-button>
				<tui-button type="gury" height="60rpx" @click="cancalDel" :size="28">取消</tui-button>
			</view>
		</view>

		<!--底部分享弹窗-->
		<tui-bottom-popup backgroundColor="#f6f6f6" :zIndex="1002" :maskZIndex="1001" :show="popupShow" @close="popup">
			<view class="tui-share">
				<view class="tui-share-title">分享到</view>

				<view class="tui-share-top" v-if="type == 1">

					<view class="tui-share-item"
						:class="[shareList[1].operate.length - 1 === index ? 'tui-item-last' : '']"
						v-for="(item, index) in shareList[1].operate" :key="index" @tap="popup">
						<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150">
							<view @click="operate(index)">
								<tui-icon :name="item.icon" color="#404040" :size="item.size"></tui-icon>
							</view>
						</view>
						<view class="tui-share-text">{{ item.name }}</view>
					</view>
				</view>

				<view class="tui-share-top" v-else>

					<view class="tui-share-item"
						:class="[shareList[2].operate.length - 1 === index ? 'tui-item-last' : '']"
						v-for="(item, index) in shareList[2].operate" :key="index" @tap="popup">
						<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150">
							<view @click="operate2(index)">
								<tui-icon :name="item.icon" color="#404040" :size="item.size"></tui-icon>
							</view>
						</view>
						<view class="tui-share-text">{{ item.name }}</view>
					</view>
				</view>

				<view class="tui-share-bottom tui-mt">
					<view class="tui-share-item"
						:class="[shareList[0].share.length - 1 === index ? 'tui-item-last' : '']"
						v-for="(item, index) in shareList[0].share" :key="index" @tap="popup">
						<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150">
							<tui-icon :name="item.icon" :color="item.color"
								:size="item.size ? item.size : 36"></tui-icon>
						</view>
						<view class="tui-share-text">{{ item.name }}</view>
					</view>
					<view class="tui-empty">!</view>
				</view>

				<view class="tui-btn-cancle" @tap="popup">取消</view>
			</view>
		</tui-bottom-popup>
		<!--底部分享弹窗-->

	</view>
</template>

<script>
	import {
		collection,
		cancelAgree,
		cancelCollection
	} from "@/api/agreeCollect.js"
	import {
		getAllImgByAlbum,
		deleteImgs
	} from "@/api/imgDetail.js"
	import {
		getAlbum
	} from "@/api/album.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		stdout
	} from "process"
	export default {
		data() {
			return {
				albumId: '',
				dataList: [],
				albumInfo: {},
				page: 1,
				limit: 6,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				popupShow: false,
				type: 0,
				shareList: [{
						share: [{
							name: "QQ",
							icon: "qq",
							color: "#07BDFD",
							size: 34
						}, {
							name: "微信",
							icon: "wechat",
							color: "#80D640"
						}, {
							name: "朋友圈",
							icon: "moments",
							color: "#80D640",
							size: 32
						}, {
							name: "新浪微博",
							icon: "sina",
							color: "#F9C718"
						}, {
							name: "复制链接",
							icon: "applets",
							color: "#2BA348"
						}]
					},
					{
						operate: [{
								name: "编辑专辑",
								icon: "edit",
								size: 30
							}, {
								name: "转入专辑",
								icon: "edit",
								size: 28
							},
							{
								name: "删除图片",
								icon: "offline",
								size: 30
							},
							{
								name: "社区公约",
								icon: "nodata",
								size: 30
							},
							{
								name: "其他内容",
								icon: "immore",
								size: 28
							}
						]
					},
					{
						operate: [{
								name: "收藏专辑",
								icon: "edit",
								size: 30
							},
							{
								name: "取消收藏",
								icon: "offline",
								size: 28
							},
						]
					}

				],
				isshow: false,
				affirmId: '',
				delMids: [],
				T: true,
				uid: '',
			}

		},
		onLoad(option) {
			this.albumId = option.albumId
		},
		created() {
			this.getAlbum()
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			getAllImgByAlbum() {

				let params = {
					albumId: this.albumId,
					type: this.type
				}

				getAllImgByAlbum(this.page, this.limit, params).then(res => {
					this.dataList = res.data.records
					this.total = res.data.total
				})
			},
			getAlbum() {
				let params = {
					id: this.albumId,
				}
				getAlbum(params).then(res => {

					this.albumInfo = res.data

					let userInfo = uni.getStorageSync("userInfo")
					this.uid = userInfo.id
					if (res.data.uid == userInfo.id) {
						this.type = 1
					}
					this.getAllImgByAlbum()
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
						albumId: this.albumId,
						type: this.type,
					}

					getAllImgByAlbum(this.page, this.limit, params).then(res => {
						this.dataList.push(...res.data.records)

					})
				}, 100)
			},

			getImgInfo(mid, status) {

				if (status == 0) {
					uni.showToast({
						title: "图片正在上传中...",
						icon: "none"
					});
					return
				}

				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.mid = mid

				addBrowseRecord(data).then(res => {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + mid
					})
				})
			},

			popup() {
				this.popupShow = !this.popupShow
			},
			operate(index) {

				//编辑操作
				if (index == 0) {
					uni.navigateTo({
						url: "/pages/user/albums/createalbum?albumId=" + this.albumId
					})
				} else if (index == 2) {
					this.isshow = true

				}
			},

			operate2(index) {
				if (index == 0) {

					let data = {}

					data.uid = uni.getStorageSync("userInfo").id
					data.agreeCollectId = this.albumId
					data.agreeCollectUid = this.albumInfo.uid
					data.type = 3
					collection(data).then(res => {
						uni.showToast({
							title: res.data.message
						})
					})
				} else if (index == 1) {
					let data = {}
					data.uid = uni.getStorageSync("userInfo").id
					data.agreeCollectId = this.albumId
					data.agreeCollectUid = this.albumInfo.uid
					data.type = 3
					cancelCollection(data).then(res => {
						uni.showToast({
							title: res.data.message
						})
					})
				}
			},

			delAffirm(mid) {
				this.delMids.push(mid)

			},
			delCancel(mid) {
				let index = this.delMids.indexOf(mid)
				this.delMids.splice(index, 1)

			},
			isAll() {
				this.T = false
				this.dataList.forEach(item => {
					this.delMids.push(item.id)
				})
			},
			cancalAll() {
				this.T = true
				this.delMids = []
			},
			cancalDel() {
				this.isshow = false
				this.delMids = []
			},
			//删除
			delRecord() {

				deleteImgs(this.delMids, this.uid).then(res => {
					uni.showToast({
						title: "删除成功"
					})

					setTimeout(() => {
						this.page = 1
						this.getAllImgByAlbum()
						this.isshow = false
						this.delMids = []
					}, 1000)

				})
			}
		}
	}
</script>

<style scoped>
	@import url(./css/albumInfo.css);
</style>