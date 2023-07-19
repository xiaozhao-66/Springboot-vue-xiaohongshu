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
			<view class="title2 w">请上传一张新的图片</view>
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

				<tui-button type="danger" shape="circle" @click="publish" limit='1'
					:disabled="disabled">点击发布</tui-button>
			</view>
		</view>

		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		appConfig
	} from '@/config/config'
	import {
		getUserInfo,
		updateUser
	} from "@/api/user.js"
	export default {
		data() {
			return {
				value: '',
				uid: '',
				type: 0,
				userInfo: {},
				disabled: true,
			}
		},
		onLoad(options) {
			this.uid = uni.getStorageSync("userInfo").id
			this.type = options.type
			this.getUserInfo(this.uid, this.type)
		},

		methods: {
			back() {
				uni.navigateBack({
					delta: 1
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
					success: function(res) {
						that.value = res.tempFilePaths[0]
						that.disabled = false
					}
				})
			},
			publish() {

				let that = this
				
				that.disabled = true

				uni.uploadFile({
					url: appConfig.WEB_API + '/util/fileoss/uploadOssFile/2',
					filePath: that.value,
					name: 'file',
					success: (uploadFileRes) => {
						let path = JSON.parse(uploadFileRes.data).data

						if (that.type == 1) {
							//更换头像
							that.userInfo.avatar = path
						} else {
							//更换封面
							that.userInfo.cover = path
						}

						updateUser(that.userInfo).then(res => {
							let params = {
								title: "修改成功",
								imgUrl: "/static/images/toast/check-circle.png",
								icon: true
							}
							that.disabled = false
							that.$refs.toast.show(params);
							let newUser = uni.getStorageSync("userInfo")
							newUser.avatar = path
							uni.setStorageSync("userInfo", newUser)
							setTimeout(() => {
								uni.switchTab({
									url: "/pages/user/user"
								})
							}, 1000)
						})
					}
				});

			},

		}
	}
</script>

<style scoped>
	@import url(./updateImg.css);
</style>