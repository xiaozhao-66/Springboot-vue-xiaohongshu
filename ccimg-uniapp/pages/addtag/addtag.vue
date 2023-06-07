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
import {getAllTag } from "@/api/tag.js"
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
		if (uni.getStorageSync("tags") != null || uni.getStorageSync("tags") != '') {
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
			}else{
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

.nav {
	display: flex;
	justify-content: space-between;
	align-items: center;
	width: 95%;
	margin: auto;
	height: 80rpx;
}

.nav p {
	font-size: 32rpx;
}

.add-tag {
	display: flex;
	border-top: 1px solid #f4f4f4;
	border-bottom: 1px solid #f4f4f4;
	min-height: 80rpx;
	align-items: center;
	padding-left: 10px;
	background-color: #f4f4f4;
}

.ermen {
	font-size: 32rpx;
}

.recommend-tag {
	width: 90%;
	margin: auto;
	padding-top: 10px;

}

.tags {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
	margin-top: 5px;
}


.tag-input {

	min-height: 80rpx;
	line-height: 80rpx;
	padding-left: 10px;
	border-bottom: 1px solid #f4f4f4;
}

.badge-item {
	position: relative;
}

.shut {
	position: absolute;
	top: -15rpx;
	right: 0px;
}</style>
