# vue-stick
A  waterfall flow component based on vue.js

一款基于 vue.js 的瀑布流组件。

### **demo:** [https://bh-lay.github.io/vue-stick/demo/](https://bh-lay.github.io/vue-stick/demo/)
### **实例 :** [http://bh-lay.com/blog](http://bh-lay.com/blog)



## 如何引入

您可以下载 `dist` 目录下的产出文件至您的项目中，用任何您希望使用的方式引入。
产出文件包含 umd 模块、ES 模块两个版本，您可以根据自己的项目自行选择。

也可以通过 npm 安装。

```bash
npm install -s vue-stick
```

引入 `vue-stick` 之后，您可以选择安装为 Vue 全局模块，也可以局部使用。

```javascript
var Stick = require('vue-stick')
// 全局注册
Vue.use(Stick)

// 局部注册
new Vue({
	data: {},
	components: {
		Stick: Stick.component
	}
})
```

## 如何使用

**template**

```vue
<Stick
	:list="list"
	@onScrollEnd="loadMore"
	>
  <template slot-scope="scope">
		<div class="card">
      <img v-if="scope.data.cover" :src="scope.data.cover"/>
      <h2>{{scope.data.title}}</h2>
      <p>{{scope.data.intro}}</p>
    </div>
  </template>
</Stick>
```

**vue**

```javascript
{
  data: {
    list: [
      {
        "cover": "http://static.bh-lay.com/.../share.jpg",
        "intro": "2018 年对于我来说是非...",
        "title": "剧中人的2018年终总结"
      },
      {
        "cover": "http://static.bh-lay.com/.../cover.jpg",
        "intro": "再次见到合肥，在历经五年的沪漂之后...",
        "title": "再见 · 合肥"
      }
    ]
  },
	methods: {
    loadMore: function () {
      this.list.push({
        "cover": "http://static.bh-lay.com/...yout.png",
        "intro": "今天的分享主要围绕 可视化布局模块的一些...",
        "title": "added by scroll 可视化布局模块开发分享"
      })
    }
  }
}
```

## 参数

| 参数                | 类型   | 必填 |     默认值      | 解释                  |
| ------------------- | ------ | ---- | :-------------: | :-------------------- |
| list                | Array  | 必填 |       []        | 瀑布流数据列表        |
| columnWidth         | Number | -    |       280       | 卡片宽度              |
| columnSpacing       | Number | -    |       10        | 卡片间距              |
| loadTriggerDistance | Number | -    |      1000       | 滚动加载距离边界值    |
| animationClass      | String | -    | 'stick-fade-in' | 卡片插入时的动画class |


> 说明：
>
> 因瀑布流的特殊性，请尽可能保证卡片內布局的稳定性。为响应可能的高度变化，本组件会延迟到第一张图片加载完成后再渲染。
>
> 该项目由原生瀑布流组件 [stick](https://github.com/bh-lay/stick) 演绎而成。
