<template>
	<view class="evan-emoji">
		<swiper :style="{ height: height }" :indicator-dots="false" :autoplay="false" :current="swiper.activeCurrent" @change="fnSwitchEmojiList">
			<!-- <block v-for="(item, index) in dataList"  :key="item.alias"> -->
			<!-- 
					可优化显示卡顿
						代码：v-show="swiper.current == index"
						问题：切换的时候空白
				-->
			<swiper-item  v-for="(item, index) in dataList" :key="item.alias">
				<scroll-view class="emoji-wrap" :style="{ height: height }" scroll-y="true">
					<view class="emoji-list" :class="[item.type]">
						<view
							class="emoji-list-item"
							:class="[item.type, (eIndex + 1) % 3 == 0 ? 'no-right-border' : '']"
							v-for="(emoji, eIndex) in item.list"
							:key="eIndex"
							@click="fnSelectEmoji(emoji, item.type)"
						>
							<text v-if="item.type == 'text'" class="emoji-text">{{ emoji.text }}</text>
							<text v-else-if="useHtmlRender && item.type == 'html'" class="emoji-html">{{ emoji.html }}</text>
							<cache-image v-else class="emoji-img" :width="60" :height="60" :url="emoji.url" :fileMd5="emoji.url" mode="scaleToFill"></cache-image>
						</view>
					</view>
				</scroll-view>
			</swiper-item>
			<!-- </block> -->
		</swiper>
		<view class="control-bar" :class="{ 'no-del-icon': !useDelIcon }">
			<scroll-view class="control-emoji-scroll" scroll-x="true" :scroll-into-view="control.currentId">
				<view
					:id="'control' + index"
					class="control-emoji-scroll-item"
					:class="{ actived: swiper.current == index }"
					:style="[{ '--bg-color': activeBgColor, '--radius': activedRadius }]"
					v-for="(item, index) in dataList"
					@click="fnSwitchEmojiList(index)"
					:key="item.name"
				>
					{{ item.name }}
				</view>
			</scroll-view>
			<image v-if="useDelIcon" class="control-bar-del-icon" src="../../static/icon/del_icon.png" mode="scaleToFill" @click.stop="fnDeleteEmoji()"></image>
		</view>
	</view>
</template>
<script>
import DefaultEmoji from '../../utils/default.emoji.js';
import CacheImage from '../cache-image/cache-image.vue';
export default {
	components: {
		CacheImage
	},
	props: {
		// 表情滚动方向(未使用)
		mode: {
			type: String,
			default: 'x'
		},
		// 表情容器高度
		height: {
			type: String,
			default: '340rpx'
		},
		// 使用删除图标
		useDelIcon: {
			type: Boolean,
			default: true
		},
		// 使用html格式渲染（内置普通表情支持，或者自定义表情格式与内置表情数据一致）
		useHtmlRender: {
			type: Boolean,
			default: true
		},
		// 是否使用默认的表情
		useDefault: {
			type: Boolean,
			default: true
		},
		// 自定义表情
		customEmojiList: {
			type: Array,
			default() {
				return [];
			}
		},
		// 选中的背景色
		activeBgColor: {
			type: String,
			default: '#ededed'
		},
		// 选中圆角样式(单位rpx)
		activeRadius: {
			type: Number,
			default: 12
		}
	},
	data() {
		return {
			emojiTypeActivedStyle: {},
			control: {
				currentId: 'control0'
			},
			swiper: {
				activeCurrent: 0, // 用于优化过度
				current: 0
			},
			dataList: []
		};
	},
	computed: {
		activedRadius() {
			return uni.upx2px(this.activeRadius) + 'px';
		}
	},
	created() {
		let emojiList = [];
		let defaultFilterList = DefaultEmoji.filter(x => x.type == 'image');
		let customFilterList = this.customEmojiList.filter(x => x.type == 'image');
		let filterList = [...defaultFilterList, ...customFilterList];
		for (let i = 0; i < filterList.length; i++) {
			filterList[i].list.forEach(item => {
				emojiList.push(item);
			});
		}
		uni.$evanEmoji.util.convert.extend(emojiList);
		this.fnInit();
	},
	methods: {
		// 初始化表情库
		fnInit() {
			if (this.useDefault) {
				this.dataList = [...DefaultEmoji, ...this.customEmojiList];
			} else {
				this.dataList = this.customEmojiList;
			}
		},
		/**
		 * 切换表情列表
		 * */
		fnSwitchEmojiList(e) {
			if (e.hasOwnProperty('detail')) {
				this.swiper.current = e.detail.current;
			} else {
				this.swiper.current = e;
			}
			this.control.currentId = 'control' + this.swiper.current;
			setTimeout(() => {
				this.swiper.activeCurrent = this.swiper.current;
			}, 10);
		},
		/**
		 * 选中表情
		 * @param {object} item = 选中的表情数据
		 * @param {string} type = 表情类型
		 * */
		fnSelectEmoji(emoji, type) {
			this.$emit('on-select', {
				type: type,
				data: emoji
			});
		},
		fnDeleteEmoji() {
			this.$emit('on-delete');
		}
	}
};
</script>
<style scoped lang="scss">
@import './evan-emoji.scss';

.control-bar {
	display: flex;
	position: relative;
	background-color: #ffffff;
	box-sizing: border-box;
	padding-right: 80rpx;
	border-top: 2rpx solid #f8f8f8;
	&.no-del-icon {
		padding-right: 0;
		.control-emoji-scroll {
			padding: 6rpx 10rpx;
		}
	}
	&-del-icon {
		width: 50rpx;
		height: 44rpx;
		position: absolute;
		top: 50%;
		transform: translateY(-50%);
		right: 20rpx;
		overflow: initial;
		&::before {
			content: '';
			position: absolute;
			top: 50%;
			transform: translateY(-50%);
			left: -20rpx;
			width: 2rpx;
			height: 28rpx;
			background-color: #ccc;
		}
	}
}

.control-emoji-scroll {
	white-space: nowrap; // 滚动必须加的属性
	box-sizing: border-box;
	padding: 6rpx 20rpx;

	// border-top: 2rpx solid #e8e8e8;
	// border-bottom: 2rpx solid #e8e8e8;
	// background-color: rgba($color: #e8e8e8, $alpha: 0.3);
	&-item {
		display: inline-block;
		box-sizing: border-box;
		padding: 6rpx 14rpx;
		&.actived {
			background-color: var(--bg-color);
			border-radius: var(--radius);
		}
		& + & {
			// border-left: 2rpx solid #e8e8e8;
		}
	}
}
.emoji-wrap {
	width: 100%;
}
.emoji-list {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	box-sizing: border-box;
	padding: 10rpx;

	&-item {
		width: 14.2%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-sizing: border-box;
		padding: 10rpx 6rpx;

		&.text {
			width: 33.33%;
			border-right: 2rpx solid #eee;
			border-bottom: 2rpx solid #eee;
			&.no-right-border {
				border-right: 0;
			}
		}
	}
}
.emoji-text {
}
.emoji-html {
	font-size: 50rpx;
}
.emoji-img {
	width: 60rpx;
	height: 60rpx;
}
</style>
