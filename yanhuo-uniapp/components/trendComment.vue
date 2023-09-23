<template>
	<view class="container">
		<tui-bottom-popup backgroundColor="#ffffff" :zIndex="1002" :maskZIndex="1001" :height="1000" :show="popupShow"
			@close="popup">

			<scroll-view scroll-y class="page" @scrolltolower="loadData" :scroll-top="scrollTop" @scroll="scroll">

				<comment :mid='mid' @getComment="getComment" @addAgree="addAgree"
					@cancelAgreeComment="cancelAgreeComment" :seed='seed' :page="page" :comArr="comArr">
				</comment>

			</scroll-view>

			<view class="fotter" :style="{ 'bottom': bottomVal }">

				<input :placeholder="placeholder" class="input-comment" :focus="isFocus" v-model="content"
					:adjust-position="false" @focus="inputBindFocus" @blur="inputBindBlur" />

				<view class="icon">
					<view>
						<tui-icon name="imface" size="20"></tui-icon>
					</view>
					<view>
						<tui-button type="danger" height="60rpx" width="100rpx" @click="addComment">发送</tui-button>
					</view>
				</view>
			</view>

		</tui-bottom-popup>
	</view>
</template>

<script>
	import {
		addComment
	} from "@/api/comment.js"
	import {
		agree,
		cancelAgree
	} from "@/api/agreeCollect.js"
	import Comment from "@/components/comment.vue"
	export default {
		name: "trendComment",
		components: {
			Comment
		},
		props: {
			popupShow: Boolean,
			mid: String,
			seed: Number
		},
		data() {
			return {
				dataList: [],
				comArr: [],
				page: 1,
				limit: 8,
				total: 0,
				bottomVal: '',
				content: '',
				isFocus: false,
				placeholder: '请输入内容~',
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				comment: {},

			};
		},


		methods: {
			popup() {
				this.popupShow = !this.popupShow

				if (!this.popupShow) {
					this.page = 1
					this.seed = Math.random()
					uni.showTabBar()
					//滚动到最顶部
					this.scrollTop = this.old.scrollTop
					this.$nextTick(function() {
						this.scrollTop = 0
					});

				}
				this.$emit('popup', this.popupShow);
			},

			scroll(e) {
				this.old.scrollTop = e.detail.scrollTop
			},

			loadData() {
				this.page = this.page + 1
			},


			//------------------------输入框部分

			inputBindFocus(e) {
				// 获取手机键盘的高度，赋值给input 所在盒子的 bottom 值
				// 注意!!! 这里的 px 至关重要!!! 我搜到的很多解决方案都没有说这里要添加 px
				this.bottomVal = e.detail.height + 'px'
				this.showChat = true

				if (this.comment.id != null) {
					this.placeholder = '回复' + this.comment.username + ':'
				}

			},

			inputBindBlur() {
				// input 失去焦点，键盘隐藏，设置 input 所在盒子的 bottom 值为0

				setTimeout(() => {
					this.bottomVal = 0 + 'px'
					this.showChat = false
					this.isFocus = false
				}, 100)

				this.placeholder = '请输入内容~'
			},

			getComment(comment) {

				this.isFocus = true
				this.comment = comment
			},

			addComment() {


				let userInfo = uni.getStorageSync("userInfo")

				if (userInfo == null || userInfo == undefined) {
					return
				} else {

					let commentInfo = {}
					commentInfo.content = this.content
					commentInfo.uid = userInfo.id
					commentInfo.mid = this.mid
					
					//添加一级评论
					if (this.comment.id == null) {
						commentInfo.pid = 0
						commentInfo.replyId = 0
						commentInfo.replyUid = 0
						commentInfo.level = 1
					} else {

						commentInfo.replyName = this.comment.username
						commentInfo.level = 2
						commentInfo.replyId = this.comment.id
						commentInfo.replyUid = this.comment.uid
						//添加二级评论
						if (this.comment.pid == 0) {
							commentInfo.pid = this.comment.id
						} else {
							commentInfo.pid = this.comment.pid

						}
					}

					addComment(commentInfo).then(res => {
						this.content = ''
						this.seed = Math.random()
						this.bottomVal = 0 + 'px'
						this.showChat = false
						this.isFocus = false
						this.placeholder = '请输入内容~'
						this.parentId = commentInfo.pid
						this.comment = {}
					})
				}
			},

			addAgree(comment) {

				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.type = 0
				data.agreeCollectId = comment.id
				data.agreeCollectUid = comment.uid
				agree(data).then()
			},

			cancelAgreeComment(comment) {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.agreeCollectId = comment.id
				data.agreeCollectUid = comment.uid
				data.type = 0
				cancelAgree(data).then()
			},

		}
	}
</script>

<style scoped>
	ul {
		padding: 40rpx;
	}

	li {
		list-style: none;
		margin-top: 20px;
	}

	.page {
		height: 70vh;
	}

	.item-top {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.left {
		display: flex;
		align-items: center;

	}

	.left image {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
	}



	.comment-two {
		margin-top: 10rpx;
		margin-left: 80rpx;
	}

	.other {
		margin-left: 160rpx;
		color: #5555ff;
		font-size: 28rpx;

	}

	.reply {
		color: #939393;
	}

	.time {
		color: #939393;
		font-size: 18rpx;
	}

	.item-main {

		margin-left: 80rpx;
		margin-right: 80rpx;
		font-size: 28rpx;
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
		word-break: break-all
	}

	.loadStyle {
		margin-top: 20rpx;
		width: 100%;
		height: 60rpx;
		text-align: center;
		color: #bfbfbf;
		padding-bottom: 200rpx;

	}

	.fotter {
		position: fixed;
		display: flex;
		align-items: center;
		left: 0px;
		bottom: 0px;
		width: 100%;
		height: 50px;
		background-color: #fff;
		z-index: 999;
	}

	.fotter .input-comment {

		margin-left: 10px;
		margin-right: 10px;
		width: 450rpx;
		height: 60rpx;
		background-color: #f1f1f1;
		padding-left: 10px;
		bottom: 0;
	}

	.icon {
		width: 180rpx;

		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>