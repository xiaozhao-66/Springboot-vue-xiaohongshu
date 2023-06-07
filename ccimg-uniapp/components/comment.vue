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
									<image :src="commentOne.avatar" mode="aspectFill" />
									<view class="username">{{ commentOne.username }}</view>
								</view>

								<view class="right">

									<tui-icon v-if="commentOne.isAgree" name="like-fill" size="15" @click="cancelAgree(commentOne,index)"></tui-icon>
									<tui-icon v-else @click="addAgree(commentOne, index)" name="like" size="15"></tui-icon>
									<view class="ziti">{{ commentOne.count }}</view>
								</view>
							</view>

							<view class="item-main" @click="clickItem(commentOne, index)">
								{{ commentOne.content }}<span class="time">{{ commentOne.time }}</span>
							</view>

							<view class="other" v-if="commentOne.children">
								<view @click="loadTwoData(commentOne.id)" v-if="pid != commentOne.id" >点击加载更多</view>
							</view>
						</view>
					</view>

					<view v-if="commentOne.children && !isScroll">
						<view class="comment-two" v-for="(commentTwo, index2) in twoData " :key="index2"
							v-if="pid == commentOne.id">
							<view class="comment-item">
								<view class="item-top">
									<view class="left">
										<image :src="commentTwo.avatar" />
										<view class="username">{{ commentTwo.username }}</view>
									</view>

									<view class="right">
										<tui-icon v-if="commentTwo.isAgree" name="like-fill" size="15"  @click="cancelAgree(commentTwo,index2)"></tui-icon>
										<tui-icon v-else @click="addAgree(commentTwo, index2)" name="like"
											size="15"></tui-icon>
										<view class="ziti">{{ commentTwo.count }}</view>

									</view>
								</view>

								<view class="item-main" @click="clickItem(commentTwo, index2)">
									回复<span class="reply">{{ commentTwo.replyName }}</span>:{{ commentTwo.content }} <span
										class="time">{{ commentTwo.time }}</span>
								</view>
							</view>
						</view>

						<view class="other" v-if="pid == commentOne.id">
							<view @click="showMore(commentOne.id)" v-if="T && twoData.length < total2">点击加载更多</view>
						</view>

					</view>

					<!-- 滑动到指定评论 -->
					<view v-if="commentOne.children && isScroll">
						<view class="comment-two" v-for="(commentTwo, index2) in twoData2 " :key="index2"
							v-if="pid == commentOne.id" :id="'two-' + commentTwo.id"
							:style="bgcolorId == commentTwo.id ? 'background-color:#f2f2f2' : 'background-color:#fff'">
							<view class="comment-item">
								<view class="item-top">
									<view class="left">
										<image :src="commentTwo.avatar" />
										<view class="username">{{ commentTwo.username }}</view>
									</view>

									<view class="right">
										<tui-icon v-if="commentTwo.isAgree" name="like-fill" size="15"  @click="cancelAgree(commentTwo,index2)"></tui-icon>
										<tui-icon v-else @click="addAgree(commentTwo, index2)" name="like"
											size="15"></tui-icon>
										<view class="ziti">{{ commentTwo.count }}</view>
										
									</view>
								</view>

								<view class="item-main" @click="clickItem(commentTwo, index2)">
									回复<span class="reply">{{ commentTwo.replyName }}</span>:{{ commentTwo.content }} <span
										class="time">{{ commentTwo.time }}</span>
								</view>
							</view>
						</view>
					</view>
				</view>

			</li>
		</ul>
		<view class="loadStyle" v-if="isEnd&&dataList.length>0">我也是有底线的~</view>
	</view>
</template>

<script>
import { getAllOneCommentByImgId, getAllTwoCommentByOneId, getAllTwoComment, getComment } from "@/api/comment.js"

export default {
	name: "comment",
	props: {
		mid: String,
		seed: Number,
		page: Number,
		parentId: String,
		comArr: Array,
		commentInfo: Object,
	},
	data() {
		return {
			page1: 1,
			limit1: 6,
			total1: 0,
			page2: 1,
			limit2: 4,
			total2: 0,
			dataList: [],
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			pid: -1,
			twoData: [],
			twoData2: [],
			T: true,
			isScroll: true,
			bgcolorId: '',
			uid: '',
			//点击索引
			index: -1,

		};
	},
	watch: {
		seed(newVal, oldVal) {

			//这是trendComment组件传递的数据，如果是mian页面的评论不需要
			this.page1 = 1
			this.getAllOneCommentByImgId()
			this.page2 = 1
			this.loadTwoData(this.parentId)
			this.isScroll = false
		},
		page(newVal, oldVal) {
			this.page1 = newVal
			this.loadData()
		},
		commentInfo(newVal, oldVal) {

			//将comment添加到数组
			this.addComment(newVal)
		},

	},
	created() {
		this.uid = uni.getStorageSync("userInfo").id
		this.getAllOneCommentByImgId()
	},
	mounted() {

		if (this.comArr[1] == 0) {
			this.scrollComment(this.comArr[0])
		} else {
			//先得到当前一级评论，再滑倒二级评论
			this.getScrollTwoComment(this.comArr[0])
		}

	},

	methods: {
		getAllOneCommentByImgId() {
			let params = {
				mid: this.mid,
				uid: this.uid
			}
			getAllOneCommentByImgId(this.page1, this.limit1, params).then(res => {

				this.dataList = res.data.records
				this.total = res.data.total

			})
		},
		getScrollTwoComment(id) {
			let params = {
				id: id
			}
			getComment(params).then(res => {
				//得到当前评论
				let comment = res.data
				//进行滑动
				this.getAllTwoComment(comment.pid, id)
			})
		},
		clickItem(comment, index) {
			this.$emit('getComment', comment);
			this.index = index
			this.T = true
		},

		addComment(comment) {
			comment.count = 0
			if (comment.pid == 0) {
				//添加一级评论
				this.dataList.unshift(comment)

			} else {
				if (this.index >= 0) {
					this.dataList[this.index].children = true
					this.twoData.unshift(comment)
					this.twoData2.unshift(comment)
				}
			}
		},

		addAgree(comment, index) {

			if (comment.pid == '0') {
				this.dataList[index].count = this.dataList[index].count * 1 + 1
				this.dataList[index].isAgree = true
			} else {

				if (this.twoData.length > 0) {
					this.twoData[index].count = this.twoData[index].count * 1 + 1
					this.twoData[index].isAgree = true
				} else {
					this.twoData2[index].count = this.twoData2[index].count * 1 + 1
					this.twoData2[index].isAgree = true
				}
			}
			this.$emit('addAgree', comment);
		},
		
		
		cancelAgree(comment, index){
			if (comment.pid == '0') {
				this.dataList[index].count = this.dataList[index].count * 1 - 1
				this.dataList[index].isAgree = false
			} else {
			
				if (this.twoData.length > 0) {
					this.twoData[index].count = this.twoData[index].count * 1 - 1
					this.twoData[index].isAgree = false
				} else {
					this.twoData2[index].count = this.twoData2[index].count * 1 - 1
					this.twoData2[index].isAgree = false
				}
			}
			this.$emit('cancelAgreeComment', comment);
		},

		showMore(pid) {

			if (this.twoData.length >= this.total2) {
				this.T = false
				return
			}
			this.page2 = this.page2 + 1
			let params = {
				id: pid,
				uid: this.uid
			}
			getAllTwoCommentByOneId(this.page2, this.limit2, params).then(res => {
				this.twoData.push(...res.data.records)

			})

		},

		loadData() {

			if (this.dataList.length >= this.total) {
				this.isEnd = true
				return
			}

			let params = {
				mid: this.mid,
				uid: this.uid
			}
			getAllOneCommentByImgId(this.page1, this.limit1, params).then(res => {

				this.dataList.push(...res.data.records)
			})


		},

		loadTwoData(id) {
			if (this.pid != id) {
				this.page2 = 1
				this.T = true
				this.isScroll = false
			}
			this.pid = id
			let params = {
				id: id,
				uid: this.uid
			}

			getAllTwoCommentByOneId(this.page2, this.limit2, params).then(res => {
				this.twoData = res.data.records
				this.total2 = res.data.total
			})

		},

		getAllTwoComment(pid, cid) {

			this.pid = pid
			let params = {
				id: pid,
				uid: this.uid
			}
			getAllTwoComment(params).then(res => {
				this.twoData2 = res.data
				this.scrollComment(cid)
			})
		},

		//滚动到当前评论
		scrollComment(commentId) {

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

.right{
	display: flex;
	align-items: center;
}
.ziti{
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