<template>
	<view class="container">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<h3>编辑标签</h3>
				<view @click='back'>
					<p>完成</p>
				</view>
			</view>
		</tui-navigation-bar>
		<view class="add-tag">
			<view v-for="(item, index) in tagList" :key="index">
				<view class="badge-item">
					<tui-tag type="white" shape="circle" margin="0 2px">{{ item.name }}</tui-tag>
					<view class="shut" @click="deltag(index)"><tui-icon name="shut" size="14"></tui-icon></view>
				</view>
			</view>
		</view>
		<view>
			<input placeholder="输入标签" class="tag-input" @confirm="submit" v-model='content' />
		</view>

		<view class="recommend-tag">
			<view class="ermen"> 热门推荐</view>
			<view class="tags">
				<view v-for="(item, index) in dataList" :key="index">
					<view @click="selectTag(item)">
						<tui-tag type="gray" shape="circle" margin="5px 5px">{{ item.name }}</tui-tag>
					</view>
				</view>
			</view>
		</view>
		<!-- 消息提示 -->
		<tui-toast ref="toast"></tui-toast>
	</view>
</template>

<script>
	import {
		getAllTag
	} from "@/api/tag.js"
	export default {
		data() {
			return {
				content: '',
				tagList: [],
				dataList: [],
				mid: '',
				type: '',
			}
		},
		onLoad(option) {
			this.mid = option.mid
			this.type = option.type
		},
		created() {
			this.getAllTag()

			let tags = JSON.parse(uni.getStorageSync("tags"))
			if (tags != null) {
				this.tagList = JSON.parse(uni.getStorageSync("tags"))
			}
		},
		methods: {
			submit() {
				if (this.tagList.length >= 5) {
					let params = {
						title: "最多支持5个标签",
					}
					this.$refs.toast.show(params);
				} else {
					let tag = {}

					for (var i in this.tagList) {
						if (this.tagList[i].name == this.content) {
							let params = {
								title: "标签已经存在",
							}
							this.$refs.toast.show(params);
							return
						}
					}

					tag.name = this.content
					this.tagList.push(tag)
					this.content = ''
				}
			},
			getAllTag() {
				getAllTag().then(res => {
					this.dataList = res.data
				})
			},
			deltag(index) {
				this.tagList.splice(index, 1)
			},
			selectTag(item) {
				if (this.tagList.length >= 5) {
					let params = {
						title: "最多支持5个标签",
					}
					this.$refs.toast.show(params);
				} else {
					for (var i in this.tagList) {
						if (this.tagList[i].name == item.name) {
							let params = {
								title: "标签已经存在",
							}
							this.$refs.toast.show(params);
							return
						}
					}

					this.tagList.push(item)
				}

			},
			back() {
				uni.setStorageSync("tags", JSON.stringify(this.tagList))
				uni.navigateTo({
					url: "/pages/push/push?mid=" + this.mid + '&type=' + this.type + '&version=' + 2
				})
			}
		}
	}
</script>

<style scoped>
	@import url(./addtag.css);
</style>