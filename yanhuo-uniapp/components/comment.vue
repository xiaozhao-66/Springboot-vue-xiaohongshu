<template>
	<view class="container">
		<ul>
			<li v-for="(commentOne, index ) in dataList" :key="index" :id="'two-' + commentOne.id"
				:style="bgcolorId == commentOne.id ? 'background-color:#f2f2f2' : 'background-color:#fff'">
				<view class="item">
					<view class="comment-one">
						<view class="comment-item">

							<view class="item-top">
								<view class="left">
									<image :src="commentOne.avatar" mode="aspectFill"
										@click="getUserInfo(commentOne.uid)" />
									<view class="username">{{ commentOne.username }}</view>
									<tui-tag type="gray" shape="circle" margin="5rpx" padding="10rpx" size="18rpx"
										v-if="commentOne.uid == currentUid">作者</tui-tag>

								</view>

								<view class="right">
									<tui-icon v-if="commentOne.isAgree" name="like-fill" size="15"
										@click="cancelAgree(commentOne,index,null)"></tui-icon>
									<tui-icon v-else @click="addAgree(commentOne, index ,null)" name="like"
										size="15"></tui-icon>
									<view class="ziti">{{ commentOne.count }}</view>
								</view>
							</view>

							<view class="item-main" @click="commentOperate(commentOne,index,null)"
								v-if="commentOne.uid == uid">
								{{ commentOne.content }}<span class="time">{{ commentOne.time }}</span>
							</view>

							<view class="item-main" @click="clickItem(commentOne, index)" v-else>
								{{ commentOne.content }}<span class="time">{{ commentOne.time }}</span>
							</view>
						</view>
					</view>

					<view v-if="commentOne.twoNums>0">
						<view class="comment-two" v-for="(commentTwo, index2) in  commentOne.childrenComments"
							:key="index2" :id="'two-' + commentTwo.id"
							:style="bgcolorId == commentTwo.id ? 'background-color:#f2f2f2' : 'background-color:#fff'">
							<view class="comment-item">
								<view class="item-top">
									<view class="left">
										<image :src="commentTwo.avatar" mode="aspectFill"
											@click="getUserInfo(commentTwo.uid)" />
										<view class="username">{{ commentTwo.username }}</view>
										<tui-tag type="gray" shape="circle" margin="5rpx" padding="10rpx" size="18rpx"
											v-if="commentTwo.uid == currentUid">作者</tui-tag>
									</view>

									<view class="right">
										<tui-icon v-if="commentTwo.isAgree" name="like-fill" size="15"
											@click="cancelAgree(commentTwo,index,index2)"></tui-icon>
										<tui-icon v-else @click="addAgree(commentTwo, index , index2)" name="like"
											size="15"></tui-icon>
										<view class="ziti">{{ commentTwo.count }}</view>
									</view>
								</view>

								<view class="item-main" @click="commentOperate(commentTwo,index,index2)"
									v-if="commentTwo.uid == uid">
									回复<span class="reply">{{ commentTwo.replyName }}</span>:{{ commentTwo.content }}
									<span class="time">{{ commentTwo.time }}</span>
								</view>

								<view class="item-main" @click="clickItem(commentTwo, index)" v-else>
									回复<span class="reply">{{ commentTwo.replyName }}</span>:{{ commentTwo.content }}
									<span class="time">{{ commentTwo.time }}</span>
								</view>
							</view>
						</view>

						<view class="other">
							<view @click="loadMore(commentOne.id,index)"
								v-if="commentOne.twoNums>1&&commentOne.childrenComments.length<commentOne.twoNums">
								点击加载更多</view>
						</view>
					</view>

				</view>
			</li>
		</ul>

		<tui-actionsheet :show="showActionSheet" :item-list="itemList" @click="itemClick" @cancel="closeActionSheet">
		</tui-actionsheet>

		<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
	</view>
</template>

<script>
	import {
		getAllComment,
		getAllTwoCommentByOneId,
		delComment,
		scrollComment
	} from "@/api/comment.js"
	import {
		timeAgo
	} from "../utils/webUtils";

	export default {
		name: "comment",
		props: {
			mid: String,
			seed: Number,
			val: Number,
			commentInfo: Object,
			currentUid: String,
			//滑动评论
			comArr: Array,
		},
		data() {
			return {
				dataList: [],
				page1: 1,
				limit1: 6,
				total1: 0,
				page2: 1,
				limit2: 4,
				total2: 0,

				isEnd: false, //是否到底底部了

				twoMap: {},

				//点击索引
				index: -1,
				index2: -1,

				//添加的记录id用户筛选
				commentIds: [],

				//滚动评论
				bgcolorId: '',
				isScroll: false,
				scrollTwoData: [],

				search: false,


				showActionSheet: false,

				itemList: [{
					text: "回复",
					color: "#2B2B2B"
				}, {
					text: "删除",
					color: "#ff0000"
				}],
				clickComment: {},

			};
		},
		watch: {

			seed(newVal, oldVal) {

				//这是trendComment组件传递的数据，如果是mian页面的评论不需要
				this.page1 = 1
				this.dataList = []
				this.getAllComment()
			},
			val(newVal, oldVal) {
				this.page1 = this.page1 + 1
				this.loadData()
			},
			commentInfo(newVal, oldVal) {
				//将comment添加到数组
				this.addComment(newVal)
			},
		},

		mounted() {
			this.twoMap = new Map()
			this.uid = uni.getStorageSync("userInfo").id

			if (this.comArr[0] != null) {
				this.scrollComment(this.comArr[0])
			} else {
				this.getAllComment()
			}

		},

		created() {

		},

		methods: {

			getUserInfo(uid) {
				uni.navigateTo({
					url: "/pages/otherUser/otherUser?uid=" + uid
				})
			},

			getAllComment() {
				console.log('得到所有评论')

				let params = {
					mid: this.mid,
					uid: this.uid
				}

				getAllComment(this.page1, this.limit1, params).then(res => {

					res.data.records.forEach(item => {
						item.time = timeAgo(new Date(item.createDate))

						if (item.twoNums > 0) {
							item.childrenComments[0].time = timeAgo(new Date(item.childrenComments[0]
								.createDate))
							this.commentIds.push(item.childrenComments[0].id)
						}

						this.dataList.push(item)
					})

					this.total1 = res.data.total

					for (var i = 0; i < this.total1; i++) {
						this.twoMap.set(i, 0);
					}
				})
			},


			loadData() {

				if (this.dataList.length >= this.total1) {
					this.isEnd = true
					return
				}

				let params = {
					mid: this.mid,
					uid: this.uid
				}
				getAllComment(this.page1, this.limit1, params).then(res => {

					res.data.records.forEach(item => {
						item.time = timeAgo(new Date(item.createDate))

						if (item.twoNums > 0) {
							item.childrenComments[0].time = timeAgo(new Date(item.childrenComments[0]
								.createDate))
							this.commentIds.push(item.childrenComments[0].id)
						}

						this.dataList.push(item)
					})
				})
			},

			loadMore(cid, index) {

				this.page2 = this.twoMap.get(index)

				this.page2 = this.page2 + 1

				this.twoMap.set(index, this.page2)

				let params = {
					id: cid,
					uid: this.uid
				}


				getAllTwoCommentByOneId(this.page2, this.limit2, params).then(res => {

					if (this.page2 == 1) {
						let twoData = []
						res.data.records.splice(0, 1)
						twoData = res.data.records

						twoData.forEach(item => {

							if (!this.commentIds.includes(item.id)) {
								item.time = timeAgo(new Date(item.createDate))
								this.dataList[index].childrenComments.push(item)
								this.commentIds.push(item.id)
							}
						})

					} else {

						res.data.records.forEach(item => {

							if (!this.commentIds.includes(item.id)) {

								item.time = timeAgo(new Date(item.createDate))
								this.dataList[index].childrenComments.push(item)
								this.commentIds.push(item.id)
							}
						})
					}
				})

			},

			clickItem(comment, index) {
				this.$emit('getComment', comment);
				this.index = index
				this.T = true
			},

			addComment(comment) {

				comment.count = 0
				comment.time = "刚刚"
				if (comment.pid == 0) {
					//添加一级评论
					comment.childrenComments = []
					this.dataList.unshift(comment)
				} else {
					if (this.index >= 0) {
						this.dataList[this.index].twoNums = this.dataList[this.index].twoNums * 1 + 1
						this.dataList[this.index].childrenComments.unshift(comment)
					}
				}
			},

			//隐藏组件
			closeActionSheet() {
				this.showActionSheet = false
				this.$emit('delComment', false);
			},
			//调用此方法显示组件
			commentOperate(comment, index, index2) {
				this.clickComment = comment;
				this.index = index;
				this.index2 = index2
				this.showActionSheet = true;
				this.$emit('delComment', true);
			},
			itemClick(e) {

				let index = e.index;
				this.closeActionSheet();
				if (index == 0) {
					//点击回复
					this.clickItem(this.clickComment, this.index)
				} else {
					//点击删除
					if (this.clickComment.pid == 0) {
						this.dataList.splice(this.index, 1)
					} else {
						this.dataList[this.index].twoNums = this.dataList[this.index].twoNums * 1 - 1
						this.dataList[this.index].childrenComments.splice(this.index2, 1)
					}
					let params = {
						id: this.clickComment.id
					}
					delComment(params).then()
				}
			},

			addAgree(comment, index, index2) {

				if (comment.pid == '0') {
					this.dataList[index].count = this.dataList[index].count * 1 + 1
					this.dataList[index].isAgree = true
				} else {
					this.dataList[index].childrenComments[index2].count = this.dataList[index].childrenComments[index2]
						.count * 1 + 1
					this.dataList[index].childrenComments[index2].isAgree = true
				}
				this.$emit('addAgree', comment);
			},


			cancelAgree(comment, index, index2) {
				if (comment.pid == '0') {
					this.dataList[index].count = this.dataList[index].count * 1 - 1
					this.dataList[index].isAgree = false
				} else {
					this.dataList[index].childrenComments[index2].count = this.dataList[index].childrenComments[index2]
						.count * 1 - 1
					this.dataList[index].childrenComments[index2].isAgree = false
				}
				this.$emit('cancelAgreeComment', comment);
			},

			scrollComment(cid) {
				console.log("滚动评论")
				let params = {
					id: cid,
					mid: this.mid,
					uid: this.uid
				}

				if (this.comArr[1] == 0) {

					scrollComment(params).then(res => {

						res.data.records.forEach(item => {
							item.time = timeAgo(new Date(item.createDate))

							if (item.childrenComments.length)

								if (item.twoNums > 0) {
									item.childrenComments[0].time = timeAgo(new Date(item.childrenComments[
											0]
										.createDate))
									this.commentIds.push(item.childrenComments[0].id)
								}

							this.dataList.push(item)
						})

						this.total1 = res.data.total

						for (var i = 0; i < this.total1; i++) {
							this.twoMap.set(i, 0);
						}

						this.page1 = res.data.page1

					})
				} else {
					scrollComment(params).then(res => {
						const data = res.data.records
						let index = 0

						for (var i = 0; i < data.length; i++) {
							data[i].time = timeAgo(new Date(data[i].createDate))
							if (data[i].childrenComments != null) {
								let childs = data[i].childrenComments

								for (var j = 0; j < childs.length; j++) {
									childs[j].time = timeAgo(new Date(childs[j].createDate))
									if (childs[j].id == cid) {
										index = i;
									}
								}
							}

							this.dataList.push(data[i])
						}

						this.total1 = res.data.total

						for (var i = 0; i < this.total1; i++) {
							this.twoMap.set(i, 0);
						}

						this.page1 = res.data.page1
						this.page2 = res.data.page2
						this.twoMap.set(index, this.page2)
					})
				}

				this.scroll(cid)

			},

			//滚动到当前评论
			scroll(commentId) {

				this.bgcolorId = commentId

				setTimeout(e => {

					const query = uni.createSelectorQuery().in(this)

					query.select(`#two-` + commentId).boundingClientRect(data => {

						this.$emit('scrollTop', data.top);

					}).exec();
				}, 500)

				setTimeout(() => {
					this.bgcolorId = ''
				}, 2000)


			},

		},

	}
</script>

<style scoped>
	.container {
		padding: 0rpx 40rpx 60rpx 40rpx;
	}

	ul {
		padding: 0rpx;
	}

	li {
		list-style: none;
		margin-top: 20px;
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

	.right {
		display: flex;
		align-items: center;
	}

	.ziti {
		color: #7d7d7d;
		font-size: 28rpx;
	}


	.comment-two {
		margin-top: 10rpx;
		margin-left: 80rpx;
	}

	.other {
		margin-left: 160rpx;
		color: #636363;
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
</style>