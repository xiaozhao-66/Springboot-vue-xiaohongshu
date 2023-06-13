<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
				<h3>修改密码</h3>
			</view>
		</tui-navigation-bar>

		<view class="main" v-if="!check">
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
				<tui-button @click="next()" type="danger" shape="circle">下一步</tui-button>
			</view>
		</view>

		<view class="main" v-else>
			<input placeholder="输入密码" class="info-input" type="password" v-model="password" />
			<input placeholder="重新输入密码" class="info-input" type="password" v-model="checkPassword" />

			<view class="regist">
				<tui-button @click="updatePassword" type="danger" shape="circle">修改密码</tui-button>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		sendDm,
		sendMsm,
		register,
		check,
		updatePassword
	} from "@/api/regist.js"
	export default {
		data() {
			return {
				T: true,
				check: false,
				count: 0,
				userInfo: {},
				value: '',
				code: '',
				password: '',
				checkPassword: '',
			}
		},

		created() {
			this.userInfo = uni.getStorageSync("userInfo")
		},

		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},

			isMobile(s) {
				return /^1[0-9]{10}$/.test(s)
			},
			isEmail(s) {
				return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
			},

			validatorPassword(s) {
				return /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/.test(s)
			},

			getCode() {

				let isphone = this.isMobile(this.value)
				let isEmail = this.isEmail(this.value)


				if (isphone) {
					sendMsm(this.value).then(res => {
						uni.showToast({
							title: "发送成功"
						})
					})
				} else if (isEmail) {
					sendDm(this.value).then(res => {
						uni.showToast({
							title: "发送成功"
						})
					})
				} else {
					uni.showToast({
						title: "请输入正确手机号或邮箱"
					})
					return
				}

				this.T = false
				this.count = 60
				var times = setInterval(() => {
					this.count--; //递减
					if (this.count <= 1) {
						this.T = true
						clearInterval(times);
					}
				}, 1000)

			},



			next() {

				let isphone = this.isMobile(this.value)
				let isEmail = this.isEmail(this.value)
				this.userInfo.code = this.code
				if (isphone) {
					this.userInfo.phone = this.value
				} else if (isEmail) {
					this.userInfo.email = this.value
				}
				this.userInfo.type = 0

				check(this.userInfo).then(res => {

					if (res.data.res === 1) {
						this.check = true
					} else {
						uni.showToast({
							title: res.data.message
						})
					}
				})


			},

			updatePassword() {

				let pwd = this.validatorPassword(this.password)

				if (!pwd) {
					uni.showToast({
						title: "请输入8-20位由字母和数字组成的密码"
					})
					return
				}

				if (this.password !== this.checkPassword) {
					uni.showToast({
						title: "两次输入的密码不一致"
					})
					return
				}

				this.userInfo.password = this.password
				this.userInfo.checkPassword = this.checkPassword
				updatePassword(this.userInfo).then(res => {

					if (res.data) {
						uni.showToast({
							title: "修改成功"
						})
						setTimeout(() => {
							uni.reLaunch({
								url: "/pages/user/user"
							});
						}, 1000)
					}
				})
			}

		}
	}
</script>

<style scoped>
	@import url(./updatePassword.css);
</style>