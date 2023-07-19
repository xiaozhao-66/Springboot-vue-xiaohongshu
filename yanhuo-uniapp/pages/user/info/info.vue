<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<tui-icon name="arrowleft" size="25" @click="back"></tui-icon>
				<h3>编辑资料</h3>
			</view>
		</tui-navigation-bar>

		<view class="body">
			<view class="body-img">
				<image :src="userInfo.cover" mode="aspectFill" />
				<view class="body-rz">
					<tui-icon name="attestation" size="20" color="#ff0000" v-if='userInfo.fanCount>0'></tui-icon>
					<tui-icon name="attestation" size="20" color="#878787" v-else></tui-icon>
					<p>获取认证</p>
				</view>
			</view>

			<view class="center">
				<view class="avatar">
					<image :src="userInfo.avatar" mode="aspectFill"></image>
				</view>
				<view class="center-button">
					<button @click="updateImg(1)">更换头像</button>
					<button @click="updateImg(2)">更换封面</button>
				</view>
			</view>
		</view>

		<view class="info">
			<ul>
				<li class="info-item">
					<view class="info-item-zhuti" @click="updateInfo(1)">
						<view class="title">昵称</view>
						<view class="cont">{{ userInfo.username }}</view>
						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>

				</li>
				<li class="info-item">
					<view class="info-item-zhuti" @click="updateInfo(2)">
						<view class="title">性别</view>
						<view class="cont">{{ userInfo.gender == 1 ? '男' : '女' }}</view>
						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>

					<tui-modal :show="sexShow" @click="updateSex" @cancel="hideSex" content="请选择性别"
						:button="radioSex"></tui-modal>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti" @click="updateInfo(3)">
						<view class="title">生日</view>
						<view class="cont">{{ userInfo.birthday }}</view>
						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>

					<tui-datetime ref="dateTime" :type="type" :startYear="startYear" :endYear="endYear"
						:cancelColor="cancelColor" :color="color" :setDateTime="setDateTime" :unitTop="unitTop"
						:radius="radius" @confirm="changeBirthday">
					</tui-datetime>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti" @click="updateInfo(4)">
						<view class="title">简介</view>
						<view class="cont">{{ userInfo.description }}</view>
						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>
				</li>
				<li class="info-item">
					<view class="info-item-zhuti" @click="updateInfo(5)">
						<view class="title">地址</view>
						<view class="cont">{{ userInfo.address }}</view>
						<view class="icon"><tui-icon name="arrowright" size="25"></tui-icon></view>
					</view>


					<!--picker-view start-->
					<view class="tui-mask-screen" :class="[showPickerStatus?'tui-mask-show':'']" @tap="hidePicker">
					</view>
					<view class="tui-picker-box" :class="[showPickerStatus?'tui-pickerbox-show':'']">
						<view class="picker-header tui-list-item">
							<view class="btn-cancle" hover-class="tui-opcity" :hover-stay-time="150"
								@tap.stop="hidePicker">取消</view>
							<view class="btn-sure" hover-class="tui-opcity" :hover-stay-time="150" @tap.stop="picker">确定
							</view>
						</view>
						<picker-view indicator-style="height: 50px;" class="picker-view" :value="value"
							@change="columnPicker">
							<picker-view-column>
								<view v-for="(item,index) in proviceArr" :key="index" class="item">{{item}}</view>
							</picker-view-column>
							<picker-view-column>
								<view v-for="(item,index) in cityArr" :key="index" class="item">{{item}}</view>
							</picker-view-column>
							<picker-view-column>
								<view v-for="(item,index) in districtArr" :key="index" class="item">{{item}}</view>
							</picker-view-column>
						</picker-view>
					</view>
					<!--picker-view end-->
				</li>
			</ul>
		</view>
		<view class="fotter"></view>
	</view>
</template>

<script>
	import {
		getUserInfo,
		updateUser
	} from "@/api/user.js"
	import cityData from '@/utils/picker.city.js'
	export default {
		components: {

		},
		data() {
			return {
				userInfo: {},
				uid: '',
				sexShow: false,
				radioSex: [{
						text: '男',
						type: 'red',
					},
					{
						text: '女',
						type: 'red',
					}
				],

				type: 2,
				startYear: 1980,
				endYear: 2030,
				cancelColor: '#888',
				color: '#5677fc',
				setDateTime: '',
				result: '',
				unitTop: false,
				radius: false,


				proviceArr: [],
				cityArr: [],
				districtArr: [],
				value: [0, 0, 0],
				iconHidden: true,
				showPickerStatus: false,
				text: ["请选择", "请选择", "请选择"],
				searchKey: ""
			}

		},

		onLoad(options) {
			this.getUser(options.uid)

			this.proviceArr = this.toArr(cityData);
			this.cityArr = this.toArr(cityData[0].children);
			this.districtArr = this.toArr(cityData[0].children[0].children)

		},
		onShow(options) {

		},
		created() {

		},
		methods: {

			toArr(object) {
				let arr = [];
				for (let i in object) {
					arr.push(object[i].text);
				}
				return arr;
			},
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			getUser(uid) {
				let params = {
					uid: uid
				}
				getUserInfo(params).then(res => {
					this.userInfo = res.data

				})

			},
			updateImg(type) {
				uni.navigateTo({
					url: "/pages/user/info/updateImg/updateImg?type=" + type
				})
			},
			updateInfo(index) {
				if (index == 1 || index == 4) {
					//修改昵称和简介
					uni.navigateTo({
						url: "/pages/user/info/updateInfo/updateInfo?type=" + index
					})
				} else if (index == 2) {
					//更改性别
					this.sexShow = true
				} else if (index == 3) {
					//修改生日
					this.$refs.dateTime.show();
				} else {
					this.showPicker()
				}
			},

			updateSex(e) {
				let index = e.index;

				if (e.index == 0) {
					this.userInfo.gender = 1;
				} else {
					this.userInfo.gender = 0;
				}
				updateUser(this.userInfo).then(res => {
					this.hideSex();
				})

			},
			hideSex() {
				this.sexShow = false;
			},
			//修改生日
			changeBirthday(e) {

				this.userInfo.birthday = e.result;
				updateUser(this.userInfo).then()
			},


			//picker change切换事件
			columnPicker: function(e) {
				let value = e.detail.value;
				//如果两者下标不一致，表示滚动过
				if (this.value[0] !== value[0]) {
					this.proviceArr = this.proviceArr;
					this.cityArr = this.toArr(cityData[value[0]].children);
					this.districtArr = this.toArr(cityData[value[0]].children[0].children);
					this.value = [value[0], 0, 0]
				} else if (this.value[1] !== value[1]) {
					this.proviceArr = this.proviceArr;
					this.cityArr = this.cityArr;
					this.districtArr = this.toArr(cityData[value[0]].children[value[1]].children);
					this.value = [value[0], value[1], 0]
				} else {
					this.value = value
				}

			},
			//确定按钮
			picker: function(e) {
				let value = this.value;
				if (cityData.length > 0) {
					let provice = cityData[value[0]].text;
					let city = cityData[value[0]].children[value[1]].text;
					let district = cityData[value[0]].children[value[1]].children[value[2]].text;
					this.text = [provice, city, district];

					let address = ''
					address += provice + ' ' + city + ' ' + district

					this.userInfo.address = address
					updateUser(this.userInfo).then(res => {
						this.showPickerStatus = false
					})
				}

			},
			// 显示picker-view
			showPicker: function() {
				this.showPickerStatus = true
			},
			// 隐藏picker-view
			hidePicker: function() {
				this.showPickerStatus = false
			},

		}
	}
</script>

<style scoped>
	@import url(./info.css);
</style>