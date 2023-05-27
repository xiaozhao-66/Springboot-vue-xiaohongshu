<template>
	<view class="container">
		
		<tui-navigation-bar backgroundColor="#fff" :isFixed="false" :isOpacity="false">
			<view class="nav">
				<view class="n-left">
					<view @click="back"><tui-icon name="arrowleft" size="25"></tui-icon></view>
				</view>

				<view class="tui-search-box" >
					<tui-icon name="search-2" :size="18" color="#bfbfbf"></tui-icon>
					<input class="tui-search-text" placeholder="请输入搜索内容" v-model="keyword" 
					 :focus="cursor"    @input="change" 
					/>
				</view>
				
				<view class="tui-notice-box">
					<tui-button type="danger" height="54rpx" width="100rpx" :size="24" @click="search">搜索</tui-button>
				</view>
				
			</view>
		</tui-navigation-bar>
		
		<view class="search-list" v-if = 'cursor'>
			<view class="tui-item-box" v-if="user!=null">
				<view class="tui-msg-box" @click="getUserInfo(user.id)">
					<image :src="user.avatar" class="tui-msg-pic" mode="aspectFill"></image>
					<view class="tui-msg-item">
						<view class="tui-msg-name">{{user.username}}</view>
						<view class="tui-msg-type">
							<view>用户id：{{ user.userId }}</view>
							<view>粉丝：{{ user.fanCount }}</view>
						</view>
					</view>
				</view>
			</view>
			<view  class="search-item" v-for="(item,index) in searchData" @click="selectTag(item.keyWord)" v-html="item.highLightKeyword"></view>
		</view>
		
		<view v-else>
			<view class="input-history w" v-show="dataList.length>0">
				<view class="top">
					<view> 搜索历史</view>
					
					<view class="all-del" v-if="D">
						<tui-button type="white" :size="20" height="40rpx" width="120rpx" @click="allDel" :link='true'>全部删除</tui-button>
						<tui-button type="white" :size="20" height="40rpx" width="60rpx" @click="cancelDel" :link='true'>取消</tui-button>
					</view>
					
					<tui-icon name="delete"  :size="16" @click="showDel" v-else></tui-icon>
				</view>
				<view class="his-main">
					<view class="tag-item"  v-for="(item,index) in dataList" :key= "index" v-show="index<searchLimit">
						<tui-tag type="gray" shape="circle"  margin='10rpx' @click="selectTag(item)"><p>{{item}}</p></tui-tag>
						<view class="del-tag" v-if="D">
							<tui-icon name="shut" :size="12" @click="deleteTag(item,index)"></tui-icon>
						</view>
					</view>
					<tui-tag type="gray" shape="circle"  margin='10rpx' v-if="!T&&dataList.length>8" ><tui-icon name="arrowdown" @click="arrowdown" ></tui-icon></tui-tag>
					<tui-tag type="gray" shape="circle"  margin='10rpx' v-if="T"><tui-icon name="arrowup" @click="arrowup" ></tui-icon></tui-tag>
				</view>
			</view>
			
			<view class="input-guess w">
				<view class="top">
					<view>猜你想搜</view>
					<tui-icon name="refresh"  :size="16" @click="refresh"></tui-icon>
				</view>
				<view class="gus-main">
					<view v-for="(item,index) in dataList2">
						<view @click="selectTag(item.content)" class="gus-item"><p>{{item.content}}</p></view>
					</view>
				</view>
			</view>
			
			<view class="hot-search w">
				
				<view class="top">热门搜索</view>
				<view class="hot-main">
					<view class="hot-item" v-for="(item, index) in hotSearchList" :key="index">
						<view class="hot-item-l" @click="selectTag(item.keyWord)">
							<tui-icon name="circle" :size="8" color="#ff0000" v-if="index==0"></tui-icon>
							<tui-icon name="circle" :size="8" color="#ff5500" v-if="index==1"></tui-icon>
							<tui-icon name="circle" :size="8" color="#ffaa00" v-if="index==2"></tui-icon>
							<tui-icon name="circle" :size="8"  v-if="index!=0&&index!=1&&index!=2"></tui-icon>
							<view class="hot-content" >{{item.keyWord}}</view>
							<view class="con-tag new-tag" v-if="item.type==2">新</view>
							<view class="con-tag hot-tag" v-if="item.type==1">热</view>
						</view>
						<view class="hot-item-r">
							{{item.count}}		
						</view>
					</view>
				
				</view>
			</view>
		</view>
	
		
	</view>
</template>

<script>
	
	import {getAllSearchRecord,addSearchRecord,deleteSearchRecord} from "@/api/searchRecord.js"
	import {getRecommend } from "@/api/imgDetail.js"
	import {addSearchRecordData,esSearchRecord,esAllSearchRecord} from "@/api/search.js"
	import {searchUserByUsername} from "@/api/user.js"
	export default {
		data() {
			return {
				keyword:'',
				D:false,
				
				dataList:[],
				dataList2:[],
				delList:[],
				page:1,
				limit: 6,
				
				searchLimit:8,
				T:false,
				//键盘
				cursor: false, //是否聚焦
				
				
				//聚焦内容
				searchData:[],
				
				//热门搜索
				hotSearchList:[],
				
				//搜索用户
				user:{}
				
			}
		},
		
		created() {
			
			this.getAllSearchRecord()
			this.getRecommend()
			this.esAllSearchRecord()
		},
		
		onLoad(options) {
			
		},
		
		methods: {
			back(){
				uni.reLaunch({
					url:"/pages/index/index"
				})
			},
			
			showDel(){
				this.D = true
			},
			
			allDel(){
				deleteSearchRecord(uni.getStorageSync("userInfo").id,this.dataList).then(res=>{
					this.dataList =[]
				})
			},
			
			cancelDel(){
				this.D = false
			},
			
			deleteTag(item,index){
			    
				this.dataList.splice(index, 1)
				this.delList.push(item)
				deleteSearchRecord(uni.getStorageSync("userInfo").id,this.delList).then()
			},
			
			arrowdown(){
				this.T = true
				this.searchLimit = 20
			},
			arrowup(){
				this.T = false
				this.searchLimit = 8
			},
			
		
			
			change(){
				if(this.keyword==''){
					this.cursor = false
					return 
				}
				
				this.cursor = true
				
				
				
				let params = {
					keyword : this.keyword
				}
				
				searchUserByUsername(params).then(res=>{
					
					this.user = res.data
				})
				
				esSearchRecord(params).then(res=>{
					
					this.searchData = res.data
				})
				
				
			},
			
			selectTag(item){
				this.keyword = item
				this.search()
			},
			getAllSearchRecord(){
				
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getAllSearchRecord(params).then(res=>{
					if(typeof(res.data)!='undefined'){
						this.dataList = res.data
					}
				})
			},
			
			search(){
				//添加一条搜索记录
				this.addSearchRecord()
				
				
				let params = {
					keyword:this.keyword
				}
				
				addSearchRecordData(params).then(res=>{
					uni.navigateTo({
						url:"/pages/index/search/searchList?keyword="+this.keyword
					})
				})
			
			},
			
			getRecommend() {
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getRecommend(this.page, this.limit, params).then(res => {
			        this.dataList2 = res.data.records
					
				})
			},
			
			refresh(){
				this.getRecommend()
			},
			
			addSearchRecord(){
				let SearchRecordDTO = {}
				SearchRecordDTO.uid = uni.getStorageSync("userInfo").id
				SearchRecordDTO.keyword = this.keyword
				addSearchRecord(SearchRecordDTO).then()
			},
			
			esAllSearchRecord(){
				
				
				let nowTime = new Date().valueOf();//转换成毫秒
				
				esSearchRecord().then(res=>{
					
					for(let i =0 ;i< res.data.length; i++){
						let obj = {}
						obj.count = res.data[i].count
						obj.highLightKeyword = res.data[i].highLightKeyword
						obj.id = res.data[i].id
						obj.keyWord = res.data[i].keyWord
						
						let jetLag = (nowTime-res.data[i].time)/1000/60
						
						//10分钟之内显示最新
						if(jetLag<10){
							//最新
							obj.type = 2
						}else if(jetLag>10&&res.data[i].count>=2){
							//最热
							obj.type = 1
						}
						
						this.hotSearchList.push(obj)
					}
					console.log(this.hotSearchList)
				})
				
			},
			
			getUserInfo(uid) {
				if (uid == uni.getStorageSync("userInfo").id) {
					uni.switchTab({
						url: "/pages/user/user"
					})
				} else {
					uni.navigateTo({
						url: "/pages/otherUser/otherUser?uid=" + uid
					})
				}
			},
			
		}
	}
</script>

<style scoped>

p{
	max-width: 250rpx;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	
}	

.w{
	width: 90%;
	margin: auto;
}


	
.nav {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-right: 24rpx;
	height: 80rpx;
}

.n-left {
	display: flex;
}

.tui-search-box {
	width: 100%;
	height: 32px;
	margin: 0 28rpx;
	border-radius: 18px;
	font-size: 14px;
	background-color: #f1f1f1;
	padding: 0 12px;
	box-sizing: border-box;
	color: #bfbfbf;
	display: flex;
	align-items: center;
}


.search-list{
	width: 90%;
	margin: auto;
}

.search-item{
	height: 80rpx;
	border-bottom: 1px solid #d2d2d2;
	line-height: 80rpx;
	font-size: 28rpx;
	color: #787878;
}


.top{
	display: flex;
	justify-content: space-between;
    font-size: 28rpx;
	color: #646464;
	padding-top: 20rpx;
	padding-bottom: 10rpx;
}


.his-main{
	display: flex;
	flex-wrap:wrap;
}

.all-del{
	display: flex;
}

.tag-item{
	position: relative;
}

.del-tag{
	position: absolute;
	right: 10rpx;
	top: -8rpx;
}

	
.gus-main{
	column-count: 2;
	column-gap: 0rpx;
	font-size: 28rpx;
}

.gus-item{
	margin-top: 20rpx;
}

.hot-main{
	width: 95%;
	margin: auto;
}

.hot-title{
	margin-top: 20rpx;
	margin-bottom: 20rpx;
}

.hot-item{
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 100rpx;
	border-bottom: 1px solid #d2d2d2;
	line-height: 100rpx;
}

.hot-item-l{
	display: flex;
	align-items: center;
}

.con-tag{
	margin-left: 5rpx;
	border-radius: 5px;
	background-color: #ffaa00;
	width: 40rpx;
	height: 40rpx;
	line-height: 40rpx;
	text-align: center;
	color: #fff;
	font-size: 24rpx;
}

.new-tag{
	background-color: #ffaa00;
}

.hot-tag{
	background-color: #ff5500;
}

.hot-item-r{
	font-size: 20rpx;
	color: #828282;
}
	
.hot-content{
	margin-left: 10rpx;
	color: #2e2e2e;
	font-size: 28rpx;
}

.hot-search{
	
}


/* 用户列表 */
.tui-item-box {
	width: 100%;
	display: flex;
	align-items: center;
	border-bottom: 1px solid #d2d2d2;
	font-size: 28rpx;
}

.tui-msg-box {
	display: flex;
	align-items: center;
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
	border-radius: 5px;
	display: block;
	margin-right: 24rpx;
	flex-shrink: 0;
}

.tui-msg-item {
	max-width: 500rpx;
	min-height: 100rpx;
	overflow: hidden;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.tui-msg-right {
	max-width: 120rpx;
	height: 88rpx;
	margin-left: auto;
	text-align: right;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	align-items: flex-end;
}
</style>
