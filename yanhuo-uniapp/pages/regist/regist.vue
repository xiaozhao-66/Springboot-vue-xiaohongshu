<template>
	<view class="container">
		<view class="top">
			<view class="title">注册</view>
		</view>
		<view class="main">
			<view class="center">
				<input placeholder="输入手机号/邮箱注册" class="info-input" v-model="value" />
				<view class="code">
					<input placeholder="验证码输入" class="info-input" v-model="code" />
					<tui-button type="white" width="200rpx" :link="true" height="60rpx" @click="getCode()"
						v-if="T">验证码</tui-button>
					<tui-tag type="white" v-else>{{ count }}</tui-tag>
				</view>
			</view>

			<view class="regist">
				<tui-button @click="regist" type="danger" shape="circle">注册</tui-button>
			</view>
		</view>
		<view></view>
	</view>
</template>

<script>
	import {
		sendDm,
		sendMsm,
		register,
		isRegist
	} from "@/api/regist.js"
	import {
		tokenUtil
	} from "@/utils/token.js"
	import {
		isMobile,
		isEmail
	} from '@/utils/validate.js'

	export default {
		data() {
			return {
				T: true,
				count: 0,
				userInfo: {},
				value: '',
				code: '',

			}
		},
		methods: {
			async getCode() {

				let isM = isMobile(this.value)
				let isE = isEmail(this.value)
				let isR = false

				if (isM) {
					//验证当前手机号是否被注册
					this.userInfo.phone = this.value

					await isRegist(this.userInfo).then(res => {
						if (res.data) {

							uni.showToast({
								title: "手机号已经被注册"
							})
							isR = true
						} else {
							let data={}
							data.phone = this.value
							sendMsm(data).then(res => {
								uni.showToast({
									title: "发送成功"
								})
							})
						}
					})


				} else if (isE) {
					//验证当前邮箱是否被注册
					this.userInfo.email = this.value

					await isRegist(this.userInfo).then(res => {
						if (res.data) {
							uni.showToast({
								title: "邮箱已经被注册"
							})
							isR = true
						} else {
							let data={}
							data.email = this.value
							sendDm(data).then(res => {
								uni.showToast({
									title: "发送成功"
								})
							})
						}
					})
				} else {
					uni.showToast({
						title: "请输入正确手机号或邮箱"
					})
					return
				}

				if (!isR) {
					this.T = false
					this.count = 60
					var times = setInterval(() => {
						this.count--; //递减
						if (this.count <= 1) {
							this.T = true
							clearInterval(times);
						}
					}, 1000)
				}
			},
			regist() {

				let isM = isMobile(this.value)
				let isE = isEmail(this.value)

				this.userInfo.code = this.code
				if (isM) {
					this.userInfo.phone = this.value
				} else if (isE) {
					this.userInfo.email = this.value
				} else {
					uni.showToast({
						title: "请输入正确手机号或邮箱"
					})
					return
				}

				register(this.userInfo).then(res => {
					if (res.data.res === '1') {
						uni.showToast({
							title: res.data.message
						})
						let user = res.data.userInfo
						tokenUtil.set(res.data.Jwt_token)
						uni.setStorageSync("userInfo", user)

						setTimeout(() => {
							uni.reLaunch({
								url: "/pages/index/index"
							});
						}, 500)
					} else {
						uni.showToast({
							title: res.data.message
						})
					}
				})
			}
		}
	}
</script>

<style scoped>
	@import url(./regist.css);
</style>