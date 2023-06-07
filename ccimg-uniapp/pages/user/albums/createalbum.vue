<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<tui-icon name="arrowleft" size="24" @click="back"></tui-icon>
					<view v-if="isupdate">修改专辑</view>
					<view v-else>创建专辑</view>
				</view>

				<view><tui-icon name="more-fill" size="20"></tui-icon></view>
			</view>
		</tui-navigation-bar>

		<view class="add-album">
			<view class="title1 w">请输入标题</view>
			<view class="add-album-body">
				<input class="add-input" placeholder="请输入专辑名称 " v-model="albumInfo.name" />
			</view>
		</view>

		<view class="upload-img" @click="chooseImg">
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

		<view class="delAlbum" v-if="isupdate">
			<tui-button width="200rpx" height="80rpx" type="danger" shape="circle" @click="delAlbum">删除专辑</tui-button>
		</view>

		<view class="fotter">
			<view class="add">

				<tui-button type="danger" shape="circle" @click="publish" limit='1'  :disabled="disabled">点击发布</tui-button>

			</view>
		</view>


		<tui-toast ref="toast"></tui-toast>
		<!-- 弹框 -->
		<tui-modal :show="modal" @click="handleClick" @cancel="hide" content="确定删除所有专辑中的数据吗" color="#333" :size="32"
			shape="circle"></tui-modal>
	</view>
</template>

<script>
import { appConfig } from '@/config/config'
import { saveAlbum, getAlbum, updateAlbum, deleteAlbum } from '@/api/album.js'
export default {
	data() {
		return {
			albumInfo: {},
			serverUrl: appConfig.WEB_API + '/utils/fileoss/uploadOssFile',
			value: '',
			imageData: [],
			uid: '',
			albumId: '',
			isupdate: false,
			modal: false,
			disabled:false
			
		}
	},
	onLoad(option) {

		this.uid = uni.getStorageSync("userInfo").id

		if (option.albumId != null) {
			this.isupdate = true;
			this.getAlbum(option.albumId)
		}

	},

	methods: {
		back(){
			uni.navigateBack({
				delta:1
			})
		},
		getAlbum(albumId) {

			let params = {
				id: albumId
			}
			getAlbum(params).then(res => {
				this.albumInfo = res.data
				this.value = res.data.cover
			})

		},
		chooseImg() {
			let that = this
			uni.chooseImage({
				count: 1, // 最多可以选择的图片张数，默认9
				sizeType: ['original'], //original 原图，compressed 压缩图，默认二者都有
				sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
				success: function (res) {
					
					that.value = res.tempFilePaths[0]
					
					
				}
			})
		},
		
		uploadImg(){
			 return new Promise((res,error)=>{
				 uni.uploadFile({
				 	url: appConfig.WEB_API + '/utils/fileoss/uploadOssFile', //仅为示例，非真实的接口地址
				 	filePath: this.value,
				 	name: 'file',
				 	success: (uploadFileRes) => {

				 	  res(JSON.parse(uploadFileRes.data).data);
				 	}
				 });
		    })
		},
		
		
		
		publish() {
            let that = this
			
			
			
			if (that.albumInfo.name == '' || that.albumInfo.name == null) {
				uni.showToast({
					   title:"请输入专辑标题",
					   icon:'none'
				})
				return
			} else if (that.value == '') {
				uni.showToast({
					   title:"请上传一张图片",
					   icon:'none'
				})
				return
			}
			
			that.disabled = true
			
			let promise =   that.uploadImg()
			
			promise.then(function(res){
				
				that.albumInfo.cover = res
				that.albumInfo.uid = that.uid
	
				if (that.isupdate) {
					//修改
					updateAlbum(that.albumInfo).then(res => {
						let params = {
							title: "修改成功",
							imgUrl: "/static/images/toast/check-circle.png",
							icon: true
						}
						that.$refs.toast.show(params);
						that.albumInfo.name = ''
						that.value = ''
						setTimeout(() => {
							uni.reLaunch({
								url: "/pages/user/user?currentTab=" + 1
							})
						}, 1000)
					})
				} else {
					//发布
					saveAlbum(that.albumInfo).then(res => {
						let params = {
							title: "发布成功",
							imgUrl: "/static/images/toast/check-circle.png",
							icon: true
						}
						that.$refs.toast.show(params);
						that.albumInfo.name = ''
						that.value = ''
						setTimeout(() => {
							uni.reLaunch({
								url: "/pages/user/user?currentTab=" + 1
							})
						}, 1000)
					})
				}
				
			})
		

			
		},
		hide() {
			this.modal = false
		},

		delAlbum() {
			this.modal = true
		},
		handleClick(e) {
			let index = e.index;

			if (index == 1) {
				let params = {
					id: this.albumInfo.id,
					uid: this.uid
				}
				deleteAlbum(params).then(res => {

					let params = {
						title: "删除成功",
						imgUrl: "/static/images/toast/check-circle.png",
						icon: true
					}
					this.$refs.toast.show(params);

					setTimeout(() => {
						uni.reLaunch({
							url: "/pages/user/user?currentTab=" + 1
						})
					}, 1000)
				})
			}
			this.hide();
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
	align-items: center;
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

.delAlbum {
	margin-left: 40px;
	margin-top: 40rpx;
}
</style>
