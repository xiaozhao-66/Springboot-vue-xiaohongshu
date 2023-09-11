<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="head">
				<view class="tui-search-box" @click="toSearch">
					<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>
					<view class="tui-search-text">请输入内容</view>
				</view>
			</view>
		</tui-navigation-bar>

		<view class="main">
			<ul>
				<li :class="T == index ? 'item-left activated' : 'item-left'" v-for="(category, index) in categoryList"
					:key="index" @click="getCategoryTwo(category, index)">
					<view :class="T == index ? 'item-box box-activated' : 'item-box'">
						{{ category.name }}
					</view>
				</li>
			</ul>

			<view class="right">
				<view class="r-head">
					<view class="r-h-item">
						<view>
							<image :src="category.cover" mode="aspectFill" :lazy-load='true'
								@click="getImgData(category.id, 1)" />
						</view>
						<p>随便看看</p>
					</view>
					<view class="r-h-item">
						<view>
							<image :src="category.hotCover" mode="aspectFill" :lazy-load='true'
								@click="getImgData(category.id, 2)" />
						</view>
						<p>热门推荐</p>
					</view>

				</view>
				<view class="r-category">
					<view class="r-c-top">
						<view class="r-c-top-left">分类</view>
						<view class="r-c-top-right">更多<tui-icon name="arrowright" size="18" color="#ec2124"></tui-icon>
						</view>
					</view>
					<view class="r-c-main">

						<tui-grid :unlined='true'>
							<block v-for="(item, index) in category.children" :key="index">
								<tui-grid-item :cell="2" :border="false" :bottomLine="false">
									<view class="card">
										<view @click="getImgData(item.id, 3)">
											<image :src="item.cover" mode="aspectFill" :lazy-load='true' />
										</view>
										<p>{{ item.name }}</p>
									</view>
								</tui-grid-item>
							</block>
						</tui-grid>

					</view>
				</view>
				<view class="r-autor">
					<view class="r-a-top">热门作者</view>
					<view class="r-a-main">
						<tui-grid :unlined='true'>
							<block v-for="(item, index) in category.children" :key="index">
								<tui-grid-item :cell="4" :border="false" :bottomLine="false">
									<view class="card">
										<image :src="item.cover" mode="aspectFill" :lazy-load='true' />
										<p>{{ item.name }}</p>
									</view>
								</tui-grid-item>
							</block>
						</tui-grid>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getCategory
	} from "@/api/category.js"
	export default {
		data() {
			return {
				current: 0,
				categoryList: [],
				category: {},
				T: 0,
			}
		},
		created() {
			this.getCategory()
		},
		methods: {
			getCategory() {
				getCategory().then(res => {
					this.categoryList = res.data
					this.category = res.data[0]

				})
			},

			getCategoryTwo(category, index) {
				this.T = index
				this.category = category
			},

			getImgData(id, type) {
				uni.navigateTo({
					url: "/pages/category/categoryList/categoryList?cid=" + id + "&type=" + type
				})
			},

			toSearch() {
				uni.navigateTo({
					url: "/pages/index/search/search"
				})
			}

		}
	}
</script>

<style scoped>
	@import url(./category.css);
</style>