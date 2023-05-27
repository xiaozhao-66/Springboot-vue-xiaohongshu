<template>
	<view class="content">
		<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
			<tui-tabs :tabs="tabs" :currentTab="currentTab" itemWidth="50%" @change="change" sliderBgColor="#ff0000"
				selectedColor="#ff0000"></tui-tabs>
		</tui-navigation-bar>
		<interest v-if="currentTab == 0"></interest>
		<dashboard v-if="currentTab == 1" :seed='seed'></dashboard>
		<hot v-if="currentTab == 2"></hot>

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
		change(e) {
			this.currentTab = e.index
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

