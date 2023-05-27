<template>
	<view class="content">
		
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<a href="#" @click="back"><tui-icon name="arrowleft" size="25"></tui-icon></a>
					<h3>关注分组</h3>
				</view>
				<a href="#"><tui-icon name="plus" size="22"></tui-icon></a>
			</view>
		</tui-navigation-bar>
		
		<view class="main">
			<view class="left"  >
				<tui-list-view  color="#777">
					<tui-list-cell >
						<view class="item">
							<p>推荐</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>搞笑</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>学习</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>萌宠</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>猫咪</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>游戏</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>动物</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>壁纸</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>风景</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>地址</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>豪车</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>旅游</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
					<tui-list-cell >
						
						<view class="item">
							<p>游戏</p>
							<a @click="clear"><tui-icon name="reduce" size="18"></tui-icon></a>
						</view>
					</tui-list-cell>
				</tui-list-view>
			</view>
			<view class="right">
				<tui-list-view>
							<tui-list-cell :lineLeft="false" @click="detail">
								<view class="tui-item-box">
									<tui-swipe-action :actions="actions" >
										<template v-slot:content>
											<view class="tui-list-item">
												
													<image src="/static/images/news/avatar_1.jpg" class="tui-msg-pic" mode="widthFix"></image>
													<view class="tui-msg-item">
														<view class="tui-msg-name">小赵</view>
														<view class="tui-msg-type">
															<p>用户id：232112</p>
															<p>粉丝：23</p>
														</view>
													</view>
												
											</view>
										</template>
									</tui-swipe-action>
								</view>
							</tui-list-cell>
							<tui-list-cell :lineLeft="false" @click="detail">
								<view class="tui-item-box">
									<tui-swipe-action :actions="actions" >
										<template v-slot:content>
											<view class="tui-list-item">
												
													<image src="/static/images/news/avatar_1.jpg" class="tui-msg-pic" mode="widthFix"></image>
													<view class="tui-msg-item">
														<view class="tui-msg-name">小赵</view>
														<view class="tui-msg-type">
															<p>用户id：232112</p>
															<p>粉丝：23</p>
														</view>
													</view>
												
											</view>
										</template>
									</tui-swipe-action>
								</view>
							</tui-list-cell>
							<tui-list-cell :lineLeft="false" @click="detail">
								<view class="tui-item-box">
									<tui-swipe-action :actions="actions" >
										<template v-slot:content>
											<view class="tui-list-item">
												
													<image src="/static/images/news/avatar_1.jpg" class="tui-msg-pic" mode="widthFix"></image>
													<view class="tui-msg-item">
														<view class="tui-msg-name">小赵</view>
														<view class="tui-msg-type">
															<p>用户id：232112</p>
															<p>粉丝：23</p>
														</view>
													</view>
												
											</view>
										</template>
									</tui-swipe-action>
								</view>
							</tui-list-cell>
							<tui-list-cell :lineLeft="false" @click="detail">
								<view class="tui-item-box">
									<tui-swipe-action :actions="actions" >
										<template v-slot:content>
											<view class="tui-list-item">
												
													<image src="/static/images/news/avatar_1.jpg" class="tui-msg-pic" mode="widthFix"></image>
													<view class="tui-msg-item">
														<view class="tui-msg-name">小赵</view>
														<view class="tui-msg-type">
															<p>用户id：232112</p>
															<p>粉丝：23</p>
														</view>
													</view>
												
											</view>
										</template>
									</tui-swipe-action>
								</view>
							</tui-list-cell>
							<tui-list-cell :lineLeft="false" @click="detail">
								<view class="tui-item-box">
									<tui-swipe-action :actions="actions" >
										<template v-slot:content>
											<view class="tui-list-item">
												
													<image src="/static/images/news/avatar_1.jpg" class="tui-msg-pic" mode="widthFix"></image>
													<view class="tui-msg-item">
														<view class="tui-msg-name">小赵</view>
														<view class="tui-msg-type">
															<p>用户id：232112</p>
															<p>粉丝：23</p>
														</view>
													</view>
												
											</view>
										</template>
									</tui-swipe-action>
								</view>
							</tui-list-cell>
						</tui-list-view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components:{
			
		},
		data() {
			return {
				current:-1,
				actions: [{
						name: '取消关注',
						color: '#fff',
						fontsize: 30, //单位rpx
						width: 80, //单位px
						background: '#FD3B31'
				}],
				dataList: [{
					url:"/static/images/product/1.jpg",
					name: "小赵",
					nums: 12
				}, {
					url:"/static/images/product/2.jpg",
					name: "kk",
					nums: 15
				}, {
					url:"/static/images/product/3.jpg",
					name: "ee",
					nums: 123
				}, {
					url:"/static/images/product/5.jpg",
					name: "mm",
					nums: 21
				}, {
					url:"/static/images/product/1.jpg",
					name: "ll",
					nums: 143
				}, {
					url:"/static/images/product/2.jpg",
					name: "ww",
					nums: 56
				}, {
					url:"/static/images/product/3.jpg",
					name: "小赵",
					nums: 89
				}, {
					url:"/static/images/product/5.jpg",
					name: "小赵",
					nums: 12
				}],
				
			}
		},
		methods: {

			clear(){
				alert(11)
			}
		}
	}
</script>

<style scoped>
	a{
		  text-decoration:none;
		  color: black;
	}
	li{
		list-style: none;
	}
	.nav{
			  display: flex;
			  align-items: center;
			  justify-content: space-between;
			  padding-right: 24rpx;
			  height: 80rpx;
	}
	.n-left{
			 display: flex;
	}
	 .nav a{
		  margin-left: 10px;
		  display: inline-block;
		  font-size: 20px;
	}
	.content .main{
		display: flex;
	}
	.content .main .left{
		width: 30%;
		border-right: 1px solid #f1f1f1;
	}
	.right{
		width: 80%;
	}

    .tui-list-item{
		width: 600rpx;
		width: 100%;
		display: flex;
		align-items: center;
	}
	
	.item{
		display: flex;
		justify-content: space-between;
	}
	
	.tui-msg-box {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.tui-msg-pic {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		display: block;
		margin-right: 24rpx;
		flex-shrink: 0;
	}
	
	.tui-msg-img {
		width: 120rpx;
		height: 160rpx;
		border-radius:5px;
		display: block;
		margin-right: 24rpx;
		flex-shrink: 0;
	}
	
	.tui-msg-item {
		
		height: 100rpx;
		overflow: hidden;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}
	
	
	
	.tui-button{
		background: #fff;
		border: 1px solid #ff0000;
		color: #ff0000;
		width: 120rpx;
		height: 60rpx;
		
	}
	
	.tui-msg-type {
		max-width: 280rpx;
		min-height: 60rpx;
		overflow: hidden;
		display: flex;
		flex-direction: column;
		
		justify-content: space-between;
		font-size: 25rpx;
	}
	
	
	.tui-msg-name {
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
		font-size: 30rpx;
		line-height: 1;
		color: #262b3a;
	}
	
	.tui-right-dot {
		height: 76rpx !important;
		padding-bottom: 10rpx !important;
	
	}
	
	.tui-content{
		background-color: #FD3B31;
		color: #fff;
		height: 80rpx;
		text-align: center;
		line-height: 80rpx;
	}

</style>
