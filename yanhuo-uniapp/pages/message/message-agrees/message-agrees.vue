<template>
	<view class="container">

		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
				<view>赞和收藏</view>
			</view>
		</tui-navigation-bar>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-list-view>
				<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">
					<view class="tui-item-box">
						<view class="tui-msg-box">
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill"
								@click="getUserInfo(item.uid)"></image>
								
							<view class="tui-msg-item" @click="getAlbum(item.aid)" v-if="item.type == 3">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view  class="tui-msg-type-item">收藏您的专辑</view>
									
									<view class="tui-msg-time">{{item.createDate}}</view>
								</view>
							
								<view class="tui-msg-content" v-if="item.type == 0">{{ item.content }}</view>
							</view>
								
							<view class="tui-msg-item" @click="getImg(item.mid)" v-else>
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view v-if="item.type == 3" class="tui-msg-type-item">收藏您的专辑</view>
									<view v-if="item.type == 2" class="tui-msg-type-item">收藏您的笔记</view>
									<view v-if="item.type == 1" class="tui-msg-type-item">点赞您的笔记</view>
									<view v-if="item.type == 0" class="tui-msg-type-item">点赞您的评论</view>
									
									<view class="tui-msg-time">{{item.createDate}}</view>
								</view>

								<view class="tui-msg-content" v-if="item.type == 0">{{ item.content }}</view>
							</view>
						</view>
						<view class="tui-msg-right">
							<image :src="item.cover" mode="aspectFit" :lazy-load='true'></image>
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
	import {
		getAllAgreeAndCollection
	} from "@/api/agreeCollect.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		timeAgo
	} from "@/utils/webUtils.js"
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
			back() {
				uni.navigateBack({
					delta: 1
				})
			},

			getAllAgreeAndCollection() {
				let params = {
					uid: this.uid
				}
				getAllAgreeAndCollection(this.page, this.limit, params).then(res => {
                    console.log(res)
					res.data.records.forEach((e) => {
						e.createDate = timeAgo(e.createDate)
						this.dataList.push(e)
					})
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
			
			getAlbum(aid){
				uni.navigateTo({
					url: "/pages/user/albums/albumInfo?albumId=" + aid
				})
			},
			
			loadData() {
				this.page = this.page + 1

				if (this.total < this.limit) {
					this.isEnd = true
					return
				}

				let params = {
					uid: this.uid
				}
				getAllAgreeAndCollection(this.page, this.limit, params).then(res => {

					res.data.records.forEach((e) => {
						e.createDate = timeAgo(e.createDate)
						this.dataList.push(e)
					})
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
	@import url(./message-agrees.css);
</style>