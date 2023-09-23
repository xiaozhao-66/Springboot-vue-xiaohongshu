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
					<view class="tui-item-box">
						<view class="tui-msg-box">
							<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill" :lazy-load='true'
								@click="getUserInfo(item.uid)" />
							<view class="tui-msg-item" @click="getComment(item.mid, item.id,item.replyId)">
								<view class="tui-msg-name">{{ item.username }}</view>
								<view class="tui-msg-type">
									<view v-if="item.replyUid!=uid" class="tui-msg-type-item">评论您的笔记</view>
									<view v-else class="tui-msg-type-item">回复您的评论</view>
									<view class="tui-msg-time">{{ item.time }}</view>
								</view>
								<view class="tui-msg-content">{{ item.content }}</view>
								<view class="tui-msg-content " v-if="item.replyId != 0">
									<view class="reply" style="display: flex;">
										<view v-if="item.replyUid!=uid">{{item.replyName}}:</view>
										<view>{{ item.replyContent }}</view>
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
	import {
		getAllReplyComment
	} from "@/api/comment.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	export default {
		data() {
			return {
				page: 1,
				limit: 8,
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
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			getAllReplyComment() {
				let params = {
					uid: this.uid
				}
				getAllReplyComment(this.page, this.limit, params).then(res => {
                    
					res.data.forEach(e => {
						e.time = timeAgo(e.createDate)
						this.dataList.push(e)
					})
					this.total = res.data.length;
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

				if (this.total < this.limit) {
					this.isEnd = true
					return
				}

				let params = {
					uid: this.uid
				}
				getAllReplyComment(this.page, this.limit, params).then(res => {

					res.data.forEach(e => {
						e.time = timeAgo(e.createDate)
						this.dataList.push(e)
					})
					this.total = res.data.length
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
	@import url(./messgae-comments.css);
</style>