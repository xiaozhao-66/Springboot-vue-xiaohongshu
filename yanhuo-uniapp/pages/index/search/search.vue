<template>
	<view class="container">

		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="25"></tui-icon></view>
				</view>

				<view class="tui-search-box">
					<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>
					<input class="tui-search-text" placeholder="请输入搜索内容" v-model="keyword" :focus="cursor"
						@input="change" />
				</view>

				<view class="tui-notice-box">
					<tui-button type="danger" height="54rpx" width="100rpx" :size="24" @click="search">搜索</tui-button>
				</view>

			</view>
		</tui-navigation-bar>

		<view class="search-list" v-if='cursor'>
			<view v-if="userList.length>0">
				<view class="tui-item-box" v-for="(user,index) in userList" :key="index">
					<view class="tui-msg-box" @click="getUserInfo(user.id)">
						<image :src="user.avatar" class="tui-msg-pic" mode="aspectFill"></image>
						<view class="tui-msg-item">
							<view class="tui-msg-name">{{user.username}}</view>
							<view class="tui-msg-type">
								<view>用户id：{{ user.userId }}</view>
								<view>粉丝：{{ user.fanCount }}</view>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view class="search-item" v-for="(item,index) in searchData" @click="selectTag(item.keyword)"
				v-html="item.highLightKeyword"></view>
		</view>

		<view v-else>
			<view class="input-history w" v-show="dataList.length>0">
				<view class="top">
					<view> 搜索历史</view>

					<view class="all-del" v-if="D">
						<tui-button type="white" :size="20" height="40rpx" width="120rpx" @click="allDel"
							:link='true'>全部删除</tui-button>
						<tui-button type="white" :size="20" height="40rpx" width="60rpx" @click="cancelDel"
							:link='true'>取消</tui-button>
					</view>

					<tui-icon name="delete" :size="16" @click="showDel" v-else></tui-icon>
				</view>
				<view class="his-main">
					<view class="tag-item" v-for="(item,index) in dataList" :key="index" v-show="index<searchLimit">
						<tui-tag type="gray" shape="circle" margin='10rpx' @click="selectTag(item)">
							<p>{{item}}</p>
						</tui-tag>
						<view class="del-tag" v-if="D">
							<tui-icon name="shut" :size="12" @click="deleteTag(item,index)"></tui-icon>
						</view>
					</view>
					<tui-tag type="gray" shape="circle" margin='10rpx' v-if="!T&&dataList.length>8"><tui-icon
							name="arrowdown" @click="arrowdown"></tui-icon></tui-tag>
					<tui-tag type="gray" shape="circle" margin='10rpx' v-if="T"><tui-icon name="arrowup"
							@click="arrowup"></tui-icon></tui-tag>
				</view>
			</view>

			<view class="input-guess w">
				<view class="top">
					<view>猜你想搜</view>
					<tui-icon name="refresh" :size="16" @click="refresh"></tui-icon>
				</view>
				<view class="gus-main">
					<view v-for="(item,index) in dataList2">
						<view @click="selectTag(item.content)" class="gus-item">
							<p>{{item.content}}</p>
						</view>
					</view>
				</view>
			</view>

			<view class="hot-search w">

				<view class="top">热门搜索</view>
				<view class="hot-main">
					<view class="hot-item" v-for="(item, index) in hotSearchList" :key="index">
						<view class="hot-item-l" @click="selectTag(item.keyword)">
							<tui-icon name="circle" :size="8" color="#ff0000" v-if="index==0"></tui-icon>
							<tui-icon name="circle" :size="8" color="#ff5500" v-if="index==1"></tui-icon>
							<tui-icon name="circle" :size="8" color="#ffaa00" v-if="index==2"></tui-icon>
							<tui-icon name="circle" :size="8" v-if="index!=0&&index!=1&&index!=2"></tui-icon>
							<view class="hot-content">{{item.keyword}}</view>
							<view class="con-tag new-tag" v-if="item.type==2">新</view>
							<view class="con-tag hot-tag" v-if="item.type==1">热</view>
						</view>
						<view class="hot-item-r">
							{{item.count}}
						</view>
					</view>

				</view>
			</view>
		</view>


	</view>
</template>

<script>
	import {
		getAllSearchRecord,
		addSearchRecord,
		deleteSearchRecord
	} from "@/api/searchRecord.js"
	import {
		getRecommend
	} from "@/api/imgDetail.js"
	import {
		addSearchRecordData,
		esSearchRecord,
		esAllSearchRecord
	} from "@/api/search.js"
	import {
		searchUserByUsername
	} from "@/api/user.js"
	export default {
		data() {
			return {
				keyword: '',
				D: false,

				dataList: [],
				dataList2: [],
				delList: [],
				page: 1,
				limit: 6,

				searchLimit: 8,
				T: false,
				//键盘
				cursor: false, //是否聚焦

				//聚焦内容
				searchData: [],

				//热门搜索
				hotSearchList: [],

				//搜索用户
				userList: [],

			}
		},

		created() {
			this.getAllSearchRecord()
			this.getRecommend()
			this.esAllSearchRecord()
		},

		onLoad(options) {

		},

		methods: {
			back() {
				uni.reLaunch({
					url: "/pages/index/index"
				})
			},

			showDel() {
				this.D = true
			},

			allDel() {
				deleteSearchRecord(uni.getStorageSync("userInfo").id, this.dataList).then(res => {
					this.dataList = []
				})
			},

			cancelDel() {
				this.D = false
			},

			deleteTag(item, index) {

				this.dataList.splice(index, 1)
				this.delList.push(item)
				deleteSearchRecord(uni.getStorageSync("userInfo").id, this.delList).then()
			},

			arrowdown() {
				this.T = true
				this.searchLimit = 20
			},
			arrowup() {
				this.T = false
				this.searchLimit = 8
			},

			change() {
				if (this.keyword == '') {
					this.cursor = false
					return
				}

				this.cursor = true

				let params = {
					keyword: this.keyword
				}

				searchUserByUsername(params).then(res => {

					this.userList = res.data
				})

				esSearchRecord(params).then(res => {

					this.searchData = res.data
				})


			},

			selectTag(item) {
				this.keyword = item
				this.search()
			},
			getAllSearchRecord() {

				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getAllSearchRecord(params).then(res => {
					if (typeof(res.data) != 'undefined') {
						this.dataList = res.data
					}
				})
			},

			search() {
				if (this.keyword == '' || this.keyword == null) {
					uni.showToast({
						title: "内容不能为空~",
						icon: 'none'
					})
					return
				}

				//添加一条搜索记录
				this.addSearchRecord()

				let params = {
					keyword: this.keyword
				}

				addSearchRecordData(params).then(res => {
					uni.navigateTo({
						url: "/pages/index/search/searchList?keyword=" + this.keyword
					})
				})

			},

			getRecommend() {
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getRecommend(this.page, this.limit, params).then(res => {
					this.dataList2 = res.data.records

				})
			},

			refresh() {
				this.getRecommend()
			},

			addSearchRecord() {
				let SearchRecordDTO = {}
				SearchRecordDTO.uid = uni.getStorageSync("userInfo").id
				SearchRecordDTO.keyword = this.keyword
				addSearchRecord(SearchRecordDTO).then()
			},

			esAllSearchRecord() {


				let nowTime = new Date().valueOf(); //转换成毫秒

				esSearchRecord().then(res => {

					res.data.forEach(item => {
						let obj = {}
						obj.count = item.count
						obj.highLightKeyword = item.highLightKeyword
						obj.id = item.id
						obj.keyword = item.keyword

						let jetLag = (nowTime - item.time) / 1000 / 60

						//10分钟之内显示最新
						if (jetLag < 10) {
							//最新
							obj.type = 2
						} else if (jetLag > 10 && item.count >= 2) {
							//最热
							obj.type = 1
						}

						this.hotSearchList.push(obj)
					})
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

		}
	}
</script>

<style scoped>
	@import url(./search.css);
</style>