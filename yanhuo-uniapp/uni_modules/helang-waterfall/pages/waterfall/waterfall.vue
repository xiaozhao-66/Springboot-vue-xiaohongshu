<template>
	<view>
		<helang-waterfall-list
			:status="waterfall.status"
			:list="waterfall.list"
			:reset="waterfall.reset"
			@click="onClick"
			@done="onDone"
		>
			<template>
				<view v-if="waterfall.status == 'await'">
					<view class="load-txt">上拉加载更多</view>
				</view>
				<view v-else-if="waterfall.status == 'loading'">
					<view class="load-txt">加载中</view>
				</view>
				<view v-else-if="waterfall.status == 'success'">
					<view class="load-txt">加载中</view>
				</view>
				<view v-else-if="waterfall.status == 'finish'">
					<view class="load-txt">没有更多了</view>
				</view>
				<view v-else-if="waterfall.status == 'fail'">
					<image class="load-icon" src="../../static/waterfall/fail.png"></image>
					<view class="load-txt">出错了，请刷新重试</view>
				</view>
				<view v-else-if="waterfall.status == 'empty'">
					<image class="load-icon" src="../../static/waterfall/empty.png"></image>
					<view class="load-txt">暂无数据</view>
				</view>
				<view v-else><!-- 别问我为什么要写一个 v-else 的空 view,不写H5平台就会有CSS生效的离谱BUG --></view>
			</template>
		</helang-waterfall-list>
		
		<view class="status-change" @tap="onStatusChange">
			<view>切换<br />状态</view>
		</view>
	</view>
</template>

<script>
	import helangWaterfallList from "@/uni_modules/helang-waterfall/components/waterfall/waterfall-list"
	
	// 列表接口模拟数据
	import mockData from '../../mock-data/waterfall-list.js'
	
	export default {
		components: {
			"helang-waterfall-list": helangWaterfallList
		},
		data() {
			return {
				// 异步请求相关
				ajax: {
					// 是否可以加载
					load: true,
					// 每页的请求条件
					rows:10,
					// 页码
					page:1,
					// 数据列表
					dataList:[]
				},
				// 瀑布流组件相关
				waterfall:{
					status:"",
					reset:false,
					list:[]
				}
				
			}
		},
		onReady() {
			this.getList();
		},
		// 触底触发
		onReachBottom() {
			this.getList();
		},
		// 下拉刷新
		onPullDownRefresh(){
			// 正常情况下接口返回应该很会很快。故意延迟调用，让用户有在刷新的体验感
			setTimeout(()=>{
				this.ajax.page = 1;
				this.ajax.load = true;
				this.getList();
			},800);
		},
		methods: {
			// 瀑布流组件点击事件
			onClick(data,index, tag){
				console.log(data);
				let direction = {
					"left":'左',
					"right":'右'
				}
				uni.showToast({
					title:`${direction[tag]}侧列表第${index+1}个被点击`,
					icon:'none'
				})
			},
			// 瀑布流组件渲染完成
			onDone(){
				// 设置组件为 非重置，这行代码保留不删即可
				this.waterfall.reset = false;
				
				// 恢复 getList 方法的调用
				this.ajax.load = true;
				this.ajax.page++;
				
				// 设置组件状态为 等待加载
				this.waterfall.status = 'await';

				/**
				 * 如果你是一个追求完美的开发者，可以通过判断当前数据的长度和请求的数据长度来优化前端请求，减少不必要请求
				 * 不需要则删除
				 * */
				/**
				if(this.ajax.dataCount >= this.ajax.rows){
					this.ajax.load = true;
					this.ajax.page++;
				}
				*/
			},
			// 获取数据
			getList() {
				if (!this.ajax.load) {
					return;
				}
				this.ajax.load = false;
				
				// 设置状态为加载中
				this.waterfall.status = 'loading';
				
				// 请求数据， mockData.getList 示例一个 ajax 请求
				mockData.getList({
					pageNum:this.ajax.page,
					pageSize:this.ajax.rows
				}).then(res=>{
					// 获取到的数据，请注意数据结构
					console.log(res);
					
					// 第一页数据执行以下代码
					if(this.ajax.page == 1){
						// 关闭下拉
						uni.stopPullDownRefresh();
						
						// 设置组件状态为 重置，可供下拉刷新这类需要重置列表功能时使用
						this.waterfall.reset = true;
					}
					
					// 数据无效时处理
					if(!res || res.length < 1){
						// 设置组件为 加载结束 状态
						this.waterfall.status = 'finish';
						return;
					}
					
					// 将数据赋值给瀑布流 list 属性
					this.waterfall.list = res;
					// 设置组件为 加载成功 状态，此时瀑布流组件开始计算当前数据的布局
					this.waterfall.status = 'success';
					
					/**
					 * 下方的代码为扩展其他功能的示例代码 可做参考，不需要可删除
					 * */ 
					
					// 缓存当前数据给其他需要该数据的功能使用
					if(this.ajax.page == 1){
						this.ajax.dataList = res;
					}else{
						this.ajax.dataList = [...this.ajax.dataList,...res];
					}
					// 记录本次数据长度，意义请看 done 事件的回调
					this.ajax.dataCount = res.length || 0;
					
					// 。。。下面不需要写代码了，等待组件渲染完成
				})
			},
			// 导航状态切换演示监听
			onStatusChange(){
				uni.showActionSheet({
					itemList: ['常规', '加载异常', '加载错误'],
					success: (res)=> {
						switch(res.tapIndex){
							case 0:
								this.ajax.page = 1;
								this.ajax.load = true;
								this.getList();
								break;
							case 1:
								// alert(111)
								this.waterfall.status = 'fail';
								break;
							case 2:
								this.waterfall.status = 'empty';
								break;
							default:
						}
					}
				});
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #f3f3f3;
	}
	
	.load-txt{
		padding: 0 0 20rpx 0;
		text-align: center;
		color: #999;
		font-size: 24rpx;
	}
	
	.load-icon{
		width: 300rpx;
		height: 300rpx;
		margin: 0 auto 20rpx auto;
		display: block;
	}
	
	.status-change{
		position: fixed;
		right: 10rpx;
		top: 60%;
		width: 80rpx;
		height: 80rpx;
		z-index: 100;
		font-size: 24rpx;
		border-radius: 50%;
		background-color: #0089ff;
		color: #fff;
		line-height: 1;
		opacity: .33;
		
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: center;
		align-items: center;
		align-content: center;
	}
</style>