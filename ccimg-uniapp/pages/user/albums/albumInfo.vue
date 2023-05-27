<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
					<view>专辑详情</view>
					
				</view>
				<view ><tui-icon name="more-fill" size="20" @click="popup"></tui-icon></view>
			</view>
		</tui-navigation-bar>
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="info">
				<view class="info-center">
					<view class="title">{{ albumInfo.name }}专辑</view>
					<view class="count">{{ albumInfo.imgCount }}张图片</view>
					<view class="avatar">
						<image :src="albumInfo.avatar" mode="aspectFill" :lazy-load='true' />
					</view>
					<view class="username">{{ albumInfo.username }}</view>
				</view>
			</view>

			<view class="body">

				<tui-grid :unlined = 'true'>
					<block v-for="(item, index) in dataList" :key="index">
						<tui-grid-item :cell="2" @click="detail" :border="false" :bottomLine="false">
							<view class="card">
								<view class="del-title">
									<view class="del-cicle" v-if="isshow && !delMids.includes(item.id)"
										@click="delAffirm(item.id)"></view>
									<view class="del-affirm" v-if="isshow && delMids.includes(item.id)"><tui-icon
											name="circle-fill" color="#f90000" size="28"
											@click="delCancel(item.id)"></tui-icon></view>
								</view>

								<view @click="getImgInfo(item.id)">
									<image class="cover" :src="item.cover" mode="aspectFill"  :lazy-load='true'/>
								</view>
								<view class="card-nums">
									{{ item.count }}
								</view>
								<view class="cont">
									<view class="content">{{ item.content }}</view>
									<view class="userInfo">
										<view class="avatar-usrname">
											<image :src="item.avatar" mode="aspectFill" :lazy-load='true' />
											<view>{{ item.username }}</view>
										</view>
										<view><tui-icon name="star" size="10"></tui-icon>{{ item.agreeCount }}</view>
									</view>

								</view>
							</view>
						</tui-grid-item>
					</block>
				</tui-grid>
			</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>

		<view class="fotter" v-if="isshow">
			<view class="f-left">
				<view class="f-del-cicle" @click="isAll" v-if="T"></view>
				<view class="del-affirm" v-else><tui-icon name="circle-fill" color="#f90000" size="28"
						@click="cancalAll"></tui-icon></view>
				全选

			</view>
			<view class="f-right">
				<tui-button type="danger" height="80rpx" @click="delRecord">删除</tui-button>
				<tui-button type="gury" height="80rpx" @click="cancalDel">取消</tui-button>
			</view>
		</view>

		<!--底部分享弹窗-->
		<tui-bottom-popup backgroundColor="#f6f6f6" :zIndex="1002" :maskZIndex="1001" :show="popupShow" @close="popup">
			<view class="tui-share">
				<view class="tui-share-title">分享到</view>

				<view class="tui-share-top" v-if="isCurrentUser">

					<view  class="tui-share-item"  :class="[shareList[1].operate.length - 1 === index ? 'tui-item-last' : '']"
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
				
					 <view  class="tui-share-item"  :class="[shareList[2].operate.length - 1 === index ? 'tui-item-last' : '']"
					 	v-for="(item, index) in shareList[2].operate" :key="index" @tap="popup">
					 	<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150">
					 		<view @click="operate2(index)">
					 			<tui-icon :name="item.icon" color="#404040" :size="item.size"></tui-icon>
					 		</view>
					 	</view>
					 	<view class="tui-share-text">{{ item.name }}</view>
					 </view>
				</view>

				<view class="tui-share-bottom tui-mt">
					<view class="tui-share-item" :class="[shareList[0].share.length - 1 === index ? 'tui-item-last' : '']"
						v-for="(item, index) in shareList[0].share" :key="index" @tap="popup">
						<view class="tui-share-icon" hover-class="tui-hover" :hover-stay-time="150">
							<tui-icon :name="item.icon" :color="item.color" :size="item.size ? item.size : 36"></tui-icon>
						</view>
						<view class="tui-share-text">{{ item.name }}</view>
					</view>
					<view class="tui-empty">!</view>
				</view>


				<view class="tui-btn-cancle" @tap="popup">取消</view>
			</view>
		</tui-bottom-popup>
		<!--底部分享弹窗-->

	</view>
</template>

<script>
import { getAllImgByAlbum, deleteImgs } from "@/api/imgDetail.js"
import { getAlbum } from "@/api/album.js"
import { addBrowseRecord } from "@/api/browseRecord.js"
import { stdout } from "process"
import {collection,cancalCollection} from "@/api/collection.js"
export default {
	data() {
		return {
			albumId: '',
			dataList: [],
			albumInfo: {},
			page: 1,
			limit: 6,
			total: 0,
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			popupShow: false,
			isCurrentUser: false,
			shareList: [
				{
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
			    }, 
			{
				operate: [{
					name: "编辑专辑",
					icon: "edit",
					size: 30
				}, {
					name: "转入专辑",
					icon: "edit",
					size: 28
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
					name: "其他内容",
					icon: "immore",
					size: 28
				}]
			},
			{
				operate: [{
					name: "收藏专辑",
					icon: "edit",
					size: 30
				},
				{
					name: "取消收藏",
					icon: "offline",
					size: 28
				},
				]
			}
			
			],
			isshow: false,
			affirmId: '',
			delMids: [],
			T: true,
			uid: '',
		}

	},
	onLoad(option) {
		this.albumId = option.albumId
	},
	created() {
		this.getAlbum()
		this.getAllImgByAlbum()

	},
	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAllImgByAlbum() {
			let params = {
				albumId: this.albumId
			}

			getAllImgByAlbum(this.page, this.limit, params).then(res => {
				this.dataList = res.data.records
				this.total = res.data.total
			})
		},
		getAlbum() {
			let params = {
				id: this.albumId,
			}
			getAlbum(params).then(res => {

				this.albumInfo = res.data

				let userInfo = uni.getStorageSync("userInfo")
				this.uid = userInfo.id
				if (res.data.uid == userInfo.id) {
					this.isCurrentUser = true
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
				let params = {
					albumId: this.albumId
				}

				getAllImgByAlbum(this.page, this.limit, params).then(res => {

					this.dataList.push(...res.data.records)
				})
			}, 1000)
		},

		getImgInfo(mid) {

			let data = {}
			data.uid = uni.getStorageSync("userInfo").id
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid
				})
			})

		},

		popup() {
			this.popupShow = !this.popupShow
		},
		operate(index) {

			//编辑操作
			if (index == 0) {
				uni.navigateTo({
					url: "/pages/user/albums/createalbum?albumId=" + this.albumId
				})
			} else if (index == 2) {
				this.isshow = true

			}
		},
		
		operate2(index){
			if (index == 0) {
				console.log("收藏")
				let data = {}
				data.collectionId = this.albumId
				data.uid = uni.getStorageSync("userInfo").id
				data.type =1
				collection(data).then(res=>{
					console.log(res.data.message)
					uni.showToast({
						title: res.data.message
					})
				})
			}else if(index==1){
				console.log("取消收藏")
				let data = {}
				data.collectionId = this.albumId
				data.uid = uni.getStorageSync("userInfo").id
				data.type =1
				cancalCollection(data).then(res=>{
					uni.showToast({
						title: res.data.message
					})
				})
			}
		},

		delAffirm(mid) {
			this.delMids.push(mid)

		},
		delCancel(mid) {
			let index = this.delMids.indexOf(mid)
			this.delMids.splice(index, 1)

		},
		isAll() {
			this.T = false
			for (let i = 0; i < this.dataList.length; i++) {
				this.delMids.push(this.dataList[i].id)
			}

		},
		cancalAll() {
			this.T = true
			this.delMids = []
		},
		cancalDel() {
			this.isshow = false
			this.delMids = []
		},
		//删除
		delRecord() {

			deleteImgs(this.delMids, this.uid).then(res => {
				uni.showToast({
					title: "删除成功"
				})

				setTimeout(() => {
					this.page = 1
					this.getAllImgByAlbum()
					this.isshow = false
					this.delMids = []
				}, 1000)

			})
		}
	}
}
</script>

<style scoped>
.container {

	background-color: #f4f4f4;
}

.page {
	height: 94vh;
}

a {
	text-decoration: none;
	color: black;
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

.cover {
	width: 100%;
	height: 400rpx;
	;
}

.info {
	background-color: #fff;

}

.info-center {
	width: 400rpx;

	margin: auto;

	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
}

.title {
	font-size: 40rpx;
	margin-top: 30px;
}

.count {
	margin-top: 10px;
	font-size: 24rpx;
	color: #5c5c5c;
}

.avatar image {
	margin-top: 10px;
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
}

.username {
	margin-top: 10px;
	margin-bottom: 20px;
}

.tui-grid {
	padding: 3px 3px;

}

.card {
	position: relative;

	border-radius: 8px;
	
	box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
}

.card .cont {
	margin-left: 10px;
	padding-bottom: 4px;
}

.card .cont p {
	margin-top: 5px;
	font-size: 12px;
}

.del-title {
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
	top: 350rpx;
	left: 320rpx;
	background-color: #7a7a7a;
	width: 30rpx;
	height: 45rpx;
	text-align: center;
	color: #fff;
	opacity: 0.5;
}

.cont .content {
	width: 300rpx;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	font-size: 28rpx;
	
}

.userInfo {
	margin-top: 5px;
	margin-bottom: 5px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-right: 5px;
	font-size: 24rpx;
}

.avatar-usrname image {
	width: 60rpx;
	height: 60rpx;
	border-radius: 50%;
}

.avatar-usrname {
	display: flex;
	justify-content: space-between;
	align-items: center;

}

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}

/* 底部分享 */
.tui-share {
	background: #e8e8e8;
	position: relative;
}

.tui-share-title {
	font-size: 26rpx;
	color: #7E7E7E;
	text-align: center;
	line-height: 26rpx;
	padding: 20rpx 0 50rpx 0;
}

.tui-share-top,
.tui-share-bottom {
	min-width: 101%;
	padding: 0 20rpx 0 30rpx;
	white-space: nowrap;
}

.tui-mt {
	margin-top: 30rpx;
	padding-bottom: 150rpx;
}

.tui-share-item {
	width: 126rpx;
	display: inline-block;
	margin-right: 24rpx;
	text-align: center;
}

.tui-item-last {
	margin: 0;
}

.tui-empty {
	display: inline-block;
	width: 30rpx;
	visibility: hidden;
}

.tui-share-icon {
	display: flex;
	align-items: center;
	justify-content: center;
	background: #fafafa;
	height: 126rpx;
	width: 126rpx;
	border-radius: 32rpx;
}

.tui-share-text {
	font-size: 24rpx;
	color: #7E7E7E;
	line-height: 24rpx;
	padding: 20rpx 0;
	white-space: nowrap;
}

.tui-btn-cancle {
	width: 100%;
	height: 100rpx;
	position: absolute;
	left: 0;
	bottom: 0;
	background: #f6f6f6;
	font-size: 36rpx;
	color: #3e3e3e;
	display: flex;
	align-items: center;
	justify-content: center;
}

.tui-hover {
	background: rgba(0, 0, 0, 0.2)
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
	display: flex;
	width: 220rpx;
	margin-right: 10px;
}</style>