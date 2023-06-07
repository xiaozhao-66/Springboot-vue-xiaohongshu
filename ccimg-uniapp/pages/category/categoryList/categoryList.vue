<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="24"></tui-icon></view>
					<view>分类数据</view>
					
				</view>
			</view>
		</tui-navigation-bar>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<!-- 使用组件做瀑布流 -->
			<pp-waterfall-flow :value="dataList" @clickImage="getImgInfo"> </pp-waterfall-flow>
			<!-- end -->
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>

import { getImgListByCategory } from "@/api/category.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
export default {
	data() {
		return {
			dataList: [],
			page: 1,
			limit: 8,
			total: 0,
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			cid: '',
			type:0,
		}
	},
	onLoad(options) {
		this.cid = options.cid
		this.type = options.type
		
		this.getImgListByCategory()
	},
	created() {

	},
	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getImgListByCategory() {
			let params = {
				id: this.cid,
				type: this.type,
			}

			getImgListByCategory(this.page, this.limit, params).then(res => {

				this.dataList = res.data.records
				this.total = res.data.total
			})
		},

		loadData() {
			
			this.loading = true
            
			if (this.dataList.length >= this.total) {
				this.isEnd = true
				return
			}
			this.page = this.page + 1;

			let params = {
				id: this.cid,
				type: this.type,
			}


			getImgListByCategory(this.page, this.limit, params).then(res => {
                 
				this.dataList.push(...res.data.records)
			})

		},
		getImgInfo(e) {
			
			if (uni.getStorageSync("userInfo").id != null || uni.getStorageSync("userInfo").id != '') {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.mid = e.id

				addBrowseRecord(data).then(res => {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + e.id
					})
				})

			} else {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + e.id
				})
			}
		},

	}
}
</script>

<style scoped>
a {
	text-decoration: none;
	color: black;
}

.page {
	margin: auto;
	width: 95%;
	height: 95vh;
}

.nav {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-right: 24rpx;
	height: 80rpx;
}

.n-left {
	display: flex;
	align-items: center;
}

.nav a {
	margin-left: 10px;
	display: inline-block;
	font-size: 20px;
}


.more-icon {

	position: relative;

}

.show {
	display: block;
}

.hidden {
	display: none;
}

.alldel {

	position: absolute;
	left: -80rpx;
	top: 70rpx;
	width: 140rpx;
	height: 50rpx;
	z-index: 9999;
	font-size: 24rpx;
	text-align: center;
	line-height: 50rpx;
	background-color: #fff;
}

image {
	width: 100%;
	height: 400rpx;
	border-top-left-radius: 8px;
	border-top-right-radius: 8px;

}

.tui-grid {
	padding: 3px 3px;

}

.card {
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

.content {
	width: 300rpx;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	font-size: 28rpx;
}

.card .del-title {
	position: absolute;
	top: 340rpx;
	left: 10rpx;
	z-index: 999;
}

.del-cicle {
	width: 20px;
	height: 20px;
	border-radius: 50%;
	border: 1px solid #fff;
}


.card .card-nums {
	position: absolute;
	top: 340rpx;
	left: 320rpx;
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
	font-size: 24rpx;
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
}

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}

.fotter {
	position: fixed;
	display: flex;
	justify-content: space-between;
	align-items: center;
	left: 0px;
	bottom: 0px;
	width: 100%;
	height: 80px;
	background-color: #fff;
	z-index: 999;
}

.f-left {
	margin-left: 10px;
	display: flex;
}

.f-del-cicle {
	width: 20px;
	height: 20px;
	border-radius: 50%;
	border: 1px solid #828282;
}

.f-right {
	width: 140rpx;
	margin-right: 10px;
}</style>
