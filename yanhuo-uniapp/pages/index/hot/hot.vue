<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			
				<view class="item-hot" v-for="(item, index) in dataList" :key="index">
					<view class="item-hot-left">{{ index + 1 }}</view>
					<view class="item-hot-right">
						<view class="item-hot-top">
							<view class="top-left">
								<view class="avatar" @click="getUserInfo(item.userId)">
									<image :src="item.avatar" mode="aspectFill" :lazy-load='true' />
								</view>
								<view class="username">
									{{ item.username }}
								</view>
							</view>
							<view class="top-right">
								{{ item.agreeCount }} <tui-icon name="like-fill" size="14" color="#ff5500"></tui-icon>
							</view>
						</view>

						<view class="content">
							{{ item.content }}
						</view>

						<view class="images" @click="toMain(item.id)">

							<view v-for="(img, index) in item.imgsUrl " v-show="index < 3">

								<image :src="img" mode="aspectFill" :lazy-load='true' class="fadeImg" />
							</view>

							<view class="nums" v-if="item.count > 3">+{{ item.count - 3 }}</view>
						</view>

					</view>
				</view>


			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
	</view>
</template>

<script>
	import {
		getHot
	} from '@/api/imgDetail.js'
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	export default {

		data() {
			return {
				page: 1,
				limit: 10,
				total: 0,
				dataList: [],
				isEnd: false,
			}
		},
		created() {
			this.getHot()
		},
		methods: {
			getHot() {
				getHot(this.page, this.limit).then(res => {
					this.dataList = res.data.records
					this.total = res.data.total
				})
			},
			loadData() {
				if (this.dataList.length >= this.total) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;

				getHot(this.page, this.limit).then(res => {
					this.dataList.push(...res.data.records)
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
			toMain(mid) {
				let data = {}
				data.uid = uni.getStorageSync("userInfo").id
				data.mid = mid

				addBrowseRecord(data).then(res => {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + mid
					})
				})
			},
		}
	}
</script>

<style scoped>
	@import url(./hot.css);
</style>