<template>
	<view class="container">

		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="tui-content-box">
				<view class="tui-avatar-box">
					<view @click="back"><tui-icon name="arrowleft" size="25"></tui-icon></view>
				</view>
				<view class="tui-search-box">
					<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>

					<input class="tui-search-text" placeholder="请输入搜索内容" v-model="keyword" />
				</view>
				<view class="tui-notice-box">
					<tui-button type="danger" height="54rpx" width="100rpx" :size="24" @click="search()">搜索</tui-button>
				</view>
			</view>
		</tui-navigation-bar>

		<nav class="navbar">
			<ul class="nav-list">
				<li class="nav-item dropdown">
					<a href="#" :class="0==nav?'nav-link activated':'nav-link'" @click="selectNav(0)">
						全部
						<tui-icon name="arrowup" :size="18" v-if="showMenu"></tui-icon>
						<tui-icon name="arrowdown" :size="18" v-else></tui-icon>
					</a>
					<ul class="dropdown-menu" v-show="showMenu">
						<li :class="0==activate?'menu-activated':''"><a href="#" @click="esSearch(0)">全部</a></li>
						<li :class="1==activate?'menu-activated':''"><a href="#" @click="esSearch(1)">最热</a></li>
						<li :class="2==activate?'menu-activated':''"><a href="#" @click="esSearch(2)">最新</a></li>
					</ul>
				</li>
				<li class="nav-item">
					<a href="#" :class="1==nav?'nav-link activated':'nav-link'" @click="selectNav(1)">用户</a>
				</li>
				<li class="nav-item">
					<a href="#" :class="2==nav?'nav-link activated':'nav-link'" @click="selectNav(2)">表情包</a>
				</li>
			</ul>
		</nav>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">

			<!-- 使用瀑布流 -->
			<view class="main" v-if="nav==0">
				<pp-waterfall-flow :value="dataList" @clickImage="getImgInfo"> </pp-waterfall-flow>
			</view>


			<view v-if="nav==1">
				<tui-list-view>
					<tui-list-cell :lineLeft="false" v-for="(item, index) in userList" :key="index">
						<view class="tui-item-box">
							<view class="tui-msg-box" @click="getUserInfo(item.uid)">
								<image :src="item.avatar" class="tui-msg-pic" mode="aspectFill"></image>
								<view class="tui-msg-item">
									<view class="tui-msg-name">{{ item.username }}</view>
									<view class="tui-msg-type">
										<p>用户id：{{ item.userId }}</p>
										<p>粉丝：{{ item.fanCount }}</p>
									</view>
								</view>
							</view>
							<view class="tui-msg-right" v-if="userInfo.id!=item.uid">

								<tui-button v-if='!item.isfollow' @click="follow(item.uid,index)" height="60rpx"
									width="120rpx" type="danger" :size="24">关注</tui-button>

								<tui-button v-else @click="clearFollow(item.uid,index)" height="60rpx" width="120rpx"
									type="gray" :size="24">取消关注</tui-button>
							</view>
						</view>
					</tui-list-cell>
				</tui-list-view>
			</view>


			<view class="loadStyle" v-if="!isEnd && loading">
				<tui-icon name="loading" :size="18"></tui-icon>
			</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>

	</view>
</template>

<script>
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		esSearch
	} from '@/api/search.js'
	import {
		addSearchRecord
	} from "@/api/searchRecord.js"
	import {
		searchUser
	} from '@/api/user.js'
	import {
		clearFollow,
		followUser
	} from '@/api/follow.js'
	export default {

		data() {
			return {
				triggered: false,
				userInfo: {},
				page: 1,
				limit: 10,
				total: 0,
				dataList: [],
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				keyword: '',

				//es
				type: 0,

				// 导航栏,
				nav: 0,
				showMenu: false,
				clickCount: 0,
				activate: 0,

				//查找用户
				userList: [],
			}
		},

		created() {
			this.userInfo = uni.getStorageSync("userInfo")

		},
		onLoad(options) {
			this.keyword = options.keyword
			this.esSearch(0)
		},

		methods: {
			back() {
				uni.navigateTo({
					url: "/pages/index/search/search"
				})
			},

			selectNav(index) {

				if (index == 0) {
					this.showMenu = !this.showMenu
					this.esSearch(0)
				} else if (index == 1) {
					//搜索用户
					this.showMenu = false

					this.searchUser()

				} else {
					this.showMenu = false
				}

				this.nav = index
			},



			loadData() {
				let that = this

				that.isEnd = false
				that.loading = true
				setTimeout(() => {

					if (that.nav == 0) {
						if (that.dataList.length >= that.total) {
							that.isEnd = true
							return
						}

						that.page = that.page + 1;

						let ImgDetailSearchDTO = {}
						ImgDetailSearchDTO.keyword = that.keyword
						ImgDetailSearchDTO.type = that.type
						esSearch(that.page, that.limit, ImgDetailSearchDTO).then(res => {
							that.dataList.push(...res.data.records)
						})
					} else if (that.nav == 1) {


						if (that.userList.length >= that.total) {
							that.isEnd = true
							return
						}

						that.page = that.page + 1;

						let params = {
							keyword: that.keyword,
							uid: that.userInfo.id
						}

						searchUser(that.page, that.limit, params).then(res => {
							that.userList.push(...res.data.records)
						})

					} else {
						console.log("加载表情包数据")
					}


				}, 1000)
			},
			getImgInfo(e) {

				//添加一条浏览记录
				let data = {}
				data.uid = this.userInfo.id
				data.mid = e.id

				addBrowseRecord(data).then(res => {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + e.id
					})
				})
			},


			search() {
				//搜索全部数据
				if (this.nav == 0) {
					this.esSearch(0)
				} else if (this.nav == 1) {
					this.searchUser()
				} else {
					//搜索表情包
				}

			},

			esSearch(type) {

				this.activate = type
				this.type = type

				this.dataList = []

				let SearchRecordDTO = {}
				SearchRecordDTO.uid = uni.getStorageSync("userInfo").id
				SearchRecordDTO.keyword = this.keyword
				addSearchRecord(SearchRecordDTO).then()

				let ImgDetailSearchDTO = {}
				ImgDetailSearchDTO.keyword = this.keyword
				ImgDetailSearchDTO.type = type

				this.page = 1

				esSearch(this.page, this.limit, ImgDetailSearchDTO).then(res => {
					this.dataList = res.data.records
					this.total = res.data.total

				})
			},

			searchUser() {

				let params = {
					keyword: this.keyword,
					uid: this.userInfo.id
				}

				searchUser(this.page, this.limit, params).then(res => {

					this.userList = res.data.records
					this.total = res.data.total

				})
			},


			getUserInfo(uid) {
				if (uid == uni.getStorageSync("userInfo").id) {
					uni.switchTab({
						url: "/pages/user/user"
					})
				} else {
					uni.navigateTo({
						url: "/pages/otherUser/otherUser?uid=" + uid
					})
				}
			},


			follow(fid, index) {

				let followDTo = {}
				followDTo.uid = this.userInfo.id
				followDTo.fid = fid
				//添加关注
				followUser(followDTo).then(res => {
					this.userList[index].isfollow = true
				})
			},

			clearFollow(fid, index) {


				let followDTo = {}
				followDTo.uid = this.userInfo.id
				followDTo.fid = fid
				clearFollow(followDTo).then(res => {
					this.userList[index].isfollow = false
				})
			}

		}
	}
</script>

<style scoped>
	@import url(./searchList.css);
</style>