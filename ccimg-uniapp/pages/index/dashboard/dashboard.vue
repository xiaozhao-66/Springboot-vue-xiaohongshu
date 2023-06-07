<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData" refresher-enabled="true"
				:refresher-triggered="triggered" @refresherrefresh="onRefresh" @scroll="scroll" :scroll-top="scrollTop">
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
					<view class="tui-search-text" >请输入内容</view>
				</view>
			</view>
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


			<view :class="isFixed?'tui-mtop-fixed':'tui-mtop' " id='tui-mtop' >
				<view class="nav" >
					<ul>
						<li v-for="(item, index) in categoryList" :key="index">
							<a @click="getImgListByCategory(item.id,index)"><view :class="index==C?'c-activated':''">{{item.name}}</view></a>
						</li>
					</ul>
				</view>

				<a>
					<tui-icon name="arrowdown" size="20" color="#666666" @click="show"  v-if="T"></tui-icon>
				   <tui-icon name="arrowup" size="20" color="#666666" @click="show"  v-else></tui-icon>
				</a>

				<view class="nav-show" :class="T ? 'box-show' : ''">
					<ul>
						<li v-for="(item, index) in categoryList" :key="index">
							<a @click="getImgListByCategory(item.id,index)"><view :class="index==C?'c-activated':''">{{item.name}}</view></a>
						</li>
					</ul>
				</view>
			</view>

            
            <!-- 使用瀑布流 -->
            <view class="main">
				<pp-waterfall-flow :value="dataList" @clickImage="getImgInfo"> </pp-waterfall-flow>
			</view>

			<!-- 不适用瀑布流 -->
			<!-- <tui-grid>
			<block v-for="(item,index) in dataList" :key="index">
				<tui-grid-item :cell="2"  :border="false" :bottomLine="false">
					<view class="card">
						<view @click="getImgInfo(item.id)">
							<img class="cover" :src="item.cover" />
						</view>
						<view class="card-nums">
							{{item.count}}
						</view>
						<view class="cont">
							<view class="content">{{item.content}}</view>
							<view class="userInfo">
								<view class="avatar-usrname">
									<image :src="item.avatar" :lazy-load="true" mode="aspectFill"></image>
									<view class="username">{{item.username}}</view>
								</view>
								<view><tui-icon name="star" size="10"></tui-icon>{{item.agreeCount}}</view>
							</view>
						</view>
					</view>
				</tui-grid-item>
			</block>
		</tui-grid> -->

		<view class="loadStyle" v-if="!isEnd && loading">
			<tui-icon name="loading" :size="18"></tui-icon>
		</view>
		<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
	</scroll-view>
		
	</view>
</template>

<script>
import { getPage, getRecommend } from "@/api/imgDetail.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
import { esSearch } from '@/api/search.js'
import { getCategory, getImgListByCategory } from '@/api/category.js'
export default {
	props: {
		seed: Number
	},
	data() {
		return {
			T: true,
			C: -1,
			visiable: false,
			triggered: false,
			userInfo: {},
			page: 1,
			limit: 20,
			total: 0,
			dataList: [],
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			keyword: '',
			isSearch: false,
			categoryList: [],
			isSearchByCategory: false,
			cid: '',
			
			//滚动
			scrollTop: 0,
			old: {
				scrollTop: 0
			},
			isFixed:false,
		}
	},
	watch:{
	  seed(newVal,oldVal){
		   
		   this.userInfo = uni.getStorageSync("userInfo")
		   this.onRefresh()
	  },
	},
	created() {
		this.getCategory()
		this.userInfo = uni.getStorageSync("userInfo")
		
	},
	mounted() {
		
		this.getRecommend()
	},
	
	methods: {
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
			if(e.detail.scrollTop>50){
				this.isFixed = true
			}else{
				this.isFixed = false
			}
			
		},
		
		getCategory() {
			getCategory().then(res => {
				
				this.categoryList = res.data
				
			})
		},

		getImgListByCategory(id,index) {
			this.C = index
			//数据要先清空
			this.dataList = []
			
			this.cid = id
			let params = {
				id: id,
				type: 1,
			}
			this.page = 1

			getImgListByCategory(this.page, this.limit, params).then(res => {
				
				this.dataList = res.data.records
				this.total = res.data.total
				this.isSearchByCategory = true
				this.T = true
			})

		},
		
		

		getPage() {
			let params = {}
			getPage(this.page, this.limit, params).then(res => {
				this.dataList = res.data.records
				this.total = res.data.total
			})
		},
		onRefresh() {
			this.C = -1
			this.triggered = true;
            this.isEnd = false
			setTimeout(() => {
				this.triggered = false;
			}, 300)
			
			//刷新数据之前要把dataList数据清空
			this.dataList=[]
			
			this.page = 1
			this.isSearch = false
			this.isSearchByCategory = false
			this.keyword = ''
			
			this.getRecommend()
		},
		getRecommend() {
			let params = {
				uid: this.userInfo.id
			}
			getRecommend(this.page, this.limit, params).then(res => {
              
				if (res.data == null) {
					this.getPage()
				} else {
					this.dataList = res.data.records
					this.total = res.data.total
				}
			})
		},
		loadData() {
			this.loading = true
			setTimeout(() => {
				if (this.dataList.length >= this.total) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;

				if (this.isSearch) {
					let params = {
						keyword: this.keyword
					}
					esSearch(this.page, this.limit, params).then(res => {

						this.dataList.push(...res.data.records)
					})
				} else if (this.isSearchByCategory) {

					let params = {
						id: this.cid,
						type: 1,
					}

					getImgListByCategory(this.page, this.limit, params).then(res => {
						this.dataList.push(...res.data.records)
					})

				} else {
					
					let params = {
						uid: this.userInfo.id
					}
					getRecommend(this.page, this.limit, params).then(res => {
						
						if (res.data == null) {
							getPage(this.page, this.limit, params).then(res => {
								this.dataList.push(...res.data.records)
							})
						} else {
							this.dataList.push(...res.data.records)
						}
					})
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
		esSearch() {
			this.dataList = []
			let params = {
				keyword: this.keyword
			}
			this.page = 1
			this.isSearch = true

			esSearch(this.page, this.limit, params).then(res => {
				this.dataList = res.data.records
				this.total = res.data.total

				this.keyword = ''
			})
		},
		
		toSearch(){
			uni.navigateTo({
				url: "/pages/index/search/search"
			})
		}


	}
}
</script>

<style scoped>
a {
	text-decoration: none;
	color: #7a7a7a;
}

ul {
	padding-left: 0rpx;
}

li {
	list-style: none;
}

.container {
	margin-top: 170rpx;
}

.main {
	/* column-count: 2;
	column-gap: 0rpx; */
	width: 95%;
	margin: auto;
	background-color: #f4f4f4;
}




.page {
	height: 90vh;

}

.d-container {
	width: 400rpx;
	padding: 180rpx 40rpx;
}

.d-content {
	/* background-color: #eaeef1; */
}

.find-user {
	height: 80rpx;
	border-bottom: 1px solid #f4f4f4;
}

.d-function {
	border-bottom: 1px solid #f4f4f4;
}

.d-function ul li {
	margin-top: 30rpx;
	margin-bottom: 30rpx;
}



.item {
	display: flex;
	justify-content: space-between;
	width: 200rpx;
	align-items: center;
	padding: 0rpx 10rpx;
	color: #7f7f7f;
	font-size: 30rpx;
}

.cover {
	width: 100%;
	max-height: 450rpx;
	object-fit: cover;
}

image {
	width: 100%;

	border-top-left-radius: 8px;
	border-top-right-radius: 8px;

}


.header {
	padding: 80rpx 90rpx 60rpx 90rpx;
	box-sizing: border-box;
}

.title {
	font-size: 34rpx;
	color: #333;
	font-weight: 500;
}

.sub-title {
	font-size: 24rpx;
	color: #7a7a7a;
	padding-top: 18rpx;
}

.tui-title {
	width: 100%;
	padding: 50rpx 30rpx 30rpx;
	box-sizing: border-box;
	font-weight: bold;
}

.tui-header-bg {
	width: 100%;
	margin: 0;
}

.tui-mtop {
	position: relative;
	display: flex;
}

.tui-mtop-fixed{
	position: fixed; 
	display: flex;
	width: 100%;
	top: 150rpx; 
	z-index: 999; 
	background-color: #fff;
}

.nav {
	overflow: auto;
}

.nav ul {
	white-space: nowrap;
}

.nav::-webkit-scrollbar {
	display: none;
}

.nav ul li {
	display: inline-block;
	font-size: 25rpx;

}

.nav ul li a {
	display: inline-block;
	margin: 0rpx;
	color: #333;
	padding-left: 45rpx;
}

.c-activated{
	
	color: red;
}

.tui-mtop a {
	line-height: 80rpx;
	margin-right: 40rpx;
}

.tui-mtop-fixed a {
	line-height: 80rpx;
	margin-right: 40rpx;
}



.nav-show {
	position: absolute;
	background-color: #fff;
	top: 82rpx;
	width: 100%;
	font-size: 24rpx;
	z-index: 9999;
}

.box-show {
	display: none;
}

.nav-show ul {
	
}

.nav-show ul li {
	float: left;
	display: inline-block;
}

.nav-show ul li a {
	display: inline-block;
	margin: 0rpx;
	color: #333;
	padding-left: 45rpx;
}


.tui-header-icon {
	width: 100%;
	position: fixed;
	top: 0;
	padding: 0 12rpx;
	display: flex;
	align-items: center;
	height: 32px;
	transform: translateZ(0);
	z-index: 99999;
	box-sizing: border-box;
}

.tui-content-box {
	width: 100%;
	height: 44px;
	padding: 0 30rpx;
	box-sizing: border-box;
	display: flex;
	align-items: center;
}

.tui-avatar-box {
	width: 30px;
	height: 30px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	border: 1px solid #eaeef1;
	flex-shrink: 0;
}

.tui-avatar {
	width: 28px;
	height: 28px;
	border-radius: 50%;
}

.tui-search-box {
	width: 100%;
	height: 32px;
	margin: 0 28rpx;
	border-radius: 18px;
	font-size: 14px;
	background-color: #f1f1f1;
	padding: 0 12px;
	box-sizing: border-box;
	color: #bfbfbf;
	display: flex;
	align-items: center;
}

.content {
	width: 300rpx;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	font-size: 28rpx;
}

.tui-bg-white {
	background-color: #ffffff !important;
}

.tui-search-text {
	padding-left: 10rpx;
}

.tui-notice-box {
	position: relative;
	flex-shrink: 0;
	font-size: 44rpx;
	color: #fff;
}

.tui-grid {
	padding: 3px 3px;

}

/* .card {
	margin: 10rpx 2rpx;
	box-sizing: border-box;
	break-inside: avoid;
	background-color: #fff;
	position: relative;
	border-radius: 8px;
	border: 1px solid #f1f1f1;
}

.card .cont {
	margin-left: 10px;
}

.card .cont p {
	margin-top: 5px;
	font-size: 12px;
}



.card .card-nums {
	position: absolute;
	bottom: 130rpx;

	left: 330rpx;
	background-color: #7a7a7a;
	width: 30rpx;
	height: 45rpx;
	text-align: center;
	color: #fff;
	opacity: 0.5;
}

.userInfo {
	margin-top: 5px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-right: 5px;
}

.avatar-usrname image {
	width: 50rpx;
	height: 50rpx;
	border-radius: 50%;
}

.avatar-usrname {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-bottom: 5px;
}

.username {
	color: #000000;
	font-size: 24rpx;
}

.agreeCount {
	color: #000000;
	font-size: 24rpx;
} */

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}
</style>
