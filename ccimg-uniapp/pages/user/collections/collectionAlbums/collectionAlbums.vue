<template>
	<view class="container">
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<tui-grid :unlined="true">
				<block v-for="(item, index) in dataList" :key="index">
					<tui-grid-item :cell="2" backgroundColor="#f4f4f4" :border="false" :bottomLine="false">
						<view class="card">
							<view @click="searchAlbum(item.id)">
								<image :src="item.cover" mode="aspectFill" :lazy-load='true'/>
							</view>
							<view class="cont">
								<h5>{{ item.content }}</h5>
								<p>{{ item.imgCount }}张图片 | {{ item.collectionCount }}人收藏</p>
							</view>
						</view>
					</tui-grid-item>
				</block>
			</tui-grid>
		<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
      </scroll-view>
	</view>
</template>

<script>
import {getAllCollection} from "@/api/collection.js"
export default {
	
	data() {
		return {
			page:1,
			limit:4,
			total:0,
			isEnd:false,
			dataList: [],
		}
	},
    props: {
		uid: String,
	},
	watch: {
		
	},
	created() {
		
		this.getAllCollection()
	},
	methods: {
		
		getAllCollection() {
			let params = {
				uid: this.uid,
				type: 1
			}
			getAllCollection(this.page,this.limit,params).then(res => {
                console.log("所有专辑",res.data)
				this.dataList = res.data.records
				this.total  = res.data.total
			})

		},

		searchAlbum(albumId) {
			uni.navigateTo({
				url: "/pages/user/albums/albumInfo?albumId=" + albumId
			})
		},
		
		loadData(){
			console.log("刷新数据")
			if (this.dataList.length >= this.total) {
				this.isEnd = true
				return
			}
			
			this.page = this.page + 1;
			let params = {
				uid: this.uid,
				type: 1
			}
			getAllCollection(this.page,this.limit,params).then(res => {
				this.dataList.push(...res.data.records)
			})
		}
		
	}
}
</script>

<style scoped>
.container {
	height: 60vh;
    background-color: #f4f4f4;
	margin-top: 40rpx;
}

.page {
	height: 60vh;
}

image {
	width: 340rpx;
	height: 400rpx;
}

.content .create {
	padding-top: 10px;
	padding-bottom: 5px;
}

.content .create button {
	width: 180rpx;
	background: #fff;
	font-size: 12px;
	color: #6f6f6f;
}

.main .tui-grid {
	padding: 10rpx 20rpx;

}

.main .tui-grid .card {
	background-color: #fff;
	width: 340rpx;
	height: 550rpx;
}

.main .card .cont {
	margin-left: 10px;
}

.main .card .cont p {
	margin-top: 5px;
	font-size: 12px;
}

.loadStyle {
	margin-top: 20rpx;
	width: 100%;
	height: 60rpx;
	text-align: center;
	color: #bfbfbf;
}
</style>
