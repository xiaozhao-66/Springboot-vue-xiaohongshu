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
			<WaterFall :list="list" @getImgInfo="getImgInfo"></WaterFall>
			<!-- end -->
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		getImgListByCategory
	} from "@/api/category.js"
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import WaterFall from '@/components/waterfall.vue'
	export default {
		components: {
			WaterFall
		},
		data() {
			return {
				list: [],
				page: 1,
				limit: 8,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				cid: '',
				type: 0,
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
			back() {
				uni.navigateBack({
					delta: 1
				})
			},

			getMoreData(dataList) {
				dataList.forEach(item => {
					this.list.push(this.getItem(item))
				})
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

			getImgListByCategory() {
				let params = {
					id: this.cid,
					type: this.type,
				}

				getImgListByCategory(this.page, this.limit, params).then(res => {
					this.getMoreData(res.data.records)
					this.total = res.data.total
				})
			},

			loadData() {

				this.loading = true

				if (this.list.length >= this.total) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;

				let params = {
					id: this.cid,
					type: this.type,
				}
				getImgListByCategory(this.page, this.limit, params).then(res => {
					this.getMoreData(res.data.records)
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
	@import url(./categoryList.css);
</style>