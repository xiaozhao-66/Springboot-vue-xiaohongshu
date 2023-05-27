import App from './App'



// #ifndef VUE3
import Vue from 'vue'
import EvanEmoji from '@/uni_modules/evan-emoji/utils/index.js'

Vue.config.productionTip = false
Vue.use(EvanEmoji)
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif
