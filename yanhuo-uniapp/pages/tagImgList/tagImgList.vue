<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="24"></tui-icon></view>
					<view>{{tag.name}}</view>
					<view></view>
				</view>
			</view>
		</tui-navigation-bar>
		<tui-tabs :tabs="tabs" :currentTab="type" itemWidth="50%" @change="change" :width="200" sliderBgColor="#ff0000"
			selectedColor="#ff0000"></tui-tabs>

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<!-- 使用组件做瀑布流 -->
			<uv-waterfall ref="waterfall" v-model="list" column-count="2" @changeList="changeList" column-gap="8">
				<template v-slot:list1>
					<!-- 为了磨平部分平台的BUG，必须套一层view -->
					<view>
						<uni-list :data="list1" :total='list1.length' style="background-color: #f4f4f4;">
							<view v-for="(item, index) in list1" :key="index" class="waterfall-item">

								<view class="waterfall-item__image">
									<image :src="item.image" :lazy-load="true" mode="widthFix" :width="item.width+'px'"
										@click="getImgInfo(item.id)">

									</image>
								</view>
								<view class="card">

									<view class="cont">
										<view class="content">{{item.content}}
										</view>
										<view class="userInfo">
											<view class="avatar-usrname">
												<image :src="item.avatar" :lazy-load="true" mode="aspectFill"
													class="avatar"></image>
												<view class="username">{{ item.username }}</view>
											</view>
											<view class="agreeCount"><tui-icon name="star"
													size="10"></tui-icon>{{ item.agreeCount }}</view>
										</view>
										<view class="card-nums">
											{{item.count}}
										</view>
									</view>
								</view>
							</view>
						</uni-list>


					</view>
				</template>
				<!-- 第二列数据 -->
				<template v-slot:list2>
					<!-- 为了磨平部分平台的BUG，必须套一层view -->
					<view>
						<uni-list :data='list2' :total='list2.length' style="background-color: #f4f4f4;">
							<view v-for="(item, index) in list2" :key="index" class="waterfall-item">
								<view class="waterfall-item__image">
									<image :src="item.image" :lazy-load="true" mode="widthFix" :width="item.width+'px'"
										@click="getImgInfo(item.id)">

									</image>
								</view>
								<view class="card">

									<view class="cont">
										<view class="content">{{item.content}}</view>
										<view class="userInfo">
											<view class="avatar-usrname">
												<image :src="item.avatar" :lazy-load="true" mode="aspectFill"
													class="avatar"></image>
												<view class="username">{{ item.username }}</view>
											</view>
											<view class="agreeCount"><tui-icon name="star"
													size="10"></tui-icon>{{ item.agreeCount }}</view>
										</view>

										<view class="card-nums">
											{{item.count}}
										</view>
									</view>
								</view>
							</view>

						</uni-list>

					</view>
				</template>
			</uv-waterfall>

			<!-- end -->
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
		</scroll-view>
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		addBrowseRecord
	} from "@/api/browseRecord.js"
	import {
		getOneTag,
		getImgListByTagId
	} from "@/api/tag.js"
	export default {
		data() {
			return {
				tag: {},

				tabs: [{
					name: "最新"
				}, {
					name: "热门"
				}],

				list: [],
				list1: [],
				list2: [],
				page: 1,
				limit: 8,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				type: 0,
			}
		},
		onLoad(options) {
			this.getOneTag(options.tid)
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			change(e) {
				this.type = e.index
				this.list = []
				this.list1 = []
				this.list2 = []
				this.page = 1
				this.total = 0
				this.getImgListByTagId()
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

			getOneTag(id) {
				let params = {
					id: id,
				}
				getOneTag(params).then(res => {
					this.tag = res.data
					this.getImgListByTagId()
				})
			},

			getImgListByTagId() {
				let params = {
					id: this.tag.id,
					type: this.type,
				}

				getImgListByTagId(this.page, this.limit, params).then(res => {
					this.getMoreData(res.data.records)
					this.total = res.data.total
				})
			},

			changeList(e) {
				this[e.name].push(e.value);
			},

			loadData() {

				this.loading = true

				if (this.list.length >= this.total) {
					this.isEnd = true
					return
				}
				this.page = this.page + 1;

				let params = {
					id: this.tag.id,
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
	@import url(./tagImgList.css);
</style>