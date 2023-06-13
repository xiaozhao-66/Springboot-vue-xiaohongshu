<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData">

			<WaterFall :list="list" @getImgInfo="getImgInfo"></WaterFall>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		getAllCollection
	} from "@/api/collection.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import WaterFall from '@/components/waterfall.vue'
	export default {
		components: {
			WaterFall
		},
		props: {
			uid: String,
		},
		data() {
			return {
				list: [],
				page: 1,
				limit: 8,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载

			}
		},

		created() {


			this.getAllCollection()
		},
		methods: {

			getMoreData(dataList) {
				for (let i = 0; i < dataList.length; i++) {
					this.list.push(this.getItem(i, dataList[i]));
				}
			},

			getItem(i, item) {
				return {
					id: item.collectionId,
					allowEdit: i == 0,
					image: item.cover,
					content: item.content,
					count: item.count,
					avatar: item.avatar,
					username: item.username,
					agreeCount: item.agreeCount,
				}
			},


			getAllCollection() {

				let params = {
					uid: this.uid,
					type: 0
				}
				getAllCollection(this.page, this.limit, params).then(res => {
					console.log(res.data)
					this.total = res.data.length
					this.getMoreData(res.data)
				})

			},

			loadData() {
				this.loading = true
				if (this.total < this.limit) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;

				let params = {
					uid: this.uid,
					type: 0
				}

				getAllCollection(this.page, this.limit, params).then(res => {
					console.log(res.data)
					this.total = res.data.length
					this.getMoreData(res.data)
				})

			},
			getImgInfo(mid) {

				if (uni.getStorageSync("userInfo").id != null || uni.getStorageSync("userInfo").id != '') {
					let data = {}
					data.uid = uni.getStorageSync("userInfo").id
					data.mid = mid

					addBrowseRecord(data).then(res => {
						uni.navigateTo({
							url: "/pages/main/main?mid=" + mid
						})
					})

				} else {
					uni.navigateTo({
						url: "/pages/main/main?mid=" + mid
					})
				}
			},

		}
	}
</script>

<style scoped>
	@import url(./collectionImgs.css);
</style>