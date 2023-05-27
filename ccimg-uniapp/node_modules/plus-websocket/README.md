# plus-websocket
在 HTML5+ 和 WEB 环境使用小程序风格的 websocket 接口，支持 H5、5+APP、uni-app（不含小程序，小程序环境请直接使用 uni 接口）。

也可以用于解决 uni-app 环境下不支持 ArrayBuffer 类型数据和不支持多个 websocket 连接的问题以及解决使用 websocket 后导致部分安卓设备白屏的问题。

## 使用方式

### NPM

```
npm i plus-websocket --save
```

```js
import socket from 'plus-websocket'
```

### 直接下载

```js
// 以下路径需根据项目实际情况填写
import socket from '../../js/plus-websocket/index.js'
```

## API

详细用法可参考 [uni-app文档](https://uniapp.dcloud.io/api/request/websocket)

* socket.connectSocket(OBJECT)
    * SocketTask.onMessage(CALLBACK)
    * SocketTask.send(OBJECT)
    * SocketTask.close(OBJECT)
    * SocketTask.onOpen(CALLBACK)
    * SocketTask.onClose(CALLBACK)
    * SocketTask.onError(CALLBACK)
* socket.onSocketOpen(CALLBACK)
* socket.onSocketError(CALLBACK)
* socket.sendSocketMessage(OBJECT)
* socket.onSocketMessage(CALLBACK)
* socket.closeSocket(OBJECT)
* socket.onSocketClose(CALLBACK)

## 注意事项

当在 uni-app 中使用时也可用当前 API 替换 uni-app 内置的 websocket API

```js
// main.js
import socket from 'plus-websocket'

// #ifdef APP-PLUS
Object.assign(uni, socket)
// #endif
```