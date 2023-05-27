*   <a href="#c1" title="概要">概要</a>
*   <a href="#c2" title="支持的平台">支持的平台</a>
*   <a href="#c3" title="使用方式">使用方式</a>
*   <a href="#c4" title="属性说明">属性说明</a>
*   <a href="#c6" title="事件说明">事件说明</a>
*   <a href="#c7" title="完整示例">完整示例</a>
*   <a href="#c8" title="示例效果">示例效果</a>
*   <a href="#c9" title="温馨提示">温馨提示</a>
*   <a href="#c10" title="关注我，不迷路">关注我，不迷路</a>

<div id="c1"></div>

#### 概要

pp-waterfalls-flow是一个瀑布流插件2.0，灵活配置、简单易用、兼容多端、同时兼容vue2和vue3、图片懒加载。

之前开发了custom-waterfalls-flow瀑布流，得到了很多小伙伴的喜欢，但是确实存在一些问题，暂时无法解决。所以现在就有这个瀑布流2.0版本，使用格子布局实现，使用更加完美。

**实现思路：** 使用grid格子布局，动态去改变grid-row属性，自动排序，其实是很简单的，比起1.0版本，插件代码简单很多。

<div id="c2"></div>

#### 支持的平台

H5、微信小程序（这两个平台经过测试优化，同事兼容vue2和vue3，其他小程序未测试）。

[H5体验地址](https://static-mp-a667b617-c5f1-4a2d-9a54-683a67cff588.next.bspapp.com/h5#/pages/waterfall-flow/waterfall-flow)、<a href="#c8" title="小程序效果查看">小程序效果查看</a>

==**特别注意：微信开发者工具只能显示几条数据，调试样式没问题，完整使用请真机预览没问题**。==

APP端：nvue-app不支持格子布局，无解。vue-app有兼容性问题，只能显示几项，下面的显示不出来，跟微信开发者工具表现一样，所以目前暂时无解，这是属于底层的兼容性问题。

其他小程序：暂未测试，需要的可以自己测试和修改，按理说问题不大，可以自行测试和修改插件。

<div id="c3"></div>

#### 使用方式

**1、导入插件**

该组件符合`uni_modules`规范，推荐直接在右侧下载处使用Hbuilderx导入插件，导入到项目根目录中的`uni_modules`文件夹中。

**2、template中使用**

uni\_modules 规范在项目页面中直接使用，不需要单独引入注册组件。

#### 基本用法

```javascript
<template>
	<pp-waterfall-flow :value="list"></pp-waterfall-flow>
</template>
<script>
	export default {
		data() {
			return {
				list: [{
					desc: "描述文字1",
					image: "https://via.placeholder.com/200x200.png/2878ff",
					title: "标题文字1"
				},{
					desc: "描述文字2",
					image: "https://via.placeholder.com/140x280.png/7FFFAA",
					title: "标题文字2"
				}]
			}
		}
	}
</script>
```
<div id="zdy"></div>

#### 自定义内容

由于动态插槽在很多平台支持度不是很好，而且可能会产生性能或警告等问题，所以2.0版本就不做成动态插槽，预设了title和desc两个字段及样式。如果开发者需要自己定义内容，就需要在插件里面去自定义，具体位置：`uni_modules/pp-waterfall-flow/components/pp-waterfall-flow/pp-waterfall-flow.vue`，**下方代码\<!--自定义内容 start-->和\<!--自定义内容 end-->之间就是自定义区域，建议开发者完成后备份，方便插件更新后替换**。

```javascript
<template>
	<view class="waterfall-flow" :style="{'--pad':padpx,'--gap':gappx,'--col':columns}">
		<view class="item" v-for="(item,index) in list" :key="index" :style="{'--mbt':mbtpx,'--br':brpx,'grid-row':`span ${item.rows?item.rows:colWidth}`}" @click="clickItem(index)">
			<lazy-load-image :image="item[imageKey]" imgMode="widthFix" :height="item.imageHeight" :index="index" :border-radius="imageBR" @finish="loadImage($event,item)" @click="clickImage"></lazy-load-image>
			<view class="char" :style="{'--h':charSizeFn}" v-if="showChar && item.rows && item.showChar">

			    <!--自定义内容 start-->
				<view class="title">{{item.title}}</view>
				<view class="desc">{{item.desc}}</view>
				<!--自定义内容 end-->

			</view>
		</view>
	</view>
</template>
```

<div id="c4"></div>

#### 属性说明

|    属性名   |       类型       |       默认值       |                     说明                    |
| :------: | :------------: | :-------------: | :---------------------------------------: |
|   value  | Array\[object] |        -        | 瀑布流数据，对象中可以包含image、showChar、title、desc等属性，<a href="#c5" title="详情见下方">详情见下方</a>|
| imageKey |     String     |      image      |                列表中的图片字段的键名                |
|  padding |     Number     |        0       |               瀑布流的左右边距，单位rpx              |
|  columns |     Number     |        2        |                  列数，建议2-5                 |
|    gap   |     Number     |        20       |                瀑布流列间距，单位rpx               |
|  itemBR  |     Number     |        20       |               瀑布流每项圆角，单位rpx               |
|  imageBR |     Number     | 20rpx 20rpx 0 0 |                   图片的圆角                   |
|    mbt   |     Number     |        20       |                  瀑布流每项下边距                 |
| showChar |     Boolean    |       true      |              瀑布流每项是否显示下方文字部分              |
| charSize |     Number     |       140       |          瀑布流每项是否显示下方文字部分的高度，单位rpx         |

<div id="c5"></div>

#### value属性说明

***注意：如果下拉刷新更新数据，需要先把value值清空，否则可能不会生效，可以参考下面的完整示例。***

|    属性名   |    类型   |  默认值 |          说明         |
| :------: | :-----: | :--: | :-----------------: |
|   image  |  String |   -  |         图片地址        |
| showChar | Boolean | true | 该项是否显示下方文字部分，可以单独配置 |
|   title  |  String |   -  |     标题（可以自定义，<a href="#zdy" title="见上">见上</a>）    |
|   desc   |         |   -  |     描述（可以自定义，<a href="#zdy" title="见上">见上</a>）    |

<div id="c6"></div>

#### 事件说明

|     事件名称    |      类型     |       说明      |
| :---------: | :---------: | :-----------: |
|  @clickItem | EventHandle | 单项点击触发，返回对应索引 |
| @clickImage | EventHandle | 图片点击触发，返回对应索引 |

<div id="c7"></div>

#### 完整示例

```javascript
<template>
	<view>
		<pp-waterfall-flow :value="list" :gap="20" :columns="2" :padding="30" :itemBR="12" imageBR="10rpx"></pp-waterfall-flow>
		<!-- <uni-load-more :status="status"></uni-load-more> -->
	</view>
</template>
<script>
	export default {
		data() {
			return {
				randoms: [
					{size:'200x500',color:'ff0000'},
					{size:'200x200',color:'2878ff'},
					{size:'200x100',color:'FFB6C1'},
					{size:'200x300',color:'9400D3'},
					{size:'100x240',color:'B0E0E6'},
					{size:'140x280',color:'7FFFAA'},
					{size:'40x60',color:'EEE8AA'},
					{size:'58x100',color:'FF7F50'},
					{size:'59x100',color:'C0C0C0'},
					{size:'60x100',color:'FAEBD7'}
				],
				list: [],
				status: 'more'
			}
		},
		onReachBottom() {
			if(this.status == 'loading') return;
			this.status = 'loading';
			for(let i = 0;i<8;i++){
				this.list.push({image:this.getImage(),title:'标题文字'+(this.list.length+1),desc:'描述文字'+(this.list.length+1)});
			}
			setTimeout(()=>{
				this.status = 'more';
			},1000)
		},
		onPullDownRefresh() {
			this.getData();
			uni.showToast({
				icon: 'success',
				title: '刷新成功'
			})
			uni.stopPullDownRefresh();
		},
		onLoad() {
			this.getData();
		},
		methods: {
			getData(){
				this.list = [];
				setTimeout(()=>{
					this.status = 'loading';
					for(let i = 0;i<10;i++){
						this.list.push({image:this.getImage(),title:'标题文字'+(this.list.length+1),desc:'描述文字'+(this.list.length+1)});
					}
					setTimeout(()=>{
						this.status = 'more';
					},1000)
				},10)
			},
			getImage(){
				const index = Math.floor(Math.random() * this.randoms.length);
				return `https://via.placeholder.com/${this.randoms[index].size}.png/${this.randoms[index].color}`;
			}
		}
	}
</script>
<style>
page {
	box-sizing: border-box;
	background-color: #f2f5f9;
}
</style>
```

<div id="c8"></div>

#### 示例效果

<img src="https://mp-a667b617-c5f1-4a2d-9a54-683a67cff588.cdn.bspapp.com/cloudstorage/2a71e51b-624a-44e4-85b6-7b08644172c3.gif" style="width:30%;" />


<div id="c9"></div>

### 温馨提示

1、该插件使用了Grid格子布局，目前我测过的微信小程序和H5是没问题的，由于兼容性问题，app暂时是有问题的。

2、对此插件或相关问题有好的建议，可以直接在评论区或者加群进行讨论。

3、希望遇到问题不要喷，也不要骂人，其实这种心情我能理解，写该插件也不是一时半会就完成了的，所以希望互相理解。只要有问题，我会尽量回复解决。

2、对此插件有任何问题建议加QQ群：**568984539**，评论区有时候来不及看，加群备注‘地区-名字-技术类型’。

#### 最后我想说：认为该插件对你有帮助的，记得收藏、好评，这样可以帮助到更多人哟！

***

<div id="c10"></div>

#### 关注我，不迷路

如果任何疑问的可以在评论区留言，还可以加QQ群交流：**568984539**，加群备注‘地区-名字-技术类型’。

更多前端等相关知识可关注我个人博客：<https://blog.csdn.net/qq_42961150?spm=1011.2124.3001.5343>

