import App from './App'
import GoEasy from '@/node_modules/GOEASY-IM/js_sdk/goeasy-2.8.3.esm.min.js'


// #ifndef VUE3
import Vue from 'vue'
import EvanEmoji from '@/uni_modules/evan-emoji/utils/index.js'
import {appConfig} from '@/config/config.js'

Vue.config.productionTip = false
Vue.use(EvanEmoji)
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif


const goEasy = GoEasy.getInstance({
    host: 'hangzhou.goeasy.io',	//应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
    appkey: appConfig.ImKey,	// common key,
    modules: ['im'],
    // true表示支持通知栏提醒，false则表示不需要通知栏提醒
    allowNotification: true //仅有效于app,小程序和H5将会被自动忽略
});

goEasy.im.on(GoEasy.IM_EVENT.CONVERSATIONS_UPDATED, setUnreadNumber);
function setUnreadNumber (content) {
    let unreadTotal = content.unreadTotal;
	uni.setStorageSync("unreadTotal",unreadTotal)
  //   if(unreadTotal > 0) {
  //       // uni.setTabBarBadge({
  //       //     index: 2,
  //       //     text: unreadTotal.toString()
  //       // });
		// uni.showTabBarRedDot({ //显示红点
		// 	index: 2 //tabbar下标
		// })
  //   }else{
  //       //uni.removeTabBarBadge({index: 0});
		// uni.hideTabBarRedDot({
		// 	index: 2
		// })
  //   }
  //   // #ifdef APP-PLUS
  //   goEasy.setBadge({
  //     badge: unreadTotal,
  //     onSuccess: function () {
  //       console.log("setBadge successfully.")
  //     },
  //     onFailed: function (error) {
  //       console.log("Failed to setBadge,error:" + error);
  //     }
  //   });
    // #endif
}

goEasy.onClickNotification((message) => {
    let currentUrl;
    const routes = getCurrentPages();

    if (routes && routes.length) {
        const curRoute = routes[routes.length - 1].route;
        const curParam = routes[routes.length - 1].options;
        currentUrl = '/' + curRoute + `?to=${curParam.to}`;
    }

    let newUrl;
    switch (message.toType) {
        case GoEasy.IM_SCENE.PRIVATE:
            newUrl = '/pages/message/privateChat/privateChat?to=' + message.senderId;
            break;
        case GoEasy.IM_SCENE.GROUP:
            newUrl = '/pages/groupChat?to=' + message.groupId;
            break;
    }

    if (currentUrl !== newUrl) {
        uni.navigateTo({
            url: newUrl,
        });
    }

});

Vue.prototype.GoEasy = GoEasy;
Vue.prototype.goEasy = goEasy;