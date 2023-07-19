<template>
	<view class="container">

		<view class="create" v-if="T">
			<view class="create-btn">
				<tui-button type="white" @click="createAlbum" shape="circle" width="180rpx" :size="24" height="60rpx"> +
					创建专辑</tui-button>
			</view>
		</view>
		<view class="main">
			<tui-grid :unlined="true">
				<block v-for="(item, index) in dataList" :key="index">
					<tui-grid-item :cell="2" backgroundColor="#f4f4f4" :border="false" :bottomLine="false">
						<view class="card">
							<view @click="searchAlbum(item.id)">
								<image :src="item.cover" mode="aspectFill" :lazy-load='true' />
							</view>
							<view class="cont">
								<h5>{{ item.name }}</h5>
								<p>{{ item.imgCount }}张图片 | {{ item.collectionCount }}人收藏</p>
							</view>
						</view>
					</tui-grid-item>
				</block>
			</tui-grid>
		</view>
	</view>
</template>

<script>
	import {
		getAllAlbum
	} from "@/api/album.js"
	import {
		getUserInfo
	} from "@/api/user.js"
	export default {
		props: {
			seed: Number,
			uid: String,
		},
		data() {
			return {
				userInfo: {},
				dataList: [],
				T: true
			}
		},

		watch: {
			//刷新子组件的数据
			seed(newVal, oldVal) {
				this.getAllAlbum()
			}
		},
		created() {

			if (this.uid != uni.getStorageSync("userInfo").id) {
				this.T = false
			}
			this.getUserInfo(this.uid)

		},
		methods: {

			getUserInfo(uid) {
				let params = {
					uid: uid
				}
				getUserInfo(params).then(res => {
					this.userInfo = res.data
					this.getAllAlbum()
				})
			},

			getAllAlbum() {
				let params = {
					uid: this.userInfo.id,
				}
				getAllAlbum(params).then(res => {
					this.dataList = res.data
				})

			},

			searchAlbum(albumId) {
				uni.navigateTo({
					url: "/pages/user/albums/albumInfo?albumId=" + albumId
				})
			},
			createAlbum() {
				uni.navigateTo({
					url: "/pages/user/albums/createalbum"
				})
			},

		}
	}
</script>

<style scoped>
	@import url(./css/albums.css);
</style>