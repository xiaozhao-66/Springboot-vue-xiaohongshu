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
					<tui-button type="danger" height="66rpx" width="120rpx" @click="saveAlbum"
						:size="24">创建专辑</tui-button>
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
	import {
		saveAlbum,
		getAllAlbum,
		addAlbumImgRelation
	} from "@/api/album.js"
	import {
		publish,
		updateImgDetail,
		updateStatus
	} from '@/api/imgDetail.js'
	import {
		appConfig
	} from '@/config/config'
	import {
		isURL
	} from '@/utils/validate.js'
	export default {
		data() {
			return {
				T: -1,
				imgInfo: {
					albumId: '',
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
			if (typeof option.mid != 'undefined' && option.mid != null) {
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

			back() {
				uni.navigateBack({
					delta: 1
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

					uni.showLoading({
						title: '上传中...'
					});


					that.imgInfo.cover = that.imgurl[0].uri
					that.imgInfo.count = that.imgurl.length


					let paths = []
					that.imgurl.forEach(e => {
						paths.push(e.uri)
					})

					that.imgInfo.imgsUrl = JSON.stringify(paths)


					delete that.imgInfo.categoryStr

					if (that.type == 'update') {


						let arr = []
						//遍历paths中属于https类型的图片
						paths.forEach(item => {
							if (isURL(item)) {
								arr.push(item)
							} else {
								arr.push(0)
							}
						})

						uni.uploadFile({
							url: appConfig.WEB_API + '/util/fileoss/uploadOssFiles/2',
							files: that.imgurl,
							formData: {
								num: that.imgurl.length
							},
							success: (res) => {

								if (typeof res.data === 'string') {
									res.data = JSON.parse(res.data);
								}

								if (res.data.code === 200) {

									let imgs = res.data.data
									let index = 0

									for (var i = 0; i < arr.length; i++) {
										if (arr[i] == 0) {
											//删除然后代替
											arr.splice(i, 1, imgs[index])
											index++
										}
									}

									let imgDetailDTO = {}

									imgDetailDTO.id = that.imgInfo.id
									imgDetailDTO.categoryId = that.imgInfo.categoryId
									imgDetailDTO.categoryPid = that.imgInfo.categoryPid
									imgDetailDTO.content = that.imgInfo.content
									imgDetailDTO.albumId = that.imgInfo.albumId
									imgDetailDTO.album = that.imgInfo.album
									imgDetailDTO.count = that.imgInfo.count
									imgDetailDTO.tags = that.imgInfo.tags

									imgDetailDTO.cover = arr[0]

									imgDetailDTO.imgsUrl = JSON.stringify(arr)
									imgDetailDTO.status = 1
									imgDetailDTO.type = 1

									updateImgDetail(imgDetailDTO).then(res => {
										uni.hideLoading();
										uni.showToast({
											title: '修改成功',

										});

										setTimeout(() => {
											that.isDisabled = false
											uni.reLaunch({
												url: '/pages/user/user'
											})
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


					} else {

						uni.uploadFile({
							url: appConfig.WEB_API + '/util/fileoss/uploadOssFiles/2',
							files: that.imgurl,
							formData: {
								num: that.imgurl.length
							},
							success: (res) => {

								if (typeof res.data === 'string') {
									res.data = JSON.parse(res.data);
								}

								console.log(res.data)

								if (res.data.code === 200) {

									that.imgInfo.cover = res.data.data[0]
									that.imgInfo.imgsUrl = JSON.stringify(res.data.data)
									that.imgInfo.status = 1
									that.imgInfo.type = 0

									publish(that.imgInfo).then(res => {

										uni.hideLoading();
										uni.showToast({
											title: '上传成功',

										});

										setTimeout(() => {
											that.isDisabled = false
											uni.reLaunch({
												url: '/pages/user/user'
											})
										}, 1000)
									})
								}


							}
						})
					}


					uni.removeStorageSync("imgList")
					uni.removeStorageSync("tags")
					uni.removeStorageSync("imgInfo")
				}
			},

			saveAlbum() {

				this.album.cover =
					"https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/20/4bae43e216124cf1966a474594a81612img_nodata.png"
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
	@import url(./addalbum.css);
</style>