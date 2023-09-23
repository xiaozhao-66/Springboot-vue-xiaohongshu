<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
			<view class="nav">
				<view @click="back"><tui-icon name="arrowleft" size="24"></tui-icon></view>
				<view class="center">
					<view @click="getUserInfo(imgInfo.userId)">
						<image class="avatar" :src="imgInfo.avatar" mode="aspectFill" />
					</view>

					<view class="username">{{ imgInfo.username }}</view>

					<view v-if="!isCurrentUser" class="followStatus">
						<view v-if="T">
							<tui-button @click="clearFollow(imgInfo.userId)" type="gray" height="50rpx" :size="22"
								width="100rpx" shape="circle" plain>已关注</tui-button>
						</view>
						<view v-else>
							<tui-button @click="follow(imgInfo.userId)" type="danger" height="50rpx" :size="22"
								width="100rpx" shape="circle" plain>关注</tui-button>
						</view>
					</view>
				</view>
				<tui-icon name="more-fill" size="20" @click="popup"></tui-icon>
			</view>
		</tui-navigation-bar>
		<scroll-view scroll-y class="page" @scrolltolower="loadData" @scroll="scroll" :scroll-top="scrollTop">
			<view class="img-list">
				<!-- 重做图片显示模块 -->
				<view class="img-list-one" v-if="count==1">
					<img :src="imgInfo.imgsUrl[0]" width="100%" @click="previewImgae(0)"></img>
				</view>

				<view class="img-list-two" v-if="count==2">

					<image :src="imgInfo.imgsUrl[0]" mode="widthFix" @click="previewImgae(0)"></image>
					<image :src="imgInfo.imgsUrl[1]" mode="widthFix" @click="previewImgae(1)"></image>
				</view>

				<view class="img-list-five" v-if="count==5">
					<view class="img-list-five-top">
						<image :src="imgInfo.imgsUrl[0]" mode="aspectFill" @click="previewImgae(0)"
							style="width: 50%;height: 600rpx;" class="fadeImg"></image>
						<image :src="imgInfo.imgsUrl[1]" mode="aspectFill" @click="previewImgae(1)"
							style="width: 50%;height: 600rpx;" class="fadeImg"></image>
					</view>
					<view class='img-list-five-down'>
						<image :src="imgInfo.imgsUrl[2]" mode="aspectFill" @click="previewImgae(2)"></image>
						<image :src="imgInfo.imgsUrl[3]" mode="aspectFill" @click="previewImgae(3)"></image>
						<image :src="imgInfo.imgsUrl[4]" mode="aspectFill" @click="previewImgae(4)"></image>
					</view>
				</view>


				<tui-grid :unlined="true" v-if="count==3||count==4||count==6||count==8">
					<block v-for="(item, index) in imgInfo.imgsUrl" :key="index">
						<tui-grid-item :cell="2" :border="false" :bottomLine="false">
							<view class="card">
								<image :src="item" mode="aspectFill" @click="previewImgae(index)"
									style="width: 100%;height: 600rpx;" class="fadeImg"></image>
							</view>
						</tui-grid-item>
					</block>
				</tui-grid>


				<view v-if="count==7">
					<view class="img-list-five-top">
						<image :src="imgInfo.imgsUrl[0]" mode="aspectFill" @click="previewImgae(0)"
							style="height: 500rpx;"></image>
						<image :src="imgInfo.imgsUrl[1]" mode="aspectFill" @click="previewImgae(1)"
							style="height: 500rpx;"></image>
					</view>
					<view class="img-list-five-top">
						<image :src="imgInfo.imgsUrl[2]" mode="aspectFill" @click="previewImgae(2)"
							style="height: 500rpx;"></image>
						<image :src="imgInfo.imgsUrl[3]" mode="aspectFill" @click="previewImgae(3)"
							style="height: 500rpx;"></image>
					</view>
					<view class='img-list-five-down'>
						<image :src="imgInfo.imgsUrl[4]" mode="aspectFill" @click="previewImgae(4)"></image>
						<image :src="imgInfo.imgsUrl[5]" mode="aspectFill" @click="previewImgae(5)"></image>
						<image :src="imgInfo.imgsUrl[6]" mode="aspectFill" @click="previewImgae(6)"></image>
					</view>
				</view>


				<tui-grid :unlined="true" v-if="count==9">
					<block v-for="(item, index) in imgInfo.imgsUrl" :key="index">
						<tui-grid-item :cell="3" :border="false" :bottomLine="false">
							<view class="card">
								<image :src="item" @click="previewImgae(index)" mode="aspectFill"
									style="width: 300rpx;height: 300rpx;" class="fadeImg" />
							</view>
						</tui-grid-item>
					</block>
				</tui-grid>



			</view>
			<view class="cont">
				<view class="img-content">{{ imgInfo.content }}</view>
				<view class="time">{{ imgInfo.time }}|{{ imgInfo.viewCount }}浏览</view>
			</view>
			<view class="relation">
				<view class="r-all">
					<h4>所属专辑</h4>
				</view>

				<view class="r-list">
					<ul>
						<li class="r-item" @click='toAlbum(album.id)'>
							<image :src="album.cover" mode="aspectFill" />
							<view class="item-content">
								<h4>{{ album.name }}</h4>
								<p>{{ album.imgCount }}张图片|{{ album.collectionCount }}人收藏</p>
							</view>
						</li>
					</ul>
				</view>
				<view class="r-tag">
					<view v-for="(tag, index) in imgInfo.tagList" :key="index">
						<tui-tag type="gray" shape="circle" margin="10rpx" size="24rpx"
							@click="toTagList(tag.id)">{{ tag.name }}</tui-tag>
					</view>
				</view>
			</view>

			<view class="comment" v-if='imgInfo.commentCount>0'>
				<view style="font-weight: bold;margin-left: 40rpx;">所有评论</view>
				<!--  scrollTop，comArr实现评论跳转，-->
				<comment :mid='imgInfo.id' @getComment="getComment" @addAgree="addAgree" @scrollTop="getScrollTop"
					@cancelAgreeComment="cancelAgreeComment" :val="val" :parentId="parentId" :comArr='comArr'
					:commentInfo='commentInfo' :currentUid='imgInfo.userId' @delComment='delComment'>
				</comment>
				<!-- 评论功能 -->
			</view>
			<view v-else style="height: 200rpx"></view>

		</scroll-view>

		<!-- 底部输入框 -->
		<view :class="fn?'fotter fotter-none':'fotter'">

			<view v-if="!useful_flag" class="fotter-info">
				<view class="fotter-content" @click="active">{{ placeholder }}</view>
				<tui-icon name="star-fill" size="20" v-if="isCollection" @click="cancelCollect()"></tui-icon>
				<tui-icon name="star" size="20" v-else @click="saveImgToAlbum()"></tui-icon>
				<view class="fotter-c-a">{{ imgInfo.collectionCount }}</view>

				<tui-icon name="agree-fill" size="20" v-if="userIsAgree" @click="cancelAgreeImg()"></tui-icon>
				<tui-icon name="agree" size="20" v-else @click="agreeImg"></tui-icon>
				<view class="fotter-c-a">{{ imgInfo.agreeCount }}</view>
			</view>

			<!-- 新增表情发送 -->
			<input-emoji v-else :useful_flag="useful_flag" :cursor="cursor" @addCommentWithEmoji="addCommentWithEmoji"
				:placeholder="placeholder">
			</input-emoji>
		</view>
		<!-- 底部输入框完成 -->

		<!--底部分享弹窗-->
		<tui-bottom-popup backgroundColor="#f6f6f6" :zIndex="1002" :maskZIndex="1001" :show="popupShow" @close="popup">
			<view class="tui-share">
				<view class="tui-share-title">操作</view>

				<view class="tui-share-top" v-if="isCurrentUser">

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
						:class="[shareList[2].operate2.length - 1 === index ? 'tui-item-last' : '']"
						v-for="(item, index) in shareList[2].operate2" :key="index" @tap="popup">
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
						<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150"
							@click="share(index)">
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


		<!-- 批量保存 -->
		<tui-bottom-popup backgroundColor="#ffffff" :zIndex="1002" :maskZIndex="1001" :show="batchSaveShow"
			@close="batchShow">
			<view class="tui-share">
				<view class="tui-share-title popup-top">

					<view @click="batchShow">取消</view>
					<view style="font-size: 28rpx; color: black; font-style: inherit;">选择需要保存的图片</view>
					<view class="selectAll">
						<view class="all-icon">
							<tui-icon name="circle-fill" color="#ff626c"
								v-if="imgInfo.imgsUrl!=null&&imgInfo.imgsUrl.length == selectImgs.length" size="18"
								@click="cancalAll()"></tui-icon>
							<tui-icon name="circle" size="18" v-else @click="selectAll()"></tui-icon>
						</view>
						<view class="all-title">
							<view v-if="imgInfo.imgsUrl!=null&&imgInfo.imgsUrl.length == selectImgs.length" size="18"
								@click="cancalAll()" style="color:#ff626c ;">全部</view>
							<view v-else @click="selectAll()">全部</view>
						</view>
					</view>
				</view>


				<view class="pictures">

					<tui-grid :unlined="true">
						<block v-for="(item, index) in imgInfo.imgsUrl" :key="index">
							<tui-grid-item :cell="3" :border="false" :bottomLine="false">
								<view class="card">

									<view class="card-circle" v-if="!selectImgs.includes(index)"
										@click="delAffirm(index)">
										<tui-icon name="circle" size="22" @click="delCancel(index)">
										</tui-icon>
									</view>
									<view class="card-circle" v-if="selectImgs.includes(index)"><tui-icon
											name="circle-fill" color="#ff626c" size="22"
											@click="delCancel(index)"></tui-icon>
									</view>

									<image :src="item" @click="previewImgae(index)" mode="aspectFill" />
								</view>
							</tui-grid-item>
						</block>
					</tui-grid>
				</view>


				<view class="tui-btn-cancle" style="background-color: #fff;">
					<tui-button type="danger" @click="batchSave()" shape="circle" width='560rpx' height="80rpx"
						:size='28'>批量下载</tui-button>
				</view>
			</view>
		</tui-bottom-popup>
		<!-- 批量保存 -->
		<!-- 蒙板 -->
		<view class="shade" v-show="show" @click="close"></view>

	</view>
</template>

<script>
	import Comment from "@/components/comment.vue"
	import InputEmoji from "@/components/inputEmoji.vue"
	import {
		getOne,
		deleteImgs
	} from '@/api/imgDetail.js'
	import {
		addComment
	} from "@/api/comment.js"
	import {
		isCollectImgToAlbum
	} from "@/api/album.js"
	import {
		agree,
		isAgree,
		cancelAgree,
		cancelCollection
	} from "@/api/agreeCollect.js"
	import {
		isFollow,
		followUser,
		clearFollow
	} from "@/api/follow.js"
	import {
		appConfig
	} from '@/config/config.js'
	import {
		timeAgo
	} from "@/utils/webUtils.js"
	export default {
		components: {
			Comment,
			InputEmoji
		},
		data() {
			return {
				imgInfo: {},
				count: 0,
				album: {},
				content: '',
				comment: {},

				placeholder: '请输入内容~',
				val: 0,
				parentId: '',
				comArr: [],
				isCollection: false,
				userIsAgree: false,
				T: false,
				isCurrentUser: false,

				//
				useful_flag: false,
				cursor: false,
				show: false,

				fn: false,

				//
				commentInfo: {},

				//滚动
				scrollTop: 0,
				old: {
					scrollTop: 0
				},

				//批量保存
				isshow: false,
				affirmId: '',
				selectImgs: [],

				//实现编辑操作
				popupShow: false,
				batchSaveShow: false,
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
				}, {
					operate: [{
							name: "编辑",
							icon: "edit",
							size: 30
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
							name: "批量保存",
							icon: "picture",
							size: 28
						}
					]
				}, {
					operate2: [{
							name: "批量保存",
							icon: "picture",
							size: 30
						},
						{
							name: "不感兴趣",
							icon: "dissatisfied",
							size: 30
						},
						{
							name: "举报",
							icon: "about",
							size: 30
						},
						{
							name: "社区公约",
							icon: "immore",
							size: 28
						},
					]
				}],
			}
		},
		onLoad(option) {
			this.imgInfo.id = option.mid

			if (option.cid != null) {
				this.comArr[0] = option.cid
				this.comArr[1] = option.rid
			}
		},

		created() {
			this.getOne()
		},
		onBackPress(options) {
			/**
			 * 由于 uni.navigateBack() 同样会触发 onBackPress 函数。
			 * 因此在 onBackPress 中直接调用 uni.navigateBack() 并始终返回 true 会引发死循环。
			 * 此时，需要根据onBackPress的回调对象中的from值来做处理，当来源是'navigateBack'时，返回 false 。
			 */
			if (options.from === 'navigateBack') {
				return false;
			}
			this.back();
			return true;
		},
		methods: {
			active() {

				this.useful_flag = true
				this.cursor = true
				this.show = true
			},

			close() {

				this.useful_flag = false
				this.show = false
				this.placeholder = '请输入内容~'
				this.comment = {}

			},

			addCommentWithEmoji(content) {
				this.useful_flag = false
				this.show = false
				this.content = content
				this.addComment()
			},

			scroll(e) {
				this.old.scrollTop = e.detail.scrollTop
			},

			getScrollTop(top) {
				this.scrollTop = this.old.scrollTop
				this.$nextTick(function() {
					this.scrollTop = top
				});
			},

			delComment(flag) {
				this.fn = flag
			},

			back() {
				//测试返回上一页面传递参数
				//    let currentTab = this.currentTab

				// // 1. 获取当前页面栈实例（此时最后一个元素为当前页）
				//let pages = getCurrentPages()

				// // 2. 上一页面实例
				// // 注意是length长度，所以要想得到上一页面的实例需要 -2
				// // 若要返回上上页面的实例就 -3，以此类推
				// let prevPage = pages[pages.length - 2]

				// // 3. 给上一页面实例绑定getValue()方法和参数（注意是$vm）
				// prevPage.$vm.getValue(currentTab)


				let pages = getCurrentPages()

				//返回上一页面部分
				if (this.isCollection) {
					uni.navigateBack({
						delta: pages.length
					})
				} else {
					uni.navigateBack({
						delta: 1
					})
				}


			},
			//-------------------关注部分-------------------
			isFollow() {
				let uid = uni.getStorageSync("userInfo").id

				let fid = this.imgInfo.userId
				let params = {
					uid: uid,
					fid: fid
				}
				isFollow(params).then(res => {

					if (res.data) {
						this.T = true
					} else {
						this.T = false
					}
				})
			},
			//关注用户
			follow(fid) {
				let curUser = uni.getStorageSync("userInfo")
				let followDTo = {}
				followDTo.uid = curUser.id
				followDTo.fid = fid
				//添加关注
				followUser(followDTo).then(res => {
					this.T = true;
				})
			},

			//取消关注
			clearFollow(fid) {
				let user = uni.getStorageSync("userInfo")
				let followDTo = {}
				followDTo.uid = user.id
				followDTo.fid = fid
				clearFollow(followDTo).then(res => {
					this.T = false;
				})
			},

			//----------------------点赞部分-----------------
			isAgree() {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.type = 1
				data.agreeCollectId = this.imgInfo.id
				data.agreeCollectUid = this.imgInfo.userId

				isAgree(data).then(res => {

					this.userIsAgree = res.data

				})
			},

			agreeImg() {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.type = 1
				data.agreeCollectId = this.imgInfo.id
				data.agreeCollectUid = this.imgInfo.userId

				agree(data).then(res => {
					this.userIsAgree = true
					this.imgInfo.agreeCount = this.imgInfo.agreeCount * 1 + 1
				})
			},

			addAgree(comment) {

				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.type = 0
				data.agreeCollectId = comment.id
				data.agreeCollectUid = comment.uid
				agree(data).then()
			},

			cancelAgreeImg() {

				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.agreeCollectId = this.imgInfo.id
				data.agreeCollectUid = this.imgInfo.userId
				data.type = 1
				cancelAgree(data).then(res => {
					this.userIsAgree = false
					this.imgInfo.agreeCount = this.imgInfo.agreeCount * 1 - 1
				})

			},

			cancelAgreeComment(comment) {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.agreeCollectId = comment.id
				data.agreeCollectUid = comment.uid
				data.type = 0
				cancelAgree(data).then()
			},

			cancelCollect() {

				if (this.imgInfo.userId == uni.getStorageSync("userInfo").id) {
					return
				}
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.agreeCollectId = this.imgInfo.id
				data.agreeCollectUid = this.imgInfo.userId
				data.type = 2
				cancelCollection(data).then(res => {
					this.isCollection = false
					this.imgInfo.collectionCount = this.imgInfo.collectionCount * 1 - 1
				})
			},


			isCollectImgToAlbum() {

				let params = {
					uid: uni.getStorageSync("userInfo").id,
					mid: this.imgInfo.id
				}
				isCollectImgToAlbum(params).then(res => {

					this.isCollection = res.data
				})
			},

			//-----------------------------------预览图片----------------
			previewImgae(index) {
				let that = this
				uni.previewImage({
					current: index, // 当前显示图片的索引值
					urls: that.imgInfo.imgsUrl, // 需要预览的图片列表，photoList要求必须是数组
					longPressActions: {
						itemList: ['保存', '收藏'],
						success: function(data) {

							//进行保存
							if (data.tapIndex == 0) {
								let p = that.downLoadImg(that.imgInfo.imgsUrl[data.index])
								p.then((data) => {
									uni.showToast({
										title: data,
										icon: "none"
									});
								})
							}
							//进行收藏
							if (data.tapIndex == 1) {
								that.saveImgToAlbum()
							}


						},
						fail: function(err) {
							return
						}
					}
				})
			},
			getOne() {

				let params = {
					id: this.imgInfo.id
				}
				getOne(params).then(res => {


					this.imgInfo = res.data
					this.imgInfo.time = timeAgo(new Date(res.data.time))
					this.count = res.data.imgsUrl.length
					this.album = res.data.album

					this.isFollow()
					this.isAgree()
					this.isCollectImgToAlbum()
					if (this.imgInfo.userId == uni.getStorageSync("userInfo").id) {
						this.isCurrentUser = true
					}

				})
			},
			loadData() {
				this.val = Math.random()
			},

			getComment(comment) {
				this.comment = comment

				//第二种方案
				this.placeholder = '回复' + this.comment.username + ':'
				this.active()
			},

			addComment() {
				let userInfo = uni.getStorageSync("userInfo")

				if (userInfo == null || typeof userInfo == 'undefined') {
					return
				} else {

					let commentInfo = {}
					commentInfo.content = this.content
					commentInfo.uid = userInfo.id
					commentInfo.mid = this.imgInfo.id
					//添加一级评论
					if (this.comment.id == null) {
						commentInfo.pid = 0
						commentInfo.replyId = 0
						commentInfo.replyUid = 0
						commentInfo.level = 1

					} else {
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
						this.comment = {}
						this.parentId = commentInfo.pid
						this.commentInfo = res.data
						this.placeholder = "请输入内容~"

						if (this.imgInfo.commentCount == 0) {
							this.imgInfo.commentCount = this.imgInfo.commentCount * 1 + 1
						}

					})
				}
			},
			getUserInfo(uid) {
				uni.navigateTo({
					url: "/pages/otherUser/otherUser?uid=" + uid
				})
			},

			saveImgToAlbum() {

				if (this.imgInfo.userId == uni.getStorageSync("userInfo").id) {
					uni.showToast({
						title: "自己不能收藏",
						icon: "none"
					});
					return
				}

				uni.navigateTo({
					url: '/pages/addalbum/addalbum?mid=' + this.imgInfo.id
				})
			},
			// 保存图片至本地
			downLoadImg(path) {
				//下载图片资源至本地，返回文件的本地临时路径
				let p = new Promise((resolve, reject) => {
					uni.downloadFile({
						url: path,
						success: (res) => {
							if (res.statusCode === 200) {
								//保存图片至相册
								uni.saveImageToPhotosAlbum({
									filePath: res.tempFilePath,
									success: function() {
										resolve("保存成功")
									},
									fail: function() {
										reject("保存失败")
									}
								});
							}
						}
					})
				})
				return p;

			},

			toAlbum(aid) {
				uni.navigateTo({
					url: "/pages/user/albums/albumInfo?albumId=" + aid
				})
			},

			//-----------------编辑功能部分------------------
			popup() {
				this.popupShow = !this.popupShow
			},
			operate(index) {

				//编辑操作
				if (index == 0) {

					uni.navigateTo({
						url: "/pages/push/push?mid=" + this.imgInfo.id + '&type=update' + '&version=' + 0
					})
				} else if (index == 1) {

					let arr = []
					arr.push(this.imgInfo.id)
					deleteImgs(arr, this.imgInfo.userId).then(res => {
						uni.showToast({
							title: "删除成功"
						})

						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/index/index?currentTab=1'
							})
						}, 1000)

					})
				} else if (index == 3) {

					this.batchSaveShow = true
					// this.isshow = true
					for (var i = 0; i < this.imgInfo.imgsUrl.length; i++) {
						this.selectImgs.push(i)
					}

				}
			},

			operate2(index) {

				if (index == 0) {
					//批量保存
					this.batchSaveShow = true
					// this.isshow = true
					for (var i = 0; i < this.imgInfo.imgsUrl.length; i++) {
						this.selectImgs.push(i)
					}
				}
			},

			batchShow() {
				this.batchSaveShow = false
			},

			delAffirm(index) {
				this.selectImgs.push(index)
			},

			selectAll() {
				for (let i = 0; i < this.imgInfo.imgsUrl.length; i++) {
					this.selectImgs.push(i)
				}
			},
			cancalAll() {

				this.selectImgs = []
			},
			delCancel(index) {
				for (var i = 0; i < this.selectImgs.length; i++) {
					if (this.selectImgs[i] == index) {
						this.selectImgs.splice(i, 1)
						break
					}
				}
			},

			batchSave() {
				uni.showToast({
					title: "保存中",
					icon: "none"
				});

				this.selectImgs.forEach(item => {
					this.downLoadImg(this.imgInfo.imgsUrl[item])
				})

				setTimeout(() => {
					uni.showToast({
						title: "保存成功",
						icon: "none"
					});

					this.batchSaveShow = false
				}, 300)
			},

			toTagList(tid) {
				console.log(tid)
				uni.navigateTo({
					url: '/pages/tagImgList/tagImgList?tid=' + tid
				})
			},

			share(index) {

				if (index == 4) {
					//分享部分（暂未开发）	

				}
			}


		}

	}
</script>

<style scoped>
	@import url(./main.css);
</style>