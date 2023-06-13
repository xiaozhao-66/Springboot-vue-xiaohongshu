<template>
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
						<image class="tui-user-avatar tui-mr" :src="item.avatar" mode="aspectFill" />
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
			<tui-button type="danger" height="80rpx" width="100rpx" @click="send()">发送</tui-button>
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

	import ZIM from '@/js_sdk/zego-ZIMUniplugin-JS/lib';
	// import ZIM from '@/js_sdk/zego-ZIMUniPlugin-JS/lib';
	import {
		getZimToken
	} from '@/api/chat.js'
	var appID = 1562974438;
	// 静态同步方法，创建 zim 实例，传入 AppID。
	// create 方法仅第一次调用时会创建 ZIM 实例，后续调用会返回 null。
	ZIM.create({
		appID
	});
	// 通过 getInstance 获取单实例，避免热更新导致 create 多次创建返回 null。
	var zim = ZIM.getInstance();

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
				version: 0,
			};
		},
		onLoad(option) {
			this.toUid = option.toUid
			this.userInfo = uni.getStorageSync("userInfo")
			this.getToUserInfo()
		},
		created() {
			this.createws()
		},

		onShow() {
			this.getList()
		},

		onBackPress() {
			console.log("左滑退出")
			zim.logout()
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
			createws() {

				let that = this
				//得到token信息
				let params = {
					userId: that.userInfo.id
				}

				getZimToken(params).then(response => {

					console.log("得到信息", response)

					var userInfo = {
						userID: that.userInfo.id,
						userName: that.userInfo.username
					};

					var token = response.data

					zim.login(userInfo, token).then(function() {

							console.log("登录成功")
						})
						.catch(function(err) {
							// 登录失败
						});
				})

				// 注册监听“运行时错误信息”的回调
				zim.on('error', function(zim, errorInfo) {
					console.log('error', errorInfo.code, errorInfo.message);
				});

				// 注册监听“网络连接状态变更”的回调
				zim.on('connectionStateChanged', function(zim, {
					state,
					event,
					extendedData
				}) {
					// console.log('connectionStateChanged', state, event, extendedData);
				});

				// 注册监听“收到单聊消息”的回调
				zim.on('receivePeerMessage', function(zim, {
					messageList,
					fromConversationID
				}) {


					//这里userId是他人的id
					if (fromConversationID == that.toUid) {

						let params = {
							uid: fromConversationID
						}
						getUserInfo(params).then(res => {

							for (let i = 0; i < messageList.length; i++) {
								let messageData = {}
								messageData.sendId = messageList[i].fromConversationID
								messageData.content = messageList[i].message
								messageData.username = res.data.username
								messageData.avatar = res.data.avatar
								that.dataList.push(messageData)
							}

						})
					}

				});

				// 注册监听“令牌即将过期”的回调
				zim.on('tokenWillExpire', function(zim, {
					second
				}) {
					// 可以在这里调用 renewToken 接口来更新 token
					let params = {
						userId: this.userInfo.id
					}
					getZimToken(params).then(response => {

						var token = response.data.data
						zim.renewToken(token)
							.then(function({
								token
							}) {
								// 更新成功
								//console.log("更新成功")
							})
							.catch(function(err) {
								// 更新失败
							})
					})
					// 新 token 生成可以参考上文
					user.getZimToken(this.userInfo.id).then(response => {
						var token = response.data.data
						zim.renewToken(token)
							.then(function({
								token
							}) {
								// 更新成功
								//console.log("更新成功")
							})
							.catch(function(err) {
								// 更新失败
							})
					})
				});
			},


			//
			send() {

				let that = this
				console.log("发送", that.toUid)
				var toConversationID = that.toUid; // 对方 userID
				var conversationType = 0; // 会话类型，取值为 单聊:0, 房间:1, 群组:2
				var config = {
					priority: 1, // 设置消息优先级，取值为 低：1（默认），中：2，高：3
				};

				var messageTextObj = {
					type: 1,
					message: that.recordInput,
					extendedData: '1'
				};
				var notification = {
					onMessageAttached: function(message) {
						// todo: Loading
					}
				}

				zim.sendMessage(messageTextObj, toConversationID, conversationType, config, notification)
					.then(function({
						message
					}) {
						console.log('发送成功')
						// 发送成功
						let MessageDTO = {}

						MessageDTO.sendId = message.senderUserID
						MessageDTO.acceptId = message.conversationID
						MessageDTO.content = message.message
						//往数据库中添加数据
						addChatRecord(MessageDTO).then(res => {
							let messageData = {}
							messageData.sendId = message.senderUserID
							messageData.avatar = that.userInfo.avatar
							messageData.content = message.message
							that.dataList.push(messageData)
							that.recordInput = ''
							that.scrollTop = that.scrollTop + that.itemAverageHeight;
						})

					})
					.catch(function(err) {
						// 发送失败
						//console.log('发送失败', err)
					});
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
			}
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
			min-height: 60rpx;
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
	}
</style>