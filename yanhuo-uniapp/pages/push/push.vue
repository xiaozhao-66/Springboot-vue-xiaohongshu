<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click='back'><tui-icon name="arrowleft" size="24"></tui-icon></view>
					<view>发布</view>
				</view>
				<view class="next">
					<view @click='next'>下一步</view>
				</view>

			</view>
		</tui-navigation-bar>
		<view class="main">

			<view class="img-list">

				<view class="img-item" v-for="(item, index) in imgList" :key="index">
					<image :src="item" @click="previewImgae(index)" mode="aspectFill"></image>
				</view>
				<view class="img-item">

					<view class="upload">
						<view class="img-icon" @click="chooseImg">
							<tui-icon name="plus"></tui-icon>
						</view>
					</view>

				</view>
			</view>

			<view class="content">

				<textarea placeholder="请输入内容" maxlength="300" class="content-input"
					v-model="imgInfo.content"></textarea>

			</view>
			<view class="tag">
				<view class="icon">
					<tui-icon name="bankcard"></tui-icon>
					<p>选择分区</p>
				</view>
				<view class="tag-item">
					{{ imgInfo.categoryStr }}
				</view>
				<view class="right">
					<view @click="show"><tui-icon name="arrowright"></tui-icon></view>
				</view>
			</view>

			<view class="tag">
				<view class="icon">
					<tui-icon name="label"></tui-icon>
					<p>添加标签</p>
				</view>
				<view class="tag-item">
					{{ tagcontent }}
				</view>
				<view class="right">

					<view @click="nextAddTag">
						<tui-icon name="arrowright"></tui-icon>
					</view>
				</view>
			</view>
		</view>
		<tui-toast ref="toast"></tui-toast>

		<!-- 底部弹出 -->
		<tui-bottom-popup :zIndex="1002" :maskZIndex="1001" :show="popupShow" @close="popup">
			<view class="categoryBox">

				<view class="category-left">
					<scroll-view scroll-y style="height: 500rpx;">
						<tui-list-view color="#777">
							<tui-list-cell v-for="(category, index) in categoryList" :key="index"
								@click="getCategoryTwo(category)">
								<view>{{ category.name }}</view>
							</tui-list-cell>
						</tui-list-view>
					</scroll-view>
				</view>

				<view class="category-right">
					<scroll-view scroll-y style="height: 500rpx;">
						<tui-list-view color="#777">
							<tui-list-cell v-for="(category, index) in categoryOne.children" :key="index"
								@click="getCategoryInfo(category)">
								<view class="title">{{ category.name }}</view>
								<view class="desc">{{ category.description }}</view>
							</tui-list-cell>
						</tui-list-view>
					</scroll-view>
				</view>
			</view>
		</tui-bottom-popup>

		<!-- 弹框 -->
		<tui-modal :show="modal" @click="handleClick" @cancel="hide" content="退出数据将不会保存" color="#333" :size="32"
			shape="circle"></tui-modal>

	</view>
</template>s

<script>
	import {
		getCategory
	} from "@/api/category.js"
	import {
		getOne
	} from '@/api/imgDetail.js'
	export default {
		data() {
			return {
				popupShow: false,
				imgList: [],
				imgInfo: {},
				imgsUrl: '',
				tagList: [],
				tagcontent: '',
				categoryList: [],
				categoryOne: {},
				categoryArr: [],
				modal: false,
				type: 'add',
				mid: '',
				version: 0,
			}
		},
		onLoad(option) {

			if (option.mid != null && option.type != null && option.version == 0) {
				this.mid = option.mid
				this.type = option.type
				this.getOne(this.mid)
			} else {
				this.mid = option.mid
				this.type = option.type
			}
		},
		onShow() {
			this.getTagContent()
		},
		created() {
			this.imgList = JSON.parse(uni.getStorageSync("imgList"))
		},
		onBackPress(options) {
			/**
			 * 由于 uni.navigateBack() 同样会触发 onBackPress 函数。
			 * 因此在 onBackPress 中直接调用 uni.navigateBack() 并始终返回 true 会引发死循环。
			 * 此时，需要根据onBackPress的回调对象中的from值来做处理，当来源是'navigateBack'时，返回 false 。
			 */
			// console.log("----------onBackPress---------", options)
			if (options.from === 'navigateBack') {
				return false;
			}
			this.back();
			return true;
		},

		methods: {
			back() {
				this.modal = true
			},
			handleClick(e) {
				let index = e.index;
				if (index == 1) {
					uni.removeStorageSync("imgList")
					uni.removeStorageSync("tags")
					uni.removeStorageSync("imgInfo")
					uni.switchTab({
						url: "/pages/index/index"
					})
				}
				this.hide();
			},
			hide() {
				this.modal = false
			},
			show() {
				this.popupShow = true
				getCategory().then(res => {
					this.categoryList = res.data
				})
			},

			chooseImg() {
				let that = this

				if (that.imgList.length >= 9) {
					uni.showToast({
						title: "图片上限"
					})
					return
				}

				let maxCount = 9 - that.imgList.length
				uni.chooseImage({
					count: maxCount, // 最多可以选择的图片张数，默认9
					sizeType: ['original'], //original 原图，compressed 压缩图，默认二者都有
					sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
					success: function(res) {
						that.imgList.push(...res.tempFilePaths)
						uni.setStorageSync("imgList", JSON.stringify(that.imgList))
					}
				})

			},

			previewImgae(index) {
				let that = this
				uni.previewImage({
					current: index, // 当前显示图片的索引值
					urls: that.imgList, // 需要预览的图片列表，photoList要求必须是数组
					longPressActions: {
						itemList: ['删除'],
						success: function(data) {
							that.imgList.splice(data.index, 1)
							uni.setStorageSync("imgList", JSON.stringify(that.imgList))
							uni.closePreviewImage()
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				})
			},

			getCategoryTwo(category) {
				this.categoryOne = category
				this.categoryArr[0] = category.name
			},
			getCategoryInfo(category) {

				this.categoryArr[1] = category.name
				this.imgInfo.categoryId = category.id
				this.imgInfo.categoryPid = category.pid
				this.popupShow = false
				this.imgInfo.categoryStr = this.categoryArr[0] + '-' + this.categoryArr[1]
			},
			popup() {
				this.popupShow = false
			},
			getTagContent() {
				let that = this
				that.tagList = JSON.parse(uni.getStorageSync('tags'))
				that.imgInfo = JSON.parse(uni.getStorageSync("imgInfo"))
				let str = ''

				that.tagList.forEach(item => {
					str += '#' + item.name
				})

				that.tagcontent = str
			},
			nextAddTag() {

				//这两行非常重要
				this.imgInfo.tags = this.tagList
				uni.setStorageSync('imgInfo', JSON.stringify(this.imgInfo))

				if (this.type == 'update') {

					uni.navigateTo({
						url: "/pages/addtag/addtag?mid=" + this.mid + '&type=update'
					})

				} else {
					uni.navigateTo({
						url: "/pages/addtag/addtag?mid=" + this.mid + '&type=add'
					})
				}


			},
			next() {

				let that = this

				let userInfo = uni.getStorageSync("userInfo")

				if (userInfo.username == null) {
					let params = {
						title: "请先登录",
					}
					that.$refs.toast.show(params);
				} else {


					if (typeof that.imgInfo.content == 'undefined' || that.imgInfo.content == '') {
						let params = {
							title: "请输入内容",
						}
						that.$refs.toast.show(params);
					} else if (typeof that.imgInfo.categoryStr == 'undefined' || that.imgInfo.categoryStr == '') {
						let params = {
							title: "请选择一个分区",
						}
						that.$refs.toast.show(params);
					} else {
						uni.getStorage({
							key: 'tags',
							success: function(res) {
								that.tagList = JSON.parse(res.data)
							}
						})

						that.imgInfo.userId = userInfo.id
						that.imgInfo.tags = that.tagList

						uni.setStorageSync('imgInfo', JSON.stringify(that.imgInfo))

						uni.navigateTo({
							url: '/pages/addalbum/addalbum?type=' + that.type
						})
					}
				}
			},

			// ---------------编辑操作
			getOne(mid) {
				let params = {
					id: mid
				}
				getOne(params).then(res => {
					
					this.imgInfo = res.data
					this.imgList = res.data.imgsUrl
					this.tagList = res.data.tagList
					this.imgInfo.categoryStr = res.data.categoryPName + '-' + res.data.categoryName;

					uni.setStorageSync('tags', JSON.stringify(this.tagList))
					uni.setStorageSync('imgList', JSON.stringify(this.imgList))
					let str = ''

					this.tagList.forEach(item => {
						str += '#' + item.name
					})

					this.tagcontent = str

				})
			},
		}
	}
</script>

<style scoped>
	@import url(./push.css);
</style>