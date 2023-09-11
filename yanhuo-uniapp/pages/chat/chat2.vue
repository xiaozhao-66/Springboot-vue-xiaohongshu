<template>
	<!-- 使用websocket做聊天功能(暂未完成) -->
	<view>
		<view class="container" :style="{ 'padding-bottom': `${175 + inputBottom}rpx` }">
			<view class="header">

				<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
					<view class="nav">

						<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
						<view class="username">{{ toUserInfo.username }}</view>


						<tui-icon name="more-fill" size="22"></tui-icon>
					</view>
				</tui-navigation-bar>


			</view>
			<scroll-view class="content" :scroll-y="true" :scroll-top="scrollTop" refresher-enabled="true"
				:refresher-triggered="triggered" @refresherrefresh="onRefresh">
				<view v-for="(item, index) in dataList" id="chatBox">
					<view class="tui-chat-item" v-if="item.sendId != userInfo.id">
						<image class="tui-user-avatar tui-mr" :src="item.avatar" mode="aspectFill"
							@click="getUserInfo(item.sendId)" />
						<view class="tui-chat-box">
							<!-- <view class="tui-user-name" >{{item.username}}</view> -->
							<tui-bubble-popup direction="left" position="relative" :mask="false" :show="true"
								triangleLeft="-22rpx" triangleTop="30rpx" width="100%" backgroundColor="#ff7900">
								<view class="tui-chat-text">{{ item.content }}</view>
							</tui-bubble-popup>
						</view>
					</view>
					<view class="tui-chat-item tui-flex-end" v-else>
						<view class="tui-chat-box">
							<!-- <view class="tui-user-name" >{{item.username}}</view> -->
							<tui-bubble-popup direction="right" position="relative" :mask="false" :show="true"
								triangleRight="-22rpx" triangleTop="30rpx" width="100%" :flexEnd="true"
								backgroundColor="#19be6b">
								<view class="tui-chat-text">{{ item.content }}</view>
							</tui-bubble-popup>

						</view>

						<image class="tui-user-avatar tui-ml" :src="item.avatar" mode="aspectFill" />
					</view>

				</view>
			</scroll-view>
		</view>
		<view class="bottom-textarea" :style="{ bottom: inputBottom + 'px' }">
			<view class="textarea-container">
				<textarea v-model="recordInput" :maxlength="-1" :auto-height="true" :show-confirm-bar="false"
					:cursor-spacing="10" :fixed="true" :adjust-position="false" @focus="focusTextarea"
					@blur="blurTextarea" />
			</view>
			<tui-button type="danger" height="82rpx" width="100rpx" @click="send()">发送</tui-button>
		</view>
	</view>
</template>

<script>
	import {
		addChatRecord,
		getChatRecord
	} from "@/api/chat.js"
	import {
		getUserInfo
	} from '@/api/user.js'
	import socket from 'plus-websocket'
	import {
		appConfig
	} from '@/config/config.js'
	export default {
		data() {
			return {
				records: [],
				recordInput: "",
				itemAverageHeight: 500, //每条数据平均高度，为确保能滚到底部，可以设置大一些
				scrollTop: 1000,
				inputBottom: 0,
				userInfo: {},
				toUid: '',
				page: 1,
				limit: 10,
				total: 0,
				dataList: [],
				toUserInfo: {},
				triggered: false,
				version: 0,
				isOpen: false,

			};
		},
		onLoad(option) {
			this.toUid = option.toUid
			this.userInfo = uni.getStorageSync("userInfo")
			this.getToUserInfo()
		},
		created() {
			this.createWs()
		},

		onShow() {
			this.getList()
		},

		onBackPress() {
			// console.log("左滑退出")
			// uni.setStorageSync("refresh",true)
		},

		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},

			getToUserInfo() {
				let params = {
					uid: this.toUid
				}

				getUserInfo(params).then(res => {
					this.toUserInfo = res.data
				})

			},
			//创建聊天
			acceptMessage(data) {
				let that = this

				let messageList = JSON.parse(data)
				if (Object.getPrototypeOf(messageList) == Array.prototype) {
					let params = {
						uid: that.toUid
					}
					getUserInfo(params).then(res => {

						messageList.forEach(item => {
							if (item.sendId == that.toUid) {
								let messageData = {}
								messageData.sendId = item.sendId
								messageData.content = item.content
								messageData.username = res.data.username
								messageData.avatar = res.data.avatar
								that.dataList.push(messageData)
							}
						})

					})
				}
			},


			//
			send() {
				let that = this

				if (that.recordInput == '' || that.recordInput == null) {
					uni.showToast({
						title: "内容不能为空~",
						icon: 'none'
					})
					return
				}
				let MessageDTO = {}

				MessageDTO.sendId = that.userInfo.id + ''
				MessageDTO.acceptId = that.toUid + ''
				MessageDTO.content = that.recordInput

				//往数据库中添加数据
				addChatRecord(MessageDTO).then(res => {
					let messageData = {}
					messageData.sendId = that.userInfo.id
					messageData.avatar = that.userInfo.avatar
					messageData.content = that.recordInput
					that.dataList.push(messageData)
					that.recordInput = ''
					that.scrollTop = that.scrollTop + that.itemAverageHeight;
				})

			},
			//-------------------------------

			getList() {

				//获取记录信息
				let params = {
					sendUid: this.userInfo.id,
					acceptUid: this.toUid
				}
				getChatRecord(this.page, this.limit, params).then(res => {
					this.dataList = res.data.records.reverse()
					this.total = res.data.total
					this.scrollTop = this.scrollTop + this.dataList.length * this.itemAverageHeight;
				})
			},
			focusTextarea(e) {
				this.inputBottom = e.detail.height;
				this.scrollTop += 1; //滚到底部
			},
			blurTextarea(e) {
				this.inputBottom = 0;
				this.scrollTop += 1; //滚到底部
			},

			onRefresh() {

				//重新打开websocket
				this.createWs()

				this.triggered = true;
				setTimeout(() => {
					this.triggered = false;
				}, 1000)

				this.page = this.page + 1

				if (this.dataList.length >= this.total) {
					return
				}
				let params = {
					sendUid: this.userInfo.id,
					acceptUid: this.toUid
				}
				getChatRecord(this.page, this.limit, params).then(res => {

					this.dataList.unshift(...res.data.records.reverse())
				})
			},

			getUserInfo(uid) {
				uni.navigateTo({
					url: "/pages/otherUser/otherUser?uid=" + uid
				})
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
					that.acceptMessage(res.data)
				});

			},
		},
	};
</script>

<style scoped>
	@import url(./chat.css);
</style>