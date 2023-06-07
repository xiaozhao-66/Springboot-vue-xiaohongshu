<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="25"></tui-icon></view>
					<h3>浏览记录</h3>
				</view>
				<view @click="delhistory" class="more-icon">
					<tui-icon name="more-fill" size="22"></tui-icon>
				</view>
			</view>
		</tui-navigation-bar>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-grid :unlined="true">
				<block v-for="(item, index) in dataList" :key="index">
					<tui-grid-item :cell="2" :border="false" :bottomLine="false">
						<view class="card">
							<view class="del-title">
								<view class="del-cicle" v-if="isshow && !delMids.includes(item.id)"
									@click="delAffirm(item.id)"></view>
								<view class="del-affirm" v-if="isshow && delMids.includes(item.id)"><tui-icon
										name="circle-fill" color="#f90000" size="28" @click="delCancel(item.id)"></tui-icon>
								</view>
							</view>
							<view @click="toMain(item.mid)">
								<image class="cover" :src="item.cover" mode="aspectFill" :lazy-load="true"/>
							</view>
							<view class="card-nums">
								{{ item.nums }}
							</view>
							<view class="cont">
								<view class="content">{{ item.content }}</view>
								<view class="userInfo">
									<view class="avatar-usrname">
										<image :src="item.avatar" mode="aspectFill"  :lazy-load="true" />
										<view class="username">{{ item.username }}</view>
									</view>
									<view class="agreeCount"><tui-icon name="star" size="10"></tui-icon>{{ item.agreeCount }}
									</view>
								</view>
							</view>
						</view>
					</tui-grid-item>
				</block>
			</tui-grid>
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
			</view>
		</view>

		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
import { getAllBrowseRecordByUser, delRecord, addBrowseRecord } from "@/api/browseRecord.js"
export default {
	data() {
		return {
			dataList: [],
			page: 1,
			limit: 12,
			total: 0,
			userInfo: {},
			isEnd: false, //是否到底底部了
			loading: false, //是否正在加载
			isshow: false,
			affirmId: '',
			delMids: [],
			T: true,
		}
	},
	created() {
		this.userInfo = uni.getStorageSync("userInfo")

		this.getAllBrowseRecordByUser()
	},
	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAllBrowseRecordByUser() {
			let params = {
				uid: this.userInfo.id
			}

			getAllBrowseRecordByUser(this.page, this.limit, params).then(res => {
				this.dataList = res.data
				this.total = res.data.length
			})
		},

		loadData() {
			this.loading = true
			setTimeout(() => {
				if (this.total < this.limit) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;
				let params = {
					uid: this.userInfo.id
				}

				getAllBrowseRecordByUser(this.page, this.limit, params).then(res => {

					this.dataList.push(...res.data)
					this.total = res.data.length
				})
			}, 1000)
		},
		toMain(mid) {
			let data = {}
			data.uid = this.userInfo.id
			data.mid = mid

			addBrowseRecord(data).then(res => {
				uni.navigateTo({
					url: "/pages/main/main?mid=" + mid
				})
			})
		},
		delhistory() {
			this.isshow = !this.isshow
			if (this.isshow == false) {
				this.delMids = []
			}
		},
		delAffirm(mid) {
			this.delMids.push(mid)

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
		delCancel(mid) {
			let index = this.delMids.indexOf(mid)
			this.delMids.splice(index, 1)

		},
		//删除历史记录
		delRecord() {
			delRecord(this.delMids).then(res => {
				let params = {
					title: "删除成功",
					imgUrl: "/static/images/toast/check-circle.png",
					icon: true
				}
				this.$refs.toast.show(params);
				this.getAllBrowseRecordByUser()
			})
		}
	}
}
</script>

<style scoped>
a {
	text-decoration: none;
	color: black;
}

.page {
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
