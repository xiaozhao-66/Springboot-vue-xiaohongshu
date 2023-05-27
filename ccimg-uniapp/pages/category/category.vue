<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="head">
				<view class="tui-search-box" @click="toSearch">
					<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>
					<view class="tui-search-text" >请输入内容</view>
				</view>
			</view>
		</tui-navigation-bar>

		<view class="main">
			
			
			<ul>
				<li :class="T==index?'item-left activated':'item-left'" v-for="(category, index) in categoryList" :key="index" @click="getCategoryTwo(category,index)">
				    <view :class="T==index?'item-box box-activated':'item-box'">
						{{ category.name }}
					</view>
				</li>
			</ul>
			
			
			<!-- <view>
				<tui-list-view  class="left">
					
	
					<tui-list-cell v-for="(category, index) in categoryList" :key="index" @click="getCategoryTwo(category)">
						{{ category.name }}
					</tui-list-cell>
				</tui-list-view>
			</view> -->
			<view class="right">
				<view class="r-head">
					<view class="r-h-item">
						<view>
							<image :src="category.cover" mode="aspectFill" :lazy-load='true' @click="getImgData(category.id,1)" />
						</view>
						<p>随便看看</p>
					</view>
					<view class="r-h-item">
						<view>
							<image :src="category.hotCover" mode="aspectFill" :lazy-load='true' @click="getImgData(category.id,2)"  />
						</view>
						<p>热门推荐</p>
					</view>

				</view>
				<view class="r-category">
					<view class="r-c-top">
						<view class="r-c-top-left">分类</view>
						<view class="r-c-top-right">更多<tui-icon name="arrowright" size="18" color="#ec2124"></tui-icon></view>
					</view>
					<view class="r-c-main">

						<tui-grid :unlined = 'true'>
							<block v-for="(item, index) in category.children" :key="index">
								<tui-grid-item :cell="2" :border="false" :bottomLine="false">
									<view class="card">
										<view @click="getImgData(item.id,3)">
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
						<tui-grid :unlined = 'true'>
							<block v-for="(item, index) in category.children" :key="index">
								<tui-grid-item :cell="4" :border="false" :bottomLine="false">
									<view class="card">
										<image :src="item.cover" mode="aspectFill" :lazy-load='true'/>
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

<script >
import { getCategory } from "@/api/category.js"
export default {
	components: {

	},
	data() {
		return {
			current: 0,
			categoryList: [],
			category: {},
			T:0,
		}
	},
	onLoad() {

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

		getCategoryTwo(category,index) {
            this.T = index
			console.log(index)
			this.category = category
		},

		getImgData(id,type) {
			uni.navigateTo({
				url: "/pages/category/categoryList/categoryList?cid=" + id+"&type="+type
			})
		},
	
		toSearch(){
			uni.navigateTo({
				url: "/pages/index/search/search"
			})
		}

	}
}
</script>

<style scoped>
ul{
	padding: 0;
}
	
li {
	list-style: none;
}

.content .head {
	margin-top: 10rpx;
	position: relative;
	height: 80rpx;
}


.tui-search-box {
	width: 95%;
	height: 32px;
	margin: 0 28rpx;
	border-radius: 18px;
	font-size: 14px;
	background-color: #f1f1f1;
	padding: 0 12px;
	box-sizing: border-box;
	color: #bfbfbf;
	display: flex;
	align-items: center;
}

.content .head .search {
	margin-left: 40rpx;
	margin-top: 10rpx;
}

.content .head .search-input {
	position: absolute;
	width: 500rpx;
	height: 60rpx;
	background-color: #f1f1f1;
	padding-left: 10px;
	border-radius: 15px;
}

.content .head button {
	position: absolute;
	left: 580rpx;
	width: 120rpx;
	height: 60rpx;
	background-color: #ec2124;
	font-size: 14px;
	color: #fff;
	line-height: 60rpx;
}

.content .main {
	display: flex;
}



.item-left{
	padding-top: 10rpx;
	width: 180rpx;
	height: 80rpx;
	background-color: #f8f8f8;
	display: flex;
	align-items: center;
}

.activated{
	color: #ec2124;
	background-color: #fff;
}

.item-left .item-box{
	 width: 95%;
	 margin: auto;
	 height: 40rpx;
	 font-size: 32rpx;
	 text-align: center;
	 
}

.box-activated{
	 border-left: 2px solid;
}








.content .main .left {
	width: 140rpx;
}

.right .r-head {
	
	height: 250rpx;
}

.right .r-head .r-h-item {
	float: left;
	margin-left: 20rpx;
	font-size: 28rpx;
}

p {
	text-align: center;
}

image {
	width: 260rpx;
	height: 180rpx;
	border-radius: 8px
}

.r-category .r-c-top {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-left: 20rpx;
	margin-right: 20rpx;
}

	
.r-category .r-c-top-left::before{
	content: "|";
	color: #ec2124;
	font-weight: bold;
}

.r-category .r-c-top-right{
	color: #e84d74;
	font-size: 28rpx;
	display: flex;
	align-items: center;
}



.r-category .r-c-main .tui-grid {
	font-size: 28rpx;
	padding: 5px;
	
}

.r-autor .r-a-top {
	margin-left: 20rpx;
}

.r-autor .r-a-main image {
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%
}

.r-autor .r-a-main .card {
	text-align: center;
}

</style>
