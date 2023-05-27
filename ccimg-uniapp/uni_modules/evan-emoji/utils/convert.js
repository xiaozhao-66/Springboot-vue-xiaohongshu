/**
 * 转换工具
 */

const validStr = function(msg) {
	return msg && (msg + '').trim();
}

const emojiTxtReg = /\[(.*?)\]/g;

const getImgHtml = function(classNames, src, alt, options) {
	let styles = options.styles || {};
	let width = options.width || 40;
	let height = options.height || 40;
	styles = {
		...styles,
		width: uni.upx2px(width) + 'px',
		height: uni.upx2px(height) + 'px'
	}
	let styleStr = ''
	for (let style in styles) {
		styleStr += `${style}:${styles[style]};`
	} 
	let className = classNames.join(' ').trim();
	return `<img style="${styleStr}" class="${className}" src="${src}" alt="[${alt}]"/>`;
};

/**
 * resourceItem
 * @typedef {Object} resourceItem
 * @property {string} text
 * @property {string} class
 * @property {?string} url  - string or null
 * @property {?string} code - string or null
 * @property {?RegExp} reg  - string or null
 */

/**
 * @type {resourceItem[]}
 */
let resource = [];

/**
 * @type {Object.<string, resourceItem>}
 */
let nameMap = {};

const Convert = {

	/**
	 * @param {Array.<{name: string, class: string, url:string, ?code:string }>} args[i]
	 */
	extend(...args) {

		args.forEach(function(list) {

			if (!Array.isArray(list)) return;

			list.forEach(function(item) {

				var code = item.code;
				if (code == null) return;

				if (typeof code === 'string') {
					item.reg = new RegExp(code, 'g');
				} else {
					item.code = null;
					throw new Error(JSON.stringify(item) + ': code muse be String')
				}

				// nameMap[item.text] = item; // 源码
				nameMap[item.name] = item;
			});

			resource = resource.concat(list);
		});

	},

	/**
	 * [xxx]-->unicode字符
	 * @param {string} msg
	 * @returns {string}
	 */
	toUnicode(msg) {
		msg = validStr(msg);
		if (!msg) return msg;

		return msg.replace(emojiTxtReg, function(match, text) {
			var item = nameMap[text];
			return item && item.code || match;
		});
	},

	/**
	 * unicode字符-->[xxx]
	 * @param {string} msg
	 * @returns {string}
	 */
	toText(msg) {
		msg = validStr(msg);
		if (!msg) return msg;

		//unicode
		resource.forEach(function(item) {
			if (!item.code) return;

			msg = msg.replace(item.reg, '[' + item.text + ']');
		})

		return msg;
	},

	/**
	 * [xx]和unicode字符 --> img标签
	 * @param {string} msg
	 * @param {boolean} [passU] true:不转换unicode表情
	 */
	toHtml(msg, passU = false, options = {
		classes: [],
		style: {}
	}) {
		msg = validStr(msg);
		if (!msg) return;
		var item;
		msg = msg.replace(emojiTxtReg, function(match, text) {
			item = nameMap[text];
			if (item) return getImgHtml([item.class, ...options.classes], item.url, item.name, options);
			else return match
		});
		if (passU) return;

		//unicode
		resource.forEach(function(item) {
			if (!item.code) return;

			msg = msg.replace(item.reg, function() {
				return getImgHtml([item.class, ...options.classes], item.url, item.name, options);
			})

		})

		return msg;

	}

}

export default Convert;
