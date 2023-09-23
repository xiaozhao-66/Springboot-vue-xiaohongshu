<template>
	<view class="container">
			<WaterFall :list="list" @getImgInfo="getImgInfo"></WaterFall>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		getAllCollection
	} from "@/api/agreeCollect.js"
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
			seed:Number,
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
		watch:{
		    seed(newVal,oldVal){
				this.loadData()
			}	
		},

		created() {
			this.getAllCollection()
		},
		methods: {

			getMoreData(dataList) {
				dataList.forEach(item => {
					this.list.push(this.getItem(item))
				})
			},

			getItem(item) {
				return {
					id: item.mid,
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
					type: 2
				}
				getAllCollection(this.page, this.limit, params).then(res => {
					console.log(res)
					this.total = res.data.total
					this.getMoreData(res.data.records)
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
					uid: this.uid,
					type: 2
				}

				getAllCollection(this.page, this.limit, params).then(res => {
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
	@import url(./collectionImgs.css);
</style>