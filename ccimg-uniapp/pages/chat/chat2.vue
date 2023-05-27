<template>
	<!-- 使用websocket做聊天功能(暂未完成) -->
	<view>
		<view class="container" :style="{ 'padding-bottom': `${52 + inputBottom}px` }">
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
						<image class="tui-user-avatar tui-mr" :src="item.avatar" mode="aspectFill"  @click="getUserInfo(item.sendId)"/>
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

<script >
import { addChatRecord, getChatRecord } from "@/api/chat.js"
import { getUserInfo } from '@/api/user.js'
import socket from 'plus-websocket'
import { appConfig } from '@/config/config.js'
import { data } from "jquery";
export default {
	data() {
		return {
			records: [],
			recordInput: "",
			itemAverageHeight: 500, //每条数据平均高度，为确保能滚到底部，可以设置大一些
			scrollTop: 1000,
			inputBottom: 0,
			userInfo: {

			},
			toUid: '',
			page: 1,
			limit: 10,
			total: 0,
			dataList: [],
			toUserInfo: {},
			triggered: false,
			version: 0 ,
			isOpen : false,
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
	   console.log("左滑退出")
	},

	methods: {
		back() {
			zim.logout()
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
						for (let i = 0; i < messageList.length; i++) {
							if(messageList[i].sendId==that.toUid){
								let messageData = {}
								messageData.sendId = messageList[i].sendId
								messageData.content = messageList[i].content
								messageData.username = res.data.username
								messageData.avatar = res.data.avatar
								that.dataList.push(messageData)
							}
						}
				})
			}
		},
		
	
		//
		send() {
		
			let that = this
			let MessageDTO = {}
	
			MessageDTO.sendId = that.userInfo.id+''
			MessageDTO.acceptId = that.toUid+''
			MessageDTO.content = that.recordInput
			console.log("发送消息成功",MessageDTO)
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
			socket.onSocketOpen(function (res) {
				console.log('WebSocket连接已打开！');
			});
			socket.onSocketError(function (res) {
				console.log('WebSocket连接打开失败，请检查！');
			});
			socket.onSocketMessage(function (res) {
				that.acceptMessage(res.data)
			});
			
		},
	},
};
</script>

<style lang="less">
@left-right-margin: 40rpx;

.container {
	width: 100vw;
	height: 100vh;
	box-sizing: border-box;
	padding-bottom: 52px;
	display: flex;
	flex-direction: column;

	.header {
		flex-shrink: 0;
		padding: 0px @left-right-margin 32rpx @left-right-margin;

		.nav {
			display: flex;
			justify-content: space-between;
			margin-right: 10px;
		}
	}

	.content {
		margin-top: 52px;
		flex-grow: 1;
		overflow: auto;
		height: 90vh;

		.message {
			margin: 0px @left-right-margin 32rpx;
			display: flex;
			flex-direction: column;
			align-items: flex-end;

			.message-content {
				width: auto;
				max-width: calc(100vw - 80rpx);
				word-wrap: break-word;
				box-sizing: border-box;
				padding: 32rpx;
				line-height: 48rpx;
				background: pink;
				border-radius: 16rpx 0px 16rpx 16rpx;
			}

			.message-time {
				line-height: 32rpx;
				margin-top: 16rpx;
			}
		}

		.tui-flex-end {
			justify-content: flex-end;
		}

		.tui-chat-item {
			width: 100%;
			padding: 30rpx 30rpx 0 30rpx;
			box-sizing: border-box;
			display: flex;
			position: relative;

			.tui-chat-box {
				max-width: 70%;

				.tui-user-name {
					font-size: 24rpx;
					color: #999;
				}

				.tui-chat-text {
					width: 100%;
					min-width: 40rpx;
					padding: 18rpx 24rpx;
					display: flex;
					align-items: center;
					flex-wrap: wrap;
					word-break: break-all;
					box-sizing: border-box;
				}
			}

			.tui-user-avatar {
				width: 84rpx;
				height: 84rpx;
				border-radius: 50%;
				flex-shrink: 0;
			}

			.tui-mr {
				margin-right: 24rpx;
			}

			.tui-ml {
				margin-left: 24rpx;
			}

		}
	}
}

.bottom-textarea {
	position: fixed;
	bottom: 0px;
	left: 0px;
	right: 0px;
	background-color: #fff;
	border-top: 1rpx solid #f4f4f4;
	padding: 6px 20px;
	display: flex;
	align-items: flex-end;

	.textarea-container {
		flex: 1;
		min-height: 80rpx;
		background: #f2f2f2;
		border-radius: 8rpx;
		box-sizing: border-box;
		padding: 20rpx 16rpx;

		>textarea {
			max-height: 250rpx;
			font-size: 36rpx;
			color: rgba(8, 8, 8, 1);
			width: auto;
		}
	}
}</style>