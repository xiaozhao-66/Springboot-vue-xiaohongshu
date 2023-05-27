<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
					<h3 v-if="type == 1">上传头像</h3>
					<h3 v-else>上传封面</h3>
				</view>

				<view><tui-icon name="more-fill" size="22"></tui-icon></view>
			</view>
		</tui-navigation-bar>

		<view class="upload-img" @click="uploadImg">
			<view class="title2 w">请选择一张图片</view>
			<view class="upload">
				<view class="img-icon" v-if="value == ''">
					<tui-icon name="pic"></tui-icon>
				</view>
				<view v-else>
					<image :src="value" mode="aspectFill"></image>
				</view>
			</view>
		</view>


		<view class="fotter">
			<view class="add">

				<tui-button type="danger" shape="circle" @click="publish" limit='1'>点击发布</tui-button>

			</view>
		</view>


		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
import { appConfig } from '@/config/config'
import { getUserInfo, updateUser } from "@/api/user.js"
export default {
	data() {
		return {
			value: '',
			uid: '',
			type: 0,
			userInfo: {},
		}
	},
	onLoad(options) {

		this.uid = uni.getStorageSync("userInfo").id
		this.type = options.type
		this.getUserInfo(this.uid, this.type)
	},

	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getUserInfo(uid, type) {
			let params = {
				uid: uid
			}
			getUserInfo(params).then(res => {

				this.userInfo = res.data
				if (type == 1) {
					this.value = res.data.avatar
				} else {
					this.value = res.data.cover
				}

			})
		},
		uploadImg() {
			let that = this
			uni.chooseImage({
				count: 1, // 最多可以选择的图片张数，默认9
				sizeType: ['original'], //original 原图，compressed 压缩图，默认二者都有
				sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
				success: function (res) {
					uni.uploadFile({
						url: appConfig.WEB_API + '/utils/fileoss/uploadOssFile',
						filePath: res.tempFilePaths[0],
						name: 'file',
						success: (uploadFileRes) => {

							that.value = JSON.parse(uploadFileRes.data).data

						}
					});
				}
			})
		},
		publish() {

			if (this.type == 1) {
				//更换头像
				this.userInfo.avatar = this.value
			} else {
				//更换封面
				this.userInfo.cover = this.value
			}

			updateUser(this.userInfo).then(res => {
				let params = {
					title: "修改成功",
					imgUrl: "/static/images/toast/check-circle.png",
					icon: true
				}
				this.$refs.toast.show(params);
				setTimeout(() => {
					uni.switchTab({
						url: "/pages/user/user"
					})
				}, 1000)
			})

			if (this.isupdate) {
				//修改
				updateAlbum(this.albumInfo).then(res => {
					let params = {
						title: "修改成功",
						imgUrl: "/static/images/toast/check-circle.png",
						icon: true
					}
					this.$refs.toast.show(params);
					this.albumInfo.name = ''
					this.value = ''
					setTimeout(() => {
						uni.reLaunch({
							url: "/pages/user/user?currentTab=" + 1
						})
					}, 1000)
				})
			} else {
				//发布

				saveAlbum(this.albumInfo).then(res => {
					let params = {
						title: "发布成功",
						imgUrl: "/static/images/toast/check-circle.png",
						icon: true
					}
					this.$refs.toast.show(params);
					this.albumInfo.name = ''
					this.value = ''
					setTimeout(() => {
						uni.reLaunch({
							url: "/pages/user/user?currentTab=" + 1
						})
					}, 1000)
				})
			}


		},

	}
}
</script>

<style scoped>
.nav {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-right: 24rpx;
	height: 80rpx;
}

.n-left {
	display: flex;
}

.add-album {
	border-top: 1px solid #f4f4f4;
	margin-top: 10px;
	padding-top: 10px;
}

.add-album-body {
	margin-top: 10px;
	display: flex;
	margin-left: 40px;
}

.title1 {
	margin-left: 40px;
}

.title2 {
	margin-bottom: 10px;
}

.w {
	margin-top: 20px;
}


.add-input {
	width: 80%;
	height: 80rpx;
	background-color: #f4f4f4;
	border-radius: 8px;
	padding-left: 5px;
}

.upload-img {
	margin-left: 40px;
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

.upload {
	width: 300rpx;
	height: 300rpx;
	background-color: #f4f4f4;
	position: relative;

}

image {
	width: 300rpx;
	height: 300rpx;
}

.img-icon {
	position: absolute;
	left: 40%;
	top: 35%;
}
</style>
