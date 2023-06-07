<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
			<tui-tabs :tabs="tabs" :currentTab="currentTab" itemWidth="50%" @change="change" sliderBgColor="#ff0000"
				selectedColor="#ff0000"></tui-tabs>
		</tui-navigation-bar>
		
		<view @touchstart="start" @touchend="end">
			
			<interest v-if="currentTab == 0"></interest>
					
					
			<dashboard v-if="currentTab == 1" :seed='seed'></dashboard>
					
			<hot v-if="currentTab == 2"></hot>
			
		</view>

		

	</view>
</template>

<script>
import Dashboard from '@/pages/index/dashboard/dashboard.vue'
import Interest from '@/pages/index/interest/interest.vue'
import Hot from '@/pages/index/hot/hot.vue'
import { getChatUserList} from '@/api/chat.js'
import { getUserRecord } from "@/api/user.js"
export default {
	components: {
		Dashboard,
		Interest,
		Hot
	},
	data() {
		return {
			currentTab: 1,
			tabs: [{
				name: "关注"
			}, {
				name: "首页"
			},
			{
				name: "热榜"
			}
			],
			pullDown: false,
			seed: 0,
			startData:{},
		}
	},
	onLoad(option) {
		if (option.currentTab) {

			this.currentTab = option.currentTab
		}
	},
	onShow() {
		this.seed = Math.random()
	},
	created() {
            if(uni.getStorageSync('userInfo')==''){
				   return
			}else{
				this.getRecordCount()
			}
	},
	methods: {
		
		onTabItemTap(e){
		   
		  if(e.index == 0){
			  //重新刷新数据
			  this.currentTab = 1
			  console.log('重新刷新数据')
			  this.seed = Math.random()
			   
		  }
		},
		
		change(e) {
			this.currentTab = e.index
		},
		
		start(e){
		                    
		    this.startData.clientX=e.changedTouches[0].clientX;
		                 
		    this.startData.clientY=e.changedTouches[0].clientY;
		},
		end(e){
		    // console.log(e)
		    const subX=e.changedTouches[0].clientX-this.startData.clientX;
		    const subY=e.changedTouches[0].clientY - this.startData.clientY;
		    if(subY>50 || subY<-50){
		        console.log('上下滑')
		    }else{
		        if(subX>100){
					this.currentTab = this.currentTab-1
		        }else if(subX<-100){
		           
					this.currentTab = this.currentTab+1
		        }else{
		        }
		    }
		},
		
		getChatUserList() {
			
			return new Promise((resolve)=>{
				
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getChatUserList(params).then(res => {			
					let count =0 
				     for(let i=0;i<res.data.length;i++){
					   if(res.data[i].count>0){
							count += res.data[i].count	
					   }
				     }
				   resolve(count)
				})
				
			})
			
			
		},
		getUserRecord() {
			return new Promise((resolve)=>{
				let params = {
					uid: uni.getStorageSync("userInfo").id
				}
				getUserRecord(params).then(res => {
					  resolve(res.data)
				})
			})
			
		},
		
		async getRecordCount(){
			let count =  await this.getChatUserList()
			let res = await this.getUserRecord()
			
			if(res.agreeCollectionCount>0||res.addFollowCount>0||res.noreplyCount>0||count>0){
				uni.showTabBarRedDot({ //显示红点
					index: 2 //tabbar下标
				})
			}else{
				uni.hideTabBarRedDot({
					index:2
				})
			}	
		}
				
		
	}
}
</script>

