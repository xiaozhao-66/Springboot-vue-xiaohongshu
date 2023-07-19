import ConvertUtil from '@/uni_modules/evan-emoji/utils/convert.js';
const install = (Vue) => {
	uni.$evanEmoji = {
		util: {
			convert: ConvertUtil
		}
	};
	Vue.prototype.$evanEmoji = {
		util: {
			convert: ConvertUtil
		}
	};
}

export default {
	install
}

