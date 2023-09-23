<template>
	
	<!-- 更改使用message2 -->
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="24"></tui-icon>
				<view>我的消息</view>
			</view>
		</tui-navigation-bar>

		<scroll-view scroll-y class="page" refresher-enabled="true" :refresher-triggered="triggered"
			@refresherrefresh="onRefresh">
			<view class="tabs">
				<view class="tabs-body">
					<view class="item " @click="next(1)">
						<tui-badge type="danger" class="tui-badge" absolute
							v-if="record.agreeCollectionCount > 0">{{ record.agreeCollectionCount }}</tui-badge>
						<view class="icon"><tui-icon name="like" color="#f90000"></tui-icon></view>
						<view class="name">赞和收藏</view>
					</view>

					<view class="item" @click="next(2)">
						<tui-badge type="danger" class="tui-badge" absolute
							v-if="record.addFollowCount > 0">{{ record.addFollowCount }}</tui-badge>
						<view class="icon"><tui-icon name="people" color="#00aa00"></tui-icon></view>
						<view class="name">新增关注</view>
					</view>
					<view class="item" @click="next(3)">
						<tui-badge type="danger" class="tui-badge" absolute
							v-if="record.noreplyCount > 0">{{ record.noreplyCount }}</tui-badge>
						<view class="icon"><tui-icon name="message" color="#ffaa00"></tui-icon></view>
						<view class="name">评论和@</view>
					</view>
				</view>
			</view>

			<view>
				<view class="container">
					<tui-list-view>
						<tui-list-cell :lineLeft="false" v-for="(item, index) in dataList" :key="index">

							<tui-swipe-action :actions="actions" @click="handleAction(item,index)">
								<template v-slot:content>

									<view class="tui-item-box" @click="chat(item.sendId,index)">
										<view class="tui-msg-box">
											<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill"
												:lazy-load='true' />
											<view class="tui-msg-item">
												<view class="tui-msg-name">{{ item.username }}</view>
												<view class="tui-msg-content">{{ item.content }}</view>
											</view>
										</view>
										<view class="tui-msg-right">
											<view class="tui-msg-time">{{ item.time }}</view>
											<tui-badge type="danger" class="tui-badge"
												v-if="item.count > 0">{{ item.count }}</tui-badge>
										</view>
									</view>
								</template>
							</tui-swipe-action>

						</tui-list-cell>
					</tui-list-view>
				</view>
			</view>
		</scroll-view>

	</view>
</template>

<script>
	import {
		getChatUserList,
		updateRecordCount,
		deleteRecord
	} from '@/api/chat.js'
	import {
		getUserRecord,
		clearUserRecord
	} from "@/api/user.js"
	import socket from 'plus-websocket'
	import {
		appConfig
	} from '@/config/config.js'
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	export default {
		data() {
			return {
				dataList: [],
				actions: [{
					name: '删除',
					color: '#fff',
					fontsize: 30, //单位rpx
					width: 70, //单位px
					background: '#FD3B31',
				}],

				record: {
					agreeCollectionCount: 0,
					addFollowCount: 0,
					noreplyCount: 0,
				},

				messageCount: 0,

				triggered: false,
				
			}
		},
		computed: {
			// messageCount(){
			// 	return this.record.messageCount
			// }
		},
		watch: {

			record: {
				handler(newVal, oldVal) {

					if (newVal.agreeCollectionCount > 0 || newVal.addFollowCount > 0 || newVal.noreplyCount > 0 || this
						.messageCount > 0) {
						uni.showTabBarRedDot({ //显示红点
							index: 2 //tabbar下标
						})
					} else {
						uni.hideTabBarRedDot({
							index: 2
						})
					}
				},

			},

			messageCount: {
				handler(newVal, oldVal) {

					if (this.record.agreeCollectionCount > 0 || this.record.addFollowCount > 0 || this.record
						.noreplyCount > 0 || newVal > 0) {
						uni.showTabBarRedDot({ //显示红点
							index: 2 //tabbar下标
						})
					} else {
						uni.hideTabBarRedDot({
							index: 2
						})
					}
				},
				immediate: true,
			}

		},
	
		onShow() {
			this.dataList = []
			this.createWs()
			this.getChatUserList()
			this.getUserRecord()
		},
		
		methods: {
			//测试功能，测试navigateBack传递参数
			// getValue(currentTab){
			//     console.log(currentTab,'B页面传递的数据')
			// 	this.currentTab = currentTab
			// },
			//测试结束
			
		
			onRefresh() {
               
				this.createWs()

				this.triggered = true;

				setTimeout(() => {
					this.triggered = false;
				}, 300)

				this.dataList = []

				this.getUserRecord()
				this.getChatUserList()
			},

			next(index) {

				if (index == 1) {
					uni.navigateTo({
						url: "/pages/message/message-agrees/message-agrees"
					})
					//清除所有的赞和收藏数量
				} else if (index == 2) {
					uni.navigateTo({
						url: "/pages/message/message-followers/message-followers"
					})
					//清除新关注数量
				} else {
					uni.navigateTo({
						url: "/pages/message/message-comments/message-comments"
					})
					//清除所有的回复
				}
				let params = {
					uid: uni.getStorageSync("userInfo").id,
					type: index
				}
				clearUserRecord(params).then(res => {
					this.getUserRecord()
				})
			},
			handleAction(e, index) {

				let params = {
					sendId: e.sendId,
					acceptId: uni.getStorageSync("userInfo").id
				}
				deleteRecord(params).then(res => {
					this.messageCount = this.messageCount - this.dataList[index].count
					this.dataList.splice(index, 1)
				})

			},
			getChatUserList() {
				
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getChatUserList(params).then(res => {
					res.data.forEach(item => {
						item.time = timeAgo(item.time)
						this.dataList.push(item)
					})

					res.data.forEach(item => {
						if (item.count > 0) {
							this.messageCount += item.count
						}
					})
				})
			},
			getUserRecord() {
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getUserRecord(params).then(res => {

					this.record = res.data
				})
			},
			chat(fromUid, index) {

				let params = {
					sendId: fromUid,
					acceptId: uni.getStorageSync("userInfo").id
				}
				updateRecordCount(params).then(res => {
					this.messageCount = this.messageCount - this.dataList[index].count
					this.dataList[index].count = 0
					uni.navigateTo({
						url: "/pages/chat/chat2?toUid=" + fromUid
					})
				})
			},

			acceptMessage(data) {

				let that = this

				let message = JSON.parse(data)

				if (Object.getPrototypeOf(message) != Array.prototype) {

					that.record = message
				} else {
					that.dataList = message

					let count = 0;

					message.forEach(item => {
						if (item.count > 0) {
							count += item.count
						}
					})

					that.messageCount = count
				}
			},

			//重新打开websocket
			createWs() {

				let that = this

				socket.connectSocket({
					// ws://192.168.1.103:8083/platform/ws/
					url: 'ws://' + appConfig.WS_API + uni.getStorageSync("userInfo").id
				});
				socket.onSocketOpen(function(res) {
					console.log('WebSocket连接已打开！');
				});
				socket.onSocketError(function(res) {
					console.log('WebSocket连接打开失败，请检查！');
				});
				socket.onSocketMessage(function(res) {
					//uni.$emit('messageCount', { 'records': res.data })
					that.acceptMessage(res.data)

				});

			},

		},

	}
</script>

<style scoped>
	@import url(./message.css);
</style>