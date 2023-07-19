/** * 图片缓存组件 */

<template>
	<image :src="src" :style="[imgStyle]" :mode="mode"></image>
</template>

<script>
import cache from '../../utils/cache.js';
export default {
	name: 'cach-image',
	props: {
		url: {
			type: String,
			default: ''
		},
		mode: {
			type: String,
			default: 'aspectFill'
		},
		fileMd5: {
			type: String,
			default: ''
		},
		styles: {
			type: Object,
			default() {
				return {};
			}
		},
		width: {
			type: [String, Number],
			default: '100%'
		},
		height: {
			type: [String, Number],
			default: '100%'
		},
		radius: {
			type: Number,
			default: 0
		}
	},
	data() {
		return {
			imgStyle: { width: '60px' },
			src: '' // 图片地址
		};
	},
	watch: {
		// 监听头像md5值的变化
		fileMd5(val) {
			// 查找获取图片缓存
			this.fnGetImageCache();
		}
	},
	created() {
		let w = isNaN(this.width) ? this.width : uni.upx2px(this.width) + 'px';
		let h = isNaN(this.height) ? this.height : uni.upx2px(this.height) + 'px';
		this.imgStyle = {
			width: w,
			height: h,
			borderRadius: uni.upx2px(this.radius) + 'px',
			...this.styles
		};
		// 查找获取图片缓存
		this.fnGetImageCache();
	},
	methods: {
		// 查找获取图片缓存
		async fnGetImageCache() {
			// #ifdef APP-PLUS
			var result = await cache.getImageCache(this.url, this.fileMd5);
			if (result) {
				this.src = result;
			} else {
				this.src = this.url;
			}
			// #endif
			// #ifndef APP-PLUS
			this.src = this.url;
			// #endif
		}
	}
};
</script>
