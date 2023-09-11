### 关于插件

**1、本插件表情收集于网上，若侵犯了您的相关权益，请联系作者删除，联系方式`QQ:1431128779`**

**2、一个简单的Emoji表情插件，以及收集的一些简单的表情，有任何建议望大佬提出来**

**3、一个简单的Emoji表情插件，以及收集的一些简单的表情，有任何建议望大佬提出来**

**4、如果觉得默认的表情数据太多了，可以在 `/uni_modules/evan-emoji/utils/default.emoji.js` 文件中删除默认的表情数据**

**5、如果觉得这些表情不够，您还可以自己扩展表情，我在文档底部增加了一些表情来源，以及表情数据的获取方法**

*** 

<br/>

### 一、使用说明

<br/>

1. 下载插件：插件遵循uni_modules规范，可以直接从插件市场直接导入使用
2. 安装使用：
   
   2.1.  在main.js
   ```javascript
   // 安装emoji
   import EvanEmoji from '@/uni_modules/evan-emoji/utils/index.js'
   Vue.use(EvanEmoji)
   ```
   
   2.2.  在 template 
   ```html
   <evan-emoji 
       @on-select="fnOnEmojiSelect" 
       @on-delete="fnOnEmojiDelete">
   </evan-emoji>
   ```
   
   2.3.  表情转换
   ```javascript
   // 渲染
   fnTranslEmoji() {
     let _testText = '[拍手]哈哈哈哈哈，[心花怒放]'
       // 将表情字符转html内容
       // api：
       // 1. 使用 uni.$evanEmoji.util.convert.toHtml
       // 2. 使用 this.$evanEmoji.util.convert.toHtml
       let html = uni.$evanEmoji.util.convert.toHtml(_testText, false, {
           classes: ['img-class-1', 'img-class-2'], // 自定义class
           width: 40, // 图片宽（rpx）
           height: 40, // 图片高（rpx）
           styles: {'vertical-align': 'text-bottom'} // 其他样式(必须是原生的style写法，不支持驼峰式)
       });
   },
   ```
   
   2.4. 局部扩展表情（示例）
   > 通过`customEmojiList`参数传入需要扩展的表情，具体参数格式请查看下方说明
   ```html
   <evan-emoji 
       :customEmojiList="myEmojiList"
       @on-select="fnOnEmojiSelect" 
       @on-delete="fnOnEmojiDelete">
   </evan-emoji>
   ```
   ```javascript
   <script>
   export default {
     data(){
       return {
        myEmojiList: [{
         		type: 'image',
         		alias: 'kz1',
         		name: '扩展表情1',
         		list: [{
         			id: '',
         			name: '"施工标',
         			code: '"\u{1f6a7}',
         			htm: '🚧',
         			class: '',
         			text: '[施工标志]',
         			url: 'https://uc-emoji.azureedge.net/orig/dd/8b7f393a72b7705da89b5b87a1d340.png'
         		}]
         	},
         	{
         		type: 'image',
         		alias: 'kz2',
         		name: '扩展表情2',
         		list: [{
         			id: '',
         			name: '挥手',
         			code: null,
         			html: '👋',
         			class: '',
         			text: '[挥手]',
         			url: 'https://emoji.emojipic.cn/pic/72/apple/waving-hand-sign_1f44b.png'
         		}]
         	}
         ]
       }
     }
   }
   ```
   
   2.5. 全局扩展表情（待补充...）
   ```javascript
   
   
   ```

### 组件参数说明

|参数名称|说明|类型|默认值|
|:--:|:--:|:--:|:--:|
|height|容器高度|String|340rpx|
|activeBgColor|表情类型选择工具条激活背景颜色|String|#ededed|
|activeRadius|表情类型选择工具条激活圆角（单位rpx）|Number|12|
|useDefault|是否使用内置的表情|Boolean|true|
|customEmojiList|自定义表情列表（看下方详细说明）|Array|[ ]|
|useHtmlRender|使用html格式渲染，表情的`html`字段必须有值且支持直接html渲染（内置普通表情支持，或者自定义表情格式与内置表情数据一致）|Boolean|true|
|useDelIcon|是否使用删除按钮图标|Boolean|true|

> **！！！关于`customEmojiList` 参数的详细说明**：

```javascript
// 参数格式示例：
// 当type='text'  一般用于颜文字表情或者只支持html代码的表情（无url图片地址的情况）
// 当type='image' 一般用于带有图片的表情
[
  // 表情分类1
  {
    type: 'image',    // type 参数取值只能取 image | text
    alias: 'jtgj',    // 表情分类名称别名
    name: '交通工具', // 表情分类名称
    list: [           // 表情数据列表
      { 
            "id": "",             // 编号
            "name": "施工标志",   // 表情名称
            "code": "\u{1f6a7}",  // unicode 编码
            "html": "🚧",         // html代码（直接显示的表情，支持插入到输入框渲染）
            "class": "",          // 转换为img图片后的class
            "text": "[施工标志]", // 表情文本内容
            "url": "https://uc-emoji.azureedge.net/orig/dd/8b7f393a72b7705da89b5b87a1d340.png", // 表情地址
      },
      // 更多（格式同上）...
    ]
  },
  // 表情分类2
  {
    type: 'image',      // type 参数取值只能取 image | text
    alias: 'ssjt',    // 表情分类名称别名
    name: '手势箭头', // 表情分类名称
    list: [           // 表情数据列表
      { 
        "id": "",             // 编号
        "name": "挥手",       // 表情名称
        "code": null,         // unicode 编码
        "html": "👋",         // html代码（直接显示的表情，支持插入到输入框渲染）
        "class": "",          // 转换为img图片后的class
        "text": "[挥手]",     // 表情文本内容
        "url": "https://emoji.emojipic.cn/pic/72/apple/waving-hand-sign_1f44b.png", // 表情地址
      }
    ]
  },
]
```

### 内部事件

|参数名称|说明|
|:--:|:--|
|||

<br/>

### 回调事件

|参数名称|说明|
|:--:|:--|
|@on-select|表情选中回调，返回格式如下：<br/>{  <br/>        type:'', // 表情的大分类 image \| text|
|@on-delete|启用`useDelIcon`的时候生效，监听删除操作|
|||
|||

<br/>

### 内置工具

###### `uni.$evanEmoji` 等同 `this.$evanEmoji`

|工具名称|说明|参数|示例|
|--|--|--|--|
|uni.$evanEmoji.util.convert|用于将包含组件表情的转换工具集合|无||
|uni.$evanEmoji.util.convert.toHtml|将包含表情的文本中的表情转换为html代码|参数1：文本内容<br/>参数2：是否转unicode编码的表情，默认值`false`<br/>参数2：图片参数配置 {<br/>    classes:[], // 图片的class数组<br/>    width: 40,  // 图片宽（rpx）      <br/>    height: 40,  // 图片高（rpx）<br/>    styles:{},      // 其他样式<br/>}|let html = uni.$evanEmoji.util.convert.toHtml(_testText, false, {         <br/>        classes: ['img-class-1', 'img-class-2'], // 自定义class         <br/>        width: 40, // 图片宽（rpx）         <br/>        height: 40, // 图片高（rpx）         <br/>        styles: {'vertical-align': 'text-bottom'} // 其他样式(必须是原生的style写法，不支持驼峰式)<br/>});|
|||||
|||||

<br/>

### 二、表情来源（获取）

#### [1.Emoji大全：https://www.emojidaquan.com/common-smileys-people-emojis](https://www.emojidaquan.com/common-smileys-people-emojis)

#####  js获取数据代码（获取后的数据格式即组件内部的数据格式）

```javascript
function fnGetEmojiList() {
    let emojiList = []

    let domList = document.querySelectorAll('.character-list__item')

    for (let i = 0; i < domList.length; i++) {
        let _a = domList[i].children[0]; // 获取a标签
        let _img = _a.children[0].children[0]; // 获取图片标签
        let _div2 = _a.childNodes[3]; // 获取unicode码所在标签

        let emoji = {
            id: '',
            name: _a.getAttribute('title'),
            url: _img.getAttribute('data-src'),
            code: _div2.innerText,
            html: _a.getAttribute('data-symbol'),
            class: '',
            text: '[' + _a.getAttribute('title') + ']',
        }
        emojiList.push(emoji)
    }

    return emojiList;
}

let r = fnGetEmojiList()
console.table(r)
console.log(JSON.stringify(r))
```

<br/>

[2.Unicode字符百科emoji数据：https://unicode-table.com/cn/emoji/](https://unicode-table.com/cn/emoji/)

#####  js获取数据代码（获取后的数据格式即组件内部的数据格式）

```javascript

function fnGetEmojiList() {
    let emojiList = []
    // 获取列表
    let domList = document.querySelectorAll('.character-list__item')
    // 遍历列表
    for (let i = 0; i < domList.length; i++) {
        let _a = domList[i].children[0]; // 获取a标签
        let _img = _a.children[0].children[0]; // 获取图片标签
        let _div2 = _a.childNodes[3]; // 获取unicode码所在标签

        let emoji = {
            id: '',
            name: _a.getAttribute('title'),
            url: _img.getAttribute('data-src'),
            code: _div2.innerText,
            html: _a.getAttribute('data-symbol'),
            class: '',
            text: '[' + _a.getAttribute('title') + ']',
        }
        emojiList.push(emoji)
    }

    return emojiList;
}

let r = fnGetEmojiList()
console.table(r)
console.log(JSON.stringify(r))
```
