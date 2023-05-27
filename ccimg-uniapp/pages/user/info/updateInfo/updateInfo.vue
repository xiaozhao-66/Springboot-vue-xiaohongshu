<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
					<h3 v-if="type == 1">修改昵称</h3>
					<h3 v-else>修改简介</h3>
				</view>

				<view><tui-icon name="more-fill" size="22"></tui-icon></view>
			</view>
		</tui-navigation-bar>

		<view class="update-info">
			<view class="title2 w">请输入内容</view>
			<view class="update">
				<textarea class="textarea" placeholder="请输入内容~" v-model="value">
				</textarea>
			</view>
		</view>


		<view class="fotter">
			<view class="add">

				<tui-button type="danger" shape="circle" @click="publish" limit='1'>点击修改</tui-button>

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
			type: 0,
			value: '',
			uid: '',
		}
	},
	onLoad(options) {
		this.type = options.type
		this.uid = uni.getStorageSync("userInfo").id
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
					this.value = res.data.username
				} else {
					this.value = res.data.description
				}

			})
		},

		publish() {

			if (this.type == 1) {
				//更换昵称
				this.userInfo.username = this.value
			} else {
				//更换简介
				this.userInfo.description = this.value
			}


			updateUser(this.userInfo).then(res => {

				let params = {
					title: "修改成功",
					imgUrl: "/static/images/toast/check-circle.png",
					icon: true
				}
				this.$refs.toast.show(params);
				uni.setStorageSync("userInfo", res.data)
				setTimeout(() => {

					uni.switchTab({
						url: "/pages/user/user"
					})
				}, 1000)
			})


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

.update {
	border: 2px solid #f4f4f4;
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

.update-info {
	margin-left: 40px;
	margin-right: 40px;
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
