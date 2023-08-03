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
								<view class="del-cicle" v-if="isshow && !delMids.includes(item.id)">
									<tui-icon name="circle" color="#f4f4f4" size="24"
										@click="delAffirm(item.id)"></tui-icon>
								</view>
								<view class="del-affirm" v-if="isshow && delMids.includes(item.id)"><tui-icon
										name="circle-fill" color="#ff626c" size="24"
										@click="delCancel(item.id)"></tui-icon>
								</view>
							</view>
							<view @click="toMain(item.id)">
								<image class="cover" :src="item.cover" mode="aspectFill" :lazy-load="true" />
							</view>
							<view class="card-nums">
								{{ item.count }}
							</view>
							<view class="cont">
								<view class="content">{{ item.content }}</view>
								<view class="userInfo">
									<view class="avatar-usrname">
										<image :src="item.avatar" mode="aspectFill" :lazy-load="true" />
										<view class="username">{{ item.username }}</view>
									</view>
									<view class="agreeCount"><tui-icon name="star" size="10"></tui-icon>{{ item.agreeCount
									}}
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
				<view class="f-del-cicle" v-if="T">
					<tui-icon name="circle" color="#d6d6d6" size="28" @click="isAll"></tui-icon>
				</view>
				<view class="del-affirm" v-else><tui-icon name="circle-fill" color="#ff626c" size="28"
						@click="cancalAll"></tui-icon></view>
				全选
			</view>
			<view class="f-right">
				<tui-button type="danger" height="60rpx" @click="delRecord" :size='28'>删除</tui-button>
			</view>
		</view>

		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		getAllBrowseRecordByUser,
		delRecord,
		addBrowseRecord
	} from "@/api/browseRecord.js"
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
			back() {
				uni.navigateBack({
					delta: 1
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
				this.dataList.forEach(item => {
					this.delMids.push(item.id)
				})

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
				delRecord(this.delMids, this.userInfo.id).then(res => {
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
	@import url(./history.css);
</style>