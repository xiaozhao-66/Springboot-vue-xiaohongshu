<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
			<tui-tabs :tabs="tabs" :currentTab="currentTab" itemWidth="50%" @change="change" sliderBgColor="#ff0000"
				selectedColor="#ff0000"></tui-tabs>
		</tui-navigation-bar>

		<view @touchstart="start" @touchend="end">
			<interest v-if="currentTab == 0"></interest>
			<hot v-if="currentTab == 2"></hot>

			<view v-if="currentTab == 1">
				<scroll-view scroll-y class="page" @scrolltolower="loadData" refresher-enabled="true"
					:refresher-triggered="triggered" @refresherrefresh="onRefresh" @scroll="scroll"
					:scroll-top="scrollTop">
					<view>
						<tui-drawer mode="left" :visible="visiable" @close="closeDrawer">
							<view class="d-container">
								<view class="d-content">
									<view class="find-user">
										<navigator url="/pages/addfriend/addfriend">
											<view class="item">
												<tui-icon name="friendadd" size="20"></tui-icon>
												发现好友
											</view>
										</navigator>
									</view>
									<view class="d-function">
										<ul>
											<li>
												<navigator url="/pages/history/history">
													<view class="item">
														<tui-icon name="clock" size="20"></tui-icon>
														浏览记录
													</view>
												</navigator>
											</li>
											<li>
												<navigator url="/pages/group/group">
													<view class="item">
														<tui-icon name="like" size="20"></tui-icon>
														关注分组
													</view>
												</navigator>
											</li>
										</ul>
									</view>
									<view class="d-function">
										<ul>
											<li>
												<view class="item">
													<tui-icon name="circle" size="20"></tui-icon>
													其他功能
												</view>
											</li>
											<li>
												<view class="item">
													<tui-icon name="circle" size="20"></tui-icon>
													其他功能
												</view>
											</li>
										</ul>
									</view>
								</view>
							</view>
						</tui-drawer>
					</view>

					<view class="tui-content-box">
						<view class="tui-avatar-box">
							<view @click="openDrawer" v-if="userInfo">
								<image :src="userInfo.avatar" class="tui-avatar" mode="aspectFill" />
							</view>
							<view v-else>
								<image src="/static/images/toast/img_nodata.png" class="tui-avatar" />
							</view>
						</view>
						<view class="tui-search-box" @click="toSearch">
							<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>
							<view class="tui-search-text">请输入内容</view>
						</view>
					</view>


					<view :class="isFixed?'tui-mtop-fixed':'tui-mtop' " id='tui-mtop'>
						<view class="nav">
							<ul>
								<li v-for="(item, index) in categoryList" :key="index">
									<a @click="getImgListByCategory(item.id,index)">
										<view :class="index==C?'c-activated':''">{{item.name}}</view>
									</a>
								</li>
							</ul>
						</view>

						<a>
							<tui-icon name="arrowdown" size="20" color="#666666" @click="show" v-if="T"></tui-icon>
							<tui-icon name="arrowup" size="20" color="#666666" @click="show" v-else></tui-icon>
						</a>

						<view class="nav-show" :class="T ? 'box-show' : ''">
							<ul>
								<li v-for="(item, index) in categoryList" :key="index" v-show="index>=7">
									<a @click="getImgListByCategory(item.id,index)">
										<view :class="index==C?'c-activated':''">{{item.name}}</view>
									</a>
								</li>
							</ul>
						</view>
					</view>


					<!-- 使用瀑布流 -->
					<view class="main">

						<uv-waterfall ref="waterfall" v-model="list" column-count="2" @changeList="changeList"
							column-gap="8">
							<template v-slot:list1>
								<!-- 为了磨平部分平台的BUG，必须套一层view -->
								<view>
									<uni-list :data="list1" :total='list1.length' style="background-color: #f4f4f4;">
										<view v-for="(item, index) in list1" :key="index" class="waterfall-item">

											<view class="waterfall-item__image">
												<image :src="item.image" :lazy-load="true" mode=""
													:width="item.width+'px'" @click="getImgInfo(item.id)">

												</image>
											</view>
											<view class="card">

												<view class="cont">
													<view class="content">{{item.content}}
													</view>
													<view class="userInfo">
														<view class="avatar-usrname">
															<image :src="item.avatar" :lazy-load="true"
																mode="aspectFill" class="avatar"></image>
															<view class="username">{{ item.username }}</view>
														</view>
														<view class="agreeCount"><tui-icon name="star"
																size="10"></tui-icon>{{ item.agreeCount }}</view>
													</view>
													<view class="card-nums">
														{{item.count}}
													</view>
												</view>
											</view>
										</view>
									</uni-list>


								</view>
							</template>
							<!-- 第二列数据 -->
							<template v-slot:list2>
								<!-- 为了磨平部分平台的BUG，必须套一层view -->
								<view>
									<uni-list :data='list2' :total='list2.length' style="background-color: #f4f4f4;">
										<view v-for="(item, index) in list2" :key="index" class="waterfall-item">
											<view class="waterfall-item__image">
												<image :src="item.image" :lazy-load="true" mode="widthFix"
													:width="item.width+'px'" @click="getImgInfo(item.id)">
												</image>
											</view>
											<view class="card">

												<view class="cont">
													<view class="content">{{item.content}}</view>
													<view class="userInfo">
														<view class="avatar-usrname">
															<image :src="item.avatar" :lazy-load="true"
																mode="aspectFill" class="avatar"></image>
															<view class="username">{{ item.username }}</view>
														</view>
														<view class="agreeCount"><tui-icon name="star"
																size="10"></tui-icon>{{ item.agreeCount }}</view>
													</view>

													<view class="card-nums">
														{{item.count}}
													</view>
												</view>
											</view>
										</view>

									</uni-list>

								</view>
							</template>
						</uv-waterfall>
					</view>
					<view class="loadStyle" v-if="!isEnd && loading">
						<tui-icon name="loading" :size="18"></tui-icon>
					</view>
					<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
				</scroll-view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		getPage,
		getRecommend
	} from "@/api/imgDetail.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"

	import {
		getCategory,
		getImgListByCategory
	} from '@/api/category.js'
	import {
		getChatUserList
	} from '@/api/chat.js'
	import {
		getUserRecord
	} from "@/api/user.js"
	import Interest from '@/pages/index/interest/interest.vue'
	import Hot from '@/pages/index/hot/hot.vue'
	export default {
		components: {
			Interest,
			Hot,
		},
		props: {
			seed: Number
		},
		data() {
			return {

				currentTab: 1,
				tabs: [{
						name: "关注"
					}, {
						name: "首页"
					},
					{
						name: "热榜"
					}
				],

				T: true,
				C: -1,
				visiable: false,
				triggered: false,
				userInfo: {},
				page: 1,
				limit: 5,
				size: 50,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				keyword: '',
				categoryList: [],
				isSearchByCategory: false,
				isFirst: false,
				cid: '',

				//滚动
				scrollTop: 0,
				old: {
					scrollTop: 0
				},

				isFixed: false,

				startData: {},

				list: [], //瀑布流总数据
				list1: [], // 瀑布流第一列数据
				list2: [], // 瀑布流第二列数据

				needRefresh: false,

				stickyScrollTop: 0,
			}
		},
		watch: {
			seed(newVal, oldVal) {
				this.userInfo = uni.getStorageSync("userInfo")
			},
		},
		onPageScroll(e) {
			this.stickyScrollTop = e.stickyScrollTop
		},
		created() { 
			this.getCategory()
			this.userInfo = uni.getStorageSync("userInfo")

			getApp().globalData.currentUser = this.userInfo;
			if (this.goEasy.getConnectionStatus() === 'disconnected') {
				this.connectGoEasy(); //连接goeasy
			}
			this.getRecordCount()
		},
		
		mounted(){
			this.getRecommend()
		},

		onHide() {
			this.needRefresh = false
		},

		onTabItemTap(e) {
			this.currentTab = 1

			if (this.needRefresh) {
				uni.pageScrollTo({ // 回到顶部
					duration: 0,
					scrollTop: 0
				})
				this.onRefresh()
			} else {
				this.needRefresh = true
			}

			//  双击事件
			// if (this.tabClick) { // 200ms 内再次点击
			// 	// 这里就是模拟的双击事件，可以写类似数据刷新相关处理
			// 	this.onRefresh()
			// }
			// this.tabClick = true
			// setTimeout(() => {
			// 	this.tabClick = false // 200ms 内没有第二次点击，就当作单击
			// }, 200)
		},

		methods: {

			connectGoEasy() {
				
				this.goEasy.connect({
					id: this.userInfo.id,
					data: {
						name: this.userInfo.username,
						avatar: this.userInfo.avatar
					},
					onSuccess: () => {
						//this.triggered = false
						console.log('GoEasy connect successfully.')

					},
					onFailed: (error) => {
						console.log('Failed to connect GoEasy, code:' + error.code + ',error:' + error
						.content);
					},
					onProgress: (attempts) => {
						console.log('GoEasy is connecting', attempts);
					}
				});
			},


			changeSticky(e) {
				console.log(e)
			},

			change(e) {
				this.currentTab = e.index
			},

			start(e) {

				this.startData.clientX = e.changedTouches[0].clientX;

				this.startData.clientY = e.changedTouches[0].clientY;
			},
			end(e) {

				const subX = e.changedTouches[0].clientX - this.startData.clientX;
				const subY = e.changedTouches[0].clientY - this.startData.clientY;
				if (subY > 50 || subY < -50) {
					//console.log('上下滑')
				} else {
					if (subX > 100) {
						if (this.currentTab > 0) {
							this.currentTab = this.currentTab - 1
						}

					} else if (subX < -100) {
						if (this.currentTab < 2) {
							this.currentTab = this.currentTab + 1
						}

					} else {}
				}
			},

			//---
			// getChatUserList() {

			// 	return new Promise((resolve) => {

			// 		let params = {
			// 			uid: uni.getStorageSync("userInfo").id
			// 		}
			// 		getChatUserList(params).then(res => {
			// 			let count = 0

			// 			res.data.forEach(item => {
			// 				if (item.count > 0) {
			// 					count += item.count
			// 				}
			// 			})

			// 			resolve(count)
			// 		})

			// 	})


			// },
			getUserRecord() {
				return new Promise((resolve) => {
					let params = {
						uid: uni.getStorageSync("userInfo").id
					}
					getUserRecord(params).then(res => {
						resolve(res.data)
					})
				})

			},

			async getRecordCount() {

				//let count = await this.getChatUserList()


				let res = await this.getUserRecord()
				let count = uni.getStorageSync("unreadTotal")

				if (res.agreeCollectionCount > 0 || res.addFollowCount > 0 || res.noreplyCount > 0 || count > 0) {
					uni.showTabBarRedDot({ //显示红点
						index: 2 //tabbar下标
					})
				} else {
					uni.hideTabBarRedDot({
						index: 2
					})
				}
			},

			//---

			show() {
				this.T = !this.T
			},
			openDrawer() {
				this.visiable = true
			},
			closeDrawer() {
				this.visiable = false
			},

			scroll(e) {

				this.old.scrollTop = e.detail.scrollTop
				if (e.detail.scrollTop > 50) {
					this.isFixed = true
				} else {
					this.isFixed = false
				}

			},

			getCategory() {
				getCategory().then(res => {
					this.categoryList = res.data
				})
			},


			getMoreData(dataList) {
				dataList.forEach(item => {
					this.list.push(this.getItem(item))
				})
				setTimeout(()=>{
					this.triggered = false
				},500)
			},
			
			getItem(item) {
				return {
					id: item.id,
					image: item.cover,
					content: item.content,
					count: item.count,
					avatar: item.avatar,
					username: item.username,
					agreeCount: item.agreeCount,
				}
			},

			getImgListByCategory(id, index) {
				this.C = index
				//数据要先清空
				this.clear()

				this.cid = id
				let params = {
					id: id,
					type: 1,
				}
				this.page = 1

				getImgListByCategory(this.page, this.limit, params).then(res => {
					this.isSearchByCategory = true
					this.T = true
					this.total = res.data.total
					this.getMoreData(res.data.records)
				})
			},


			getPage() {
				let params = {}
				getPage(this.page, this.limit, params).then(res => {
					this.total = res.data.total
					this.getMoreData(res.data.records)
				})
			},
			onRefresh() {
				this.C = -1
				this.triggered = true;
				this.isEnd = false
				setTimeout(() => {
					this.triggered = false;
					this.clear()

					this.page = 1

					this.isSearchByCategory = false

					this.getRecommend()
				}, 500)
			},
			// 清空数据
			clear() {
				//刷新数据之前要把dataList数据清空
				this.list = [];
				this.list1 = [];
				this.list2 = [];
				this.$refs.waterfall.clear();
			},
			getRecommend() {
				this.triggered = true
				let params = {
					uid: this.userInfo.id
				}
				getRecommend(this.page, this.size, params).then(res => {

					if (res.data == null) {
						this.isFirst = true
						this.getPage()
					} else {
						this.total = res.data.total
						this.getMoreData(res.data.records)
					}
				})
			},


			
			changeList(e) {
				this[e.name].push(e.value);

			},
			loadData() {
				this.loading = true

				setTimeout(() => {

					if (this.isFirst || this.isSearchByCategory) {
						if (this.list.length >= this.total) {
							this.isEnd = true
							return
						}
					} else {
						if (this.total == 0) {
							this.isEnd = true
							return
						}
					}

					this.page = this.page + 1;

					if (this.isSearchByCategory) {

						let params = {
							id: this.cid,
							type: 1,
						}

						getImgListByCategory(this.page, this.limit, params).then(res => {
							this.getMoreData(res.data.records)
						})

					} else {

						let params = {
							uid: this.userInfo.id
						}
						getRecommend(this.page, this.size, params).then(res => {

							if (res.data == null) {
								getPage(this.page, this.limit, params).then(res => {
									this.getMoreData(res.data.records)
									this.total = res.data.total
								})
							} else {
								this.getMoreData(res.data.records)
								this.total = res.data.total
							}

						})
					}

				}, 100)
			},
			getImgInfo(mid) {
				this.isFirst = false
				//添加一条浏览记录
				let data = {}
				data.uid = this.userInfo.id
				data.mid = mid
				addBrowseRecord(data).then(res => {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + mid
					})
				})
			},
			toSearch() {
				uni.navigateTo({
					url: "/pages/index/search/search"
				})
			}


		}
	}
</script>

<style scoped>
	@import url(./index.css);
</style>