<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="24"></tui-icon></view>
					<view>用户专辑</view>
				</view>
			</view>
		</tui-navigation-bar>
		<view class="main">

			<view class="albums">
				<ul>
					<li :class="T == index ? 'item-activate' : ''" v-for="(item, index) in dataList" :key='index'>
						<view @click="selectOne(item.id, index)" class="album-body">
							<image :src="item.cover" mode="aspectFill" />
							<view class="content">{{ item.name }}</view>
						</view>
					</li>
				</ul>
			</view>
			<view class="add-album">
				<view class="title">点击快速创建专辑</view>
				<view class="add-album-body">
					<input class="add-input" placeholder="请输入专辑名称 " v-model="album.name" />
					<tui-button type="danger" height="66rpx" width="120rpx" @click="saveAlbum" :size="24">创建专辑</tui-button>
				</view>
			</view>
		</view>
		<view class="fotter">
			<view class="add">
				<tui-button type="danger" shape="circle" @click="publish" v-if="mid == ''"
					:disabled="isDisabled">点击发布</tui-button>
				<tui-button type="danger" shape="circle" @click="saveImgToAlbum" v-else>添加至专辑</tui-button>
			</view>
		</view>
		<tui-toast ref="toast"></tui-toast>

	</view>
</template>

<script>
import { publish } from "@/api/imgDetail.js"
import { saveAlbum, getAllAlbum, addAlbumImgRelation } from "@/api/album.js"
import { updateImgDetail } from '@/api/imgDetail.js'
import { appConfig } from '@/config/config'
export default {
	data() {
		return {
			T: -1,
			imgInfo: {
				albumId:'',
			},
			album: {},
			dataList: [],
			userInfo: {},
			imageList: [],
			imgurl: [],
			mid: '',
			isDisabled: false,
			type: '',
		}
	},
	onLoad(option) {
		if (typeof  option.mid != 'undefined' && option.mid != null) {
			this.mid = option.mid
		}

		if (option.type != null) {
			this.type = option.type
		}

	},
	created() {
		let that = this
		that.userInfo = uni.getStorageSync("userInfo")

		if (typeof that.userInfo == 'undefined' || that.userInfo == null || that.userInfo == '') {
			return
		} else {
			that.getAllAlbum()
			that.imgInfo = JSON.parse(uni.getStorageSync('imgInfo'))  
		}
	},
	methods: {

        back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAllAlbum() {
			let data = {
				uid: this.userInfo.id
			}
			getAllAlbum(data).then(res => {

				this.dataList = res.data
			})
		},

		selectOne(id, index) {
			this.T = index
			this.imgInfo.albumId = id
		},
		publish() {
			let that = this

			if (typeof that.imgInfo.albumId == 'undefined' || that.imgInfo.albumId == null) {
				let params = {
					title: "请选择一份专辑",
				}
				that.$refs.toast.show(params);
			} else {
				that.isDisabled = true
				that.imageList = JSON.parse(uni.getStorageSync("imgList"))

				that.imgurl = that.imageList.map((value, index) => {
					return {
						name: 'images' + index,
						uri: value
					};
				});

				let params = {
					title: "上传中",
					icon: false
				}

				that.$refs.toast.show(params);


				if (that.type == 'update') {

					that.imgInfo.count = that.imgInfo.imgsUrl.length
					that.imgInfo.imgsUrl = JSON.stringify(that.imgInfo.imgsUrl)

					updateImgDetail(that.imgInfo).then(res => {
						let params = {
							title: "修改成功",
							imgUrl: "/static/images/toast/check-circle.png",
							icon: true
						}

						that.$refs.toast.show(params);

						setTimeout(() => {
							that.isDisabled = false
							uni.reLaunch({
								url: "/pages/index/index?currentTab=" + 0
							});
						}, 1000)
					})


				} else {

					uni.uploadFile({
						url: appConfig.WEB_API + '/utils/fileoss/uploadOssFiles', //仅为示例，非真实的接口地址
						files: that.imgurl,
						formData: { num: that.imgurl.length },
						success: (res) => {

							if (typeof res.data === 'string') {
								res.data = JSON.parse(res.data);
							}

							if (res.data.code === 0) {
								that.imgInfo.cover = res.data.data[0]
								that.imgInfo.imgsUrl = JSON.stringify(res.data.data)
								that.imgInfo.count = res.data.data.length
								publish(that.imgInfo).then(res => {


									uni.removeStorage({
										key: "imgInfo"
									})

									uni.removeStorage({
										key: "tags"
									})


									let params = {
										title: "上传成功",
										imgUrl: "/static/images/toast/check-circle.png",
										icon: true
									}

									that.$refs.toast.show(params);

									setTimeout(() => {
										that.isDisabled = false
										uni.reLaunch({
											url: "/pages/index/index?currentTab=" + 0
										});
									}, 1000)

								})

							} else {
								uni.showModal({
									content: '提交失败',
									showCancel: false
								});
							}
						}
					});
				}

				uni.removeStorageSync("imgList")
				uni.removeStorageSync("tags")
				uni.removeStorageSync("imgInfo")
			}
		},
		saveAlbum() {

			this.album.cover = "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/20/4bae43e216124cf1966a474594a81612img_nodata.png"
			this.album.uid = this.userInfo.id
			saveAlbum(this.album).then(res => {
				let params = {
					title: "添加成功",
					imgUrl: "/static/images/toast/check-circle.png",
					icon: true
				}
				this.$refs.toast.show(params);
				this.album.name = ''
				this.getAllAlbum(this.album.uid)
			})
		},

		saveImgToAlbum() {
			let albumImgRelationDTO = {}
			albumImgRelationDTO.uid = this.userInfo.id
			albumImgRelationDTO.aid = this.imgInfo.albumId
			albumImgRelationDTO.mid = this.mid
			addAlbumImgRelation(albumImgRelationDTO).then(res => {
				let params = {
					title: "收藏成功",
					imgUrl: "/static/images/toast/check-circle.png",
					icon: true
				}
				this.$refs.toast.show(params);

				uni.navigateTo({
					url: "/pages/main/main?mid=" + this.mid
				})
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

.nav {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-right: 24rpx;
	height: 80rpx;
}

.n-left {
	display: flex;
	align-items: center;
}


image {
	width: 120rpx;
	height: 120rpx;
	border-radius: 10px;
}

ul {
	padding: 0rpx;
}

li {
	list-style: none;
}

.main {
	border-top: 1px solid #f4f4f4;
}

.albums {
	margin-left: 40px;
}

.album-body {
	display: flex;
	align-items: center;
	margin-top: 10px;
}

.item-activate {
	background-color: #f4f4f4;
}

.content {
	margin-left: 10px;
	font-size: 28rpx;
}

.add-album {
	border-top: 1px solid #f4f4f4;
	margin-top: 10px;
	padding-top: 10px;
}

.title {
	color: #5c5c5c;
	margin-left: 40px;
	font-size: 28rpx;
}

.fotter {
	position: fixed;
	display: flex;
	align-items: center;
	left: 0px;
	bottom: 0px;
	width: 100%;
	height: 50px;
	background-color: #fff;
	z-index: 999;
}

.add {
	width: 80%;
	margin: auto;
}

.add-album-body {
	margin-top: 10px;
	display: flex;
	margin-left: 40px;
}

.add-input {
	width: 400rpx;
	height: 66rpx;
	background-color: #f4f4f4;
	border-radius: 8px;
	padding-left: 5px;
	font-size: 28rpx;
}</style>
