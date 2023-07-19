<template>
	<view class="waterfall-item-container">
		<view class="waterfall-item" @tap="onTap">
			<image :src="params.url" mode="widthFix" @load="emitHeight" @error="emitHeight"></image>
			<view class="content">
				<view>{{params.title}}</view>
				<view class="money">{{params.money}}元</view>
				<view style="margin: 0 0 8rpx 0;">
					<text class="label">{{params.label}}</text>
				</view>
				<view class="shop-name">{{params.shop}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"helangWaterfallItem",
		options:{
			virtualHost: true
		},
		props:{
			params:{
				type: Object,
				default(){
					return {}
				}
			},
			tag:{
				type:String | Number,
				default:''
			},
			index:{
				type:Number,
				default:-1
			}
		},
		data() {
			return {
				
			};
		},
		methods:{
			// 发出组件高度信息，在此处可以区分正确和错误的加载，给予错误的提示图片
			emitHeight(e){
				const query = uni.createSelectorQuery().in(this);
				query.select('.waterfall-item-container').boundingClientRect(data => {
					let height = Math.floor(data.height);
					this.$emit("height",height,this.$props.tag);
				}).exec();
			},
			onTap(){
				this.$emit("click",this.$props.index,this.$props.tag);
			}
		}
	}
</script>

<style lang="scss" scoped>
.waterfall-item{
	padding: 16rpx;
	background-color: #fff;
	border-radius: 4px;
	font-size: 28rpx;
	color: #666;
	
	image{
		display: block;
		width: 100%;
		// 默认设置一个图片的大约值
		height: 350rpx;
	}
	
	.content{
		margin-top: 16rpx;
		
		.money{
			color: #fa3534;
			margin-top: 8rpx;
		}
		
		.label{
			background-color: #fa3534;
			color: #fff;
			font-size: 20rpx;
			padding: 4rpx 16rpx;
			border-radius: 20rpx;
		}
		
		.shop-name{
			font-size: 20rpx;
			color: #999;
		}
	}
}
</style>
