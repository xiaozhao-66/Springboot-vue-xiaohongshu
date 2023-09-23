<template>
	<view class="container">
		<view class="nav">
			<view class="tui-mtop">
				<tui-tabs :tabs="tabs2" :height="88" :currentTab="currentTab" :sliderWidth="150" :sliderHeight="60"
					bottom="50%" color="#888" selectedColor="#888" :bold="true" sliderBgColor="#f4f4f4" @change="change"
					itemWidth="50%">
				</tui-tabs>
			</view>
		</view>
		<view class="main">
			<collection-album v-if="currentTab == 0" :uid='uid'></collection-album>
			<collection-img v-if="currentTab == 1" :uid='uid' :seed="seed"></collection-img>
		</view>
	</view>
</template>

<script>
	import CollectionAlbum from "@/pages/user/collections/collectionAlbums/collectionAlbums.vue"
	import CollectionImg from "@/pages/user/collections/collectionImgs/collectionImgs.vue"
	export default {
		components: {
			CollectionAlbum,
			CollectionImg
		},
		props: {
			uid: String,
			seed:Number,
		},
		data() {
			return {
				currentTab: 0,
				tabs2: [{
					name: "专辑"
				}, {
					name: "图片"
				}, {
					name: "其他"
				}],
			}
		},
		
		methods: {
			change(e) {
				this.currentTab = e.index
			}
		}
	}
</script>

<style scoped>
	@import url(./collections.css);
</style>