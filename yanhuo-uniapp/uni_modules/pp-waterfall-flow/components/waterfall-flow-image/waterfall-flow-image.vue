<template>
	<view class="zero-content" :style="{
			  opacity: Number(opacity),
			  borderRadius: borderRadius,
			  // 因为time值需要改变,所以不直接用duration值(不能改变父组件prop传过来的值)
			  transition: `opacity ${time / 1000}s ease-in-out`
		   }" :class="'zero-lazy-item-' + elIndex">
		<view :class="'zero-lazy-item-' + elIndex" style="position: relative;">
			<view class="loading-container" v-if="(!isShow||loading)&&!isError">
				<image class="loading" :src="loadingImg" mode="aspectFit"></image>
			</view>
			<image :style="{borderRadius: borderRadius, height: imgHeight}" v-if="!isError&&image" class="zero-lazy-item" :src="isShow ? image : load" :mode="imgMode" @load="imgLoaded" @error="loadError" @tap.stop="clickImg"></image>
			<image :style="{borderRadius: borderRadius, height: imgHeight,'background-image':'url('+errorImg+')','background-size':avator?'cover':''}" class="zero-lazy-item error" v-else :src="load" :mode="imgMode" @load="errorImgLoaded" @tap.stop="clickImg"></image>
		</view>
	</view>
</template>
<script>
	/**
	 * lazyLoad 懒加载 (本组件基于uview lazyLoad开发, https://v1.uviewui.com/components/lazyLoad.html)
	 * @description 懒加载使用的场景为：页面有很多图片时，APP会同时加载所有的图片，导致页面卡顿，各个位置的图片出现前后不一致等.
	 * @property {String Number} index 用户自定义值，在事件触发时回调，用以区分是哪个图片
	 * @property {String} image 图片路径
	 * @property {String} threshold 触发加载时的位置，见上方说明，单位 rpx（默认300）
	 * @property {String Number} duration 图片加载成功时，淡入淡出时间，单位ms（默认）
	 * @property {String} effect 图片加载成功时，淡入淡出的css动画效果（默认ease-in-out）
	 * @property {Boolean} is-effect 图片加载成功时，是否启用淡入淡出效果（默认true）
	 * @property {String Number} border-radius 图片圆角值，单位rpx（默认0）
	 * @property {String Number} height 图片高度，注意：实际高度可能受img-mode参数影响（默认450）
	 * @property {String Number} mg-mode 图片的裁剪模式，详见image组件裁剪模式（默认widthFix）
	 * @event {Function} click 点击图片时触发
	 * @event {Function} load 图片加载成功时触发
	 * @event {Function} error 图片加载失败时触发
	 * @example <zero-lazy-load :image="image" ></zero-lazy-load>
	 */
	//验证十进制数字
	function number(value) {
		return /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value)
	}
	// 全局唯一标识符
	function guid(len = 32, firstU = true, radix = null) {
		let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
		let uuid = [];
		radix = radix || chars.length;
		if (len) {
			// 如果指定uuid长度,只是取随机的字符,0|x为位运算,能去掉x的小数位,返回整数位
			for (let i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
		} else {
			let r;
			// rfc4122标准要求返回的uuid中,某些位为固定的字符
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			uuid[14] = '4';
			for (let i = 0; i < 36; i++) {
				if (!uuid[i]) {
					r = 0 | Math.random() * 16;
					uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
				}
			}
		}
		// 移除第一个字符,并用u替代,因为第一个字符为数值时,该guuid不能用作id或者class
		if (firstU) {
			uuid.shift();
			return 'u' + uuid.join('');
		} else {
			return uuid.join('');
		}
	}
	export default {
		name: 'zero-lazy-load',
		emits: ['click', 'load', 'error', 'finish'],
		props: {
			index: {
				type: [Number, String]
			},
			// 要显示的图片
			image: {
				type: String,
				default: ''
			},
			// 图片裁剪模式
			imgMode: {
				type: String,
				default: 'widthFix'
			},
			// 占位图片路径
			loadingImg: {
				type: String,
				default: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAYAAACAvzbMAAAAAXNSR0IArs4c6QAAFPZJREFUeF7t3VuIZelVB/Dva+ol6NBeiDcEIyOGZIIiTryRhxEREjWZs/ekVAzImEhAQcgoXt6MT0IM5lFFwRZFIUWdfdrWdAiKvgTFKITIBDUJhpgoOEMYLw9h6KpPjvRoT3dVTbGypvtUrV+9dq119vqtBf+pOXXpzQcBAgQIEAgI9ECNEgIECBAg0ASIIyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaIAAECBASIGyBAgACBkIAACbEpIkCAAAEB4gYIECBAICQgQEJsiggQIEBAgLgBAgQIEAgJCJAQmyICBAgQECBugAABAgRCAgIkxKaogsA0TXNr7eHW2rNjjM9sNps/rzC3GQmcV0CAnFfK55USmKbpH1prr75r6GVZlm2o+CBAoLUmQJwBgbsEpml6f2tt/ySYMcZPbzab34BGgIAAcQMEXiQwTdPbWmt/cBbLGOPLN5vNc+gIVBfwFUj1CzD/iwTmef7dMcaTLxEg37bZbD6KjkB1AQFS/QIKzf/444+/9vnnn//szZs3//O0sed5fu8Y4+fOYjk+Pn7k+vXrHy9EZ1QCJwoIEIdx6QVWq9W7e+9vba09sh22937j+Pj4qc1m86m7h5+m6Y2ttZtnoPzr1atXH7527doXLj2cAQm8hIAAcSKXWmCapu37Gdv3Ne75OO0riWma1q216RSYn1qW5TcvNZrhCJxTQICcE8qnXTyB1Wr1pt77B8548qevXr366ElfTdz+quWX76j9p9vfgXXmz4LM8/xjY4yf3dZduXLl7YeHhx+7eHKemMD5BATI+Zx81gUUmOf5fWOMd5316FeuXHnN4eHh9mc+7vnY39//mqOjo1f13p976KGHPv1S/9tqf3//FUdHR8+MMb7kdrPPL8vylReQziMTOJeAADkXk0+6iAJn/TzHC/P03r9jvV5/JGO+aZpe01p70ZvrV65c+ebDw8NPZPTXg8CuCQiQXduI50kTWK1W7+i9/85LNPzqZVn+PetFV6vVR3rvj2779d7/bL1ef39Wb30I7JqAANm1jXieNIF5nr92jPG3rbWvO6np9rux1uv1W9JesLW2/Vbh3vt3996/sLe3d/Pg4ODzmf31IrBLAgJkl7bhWdIFTngz/IXXeHpZltelv6CGBAoJCJBCy6466jRNb+i9/8gY45taa8+NMf5xs9m8u6qHuQlkCQiQLEl9CBAgUExAgBRbuHEJECCQJSBAsiT1IUCAQDEBAVJs4cYlQIBAloAAyZLUZycEpmm63lr7lt77K8cYHx9jfMAb5juxGg9xCQUEyCVcatWRpmkap8x+sCzLDz9Il9Vq9arW2mObzebag3wOr00gU0CAZGrq9cAEpmn61dbaL532AL33t63X6z98EA941x+p+nRr7WeWZfmTB/EsXpNApoAAydTU64EJTNP0ydbaw2cEyO+v1+sfv98POM/zd40x/urO1305fgL+fs/l9QhsBQSIO7gUAvM8//cdvwX3npluvxfyg/d72Hmef3KM8dt3ve7nlmX5+vv9LF6PQLaAAMkW1e+BCEzT9Dettdef8eLvW5blf/9Ox/382N/f/4pbt259trX2ihded4zxK97Yv59b8Fovl4AAeblk9b2vAmf8zqsXnuPND+p9h9Vq9WTvffvHqbZ/W+Ta8fHxNkC274X4IHChBQTIhV6fh79TYJ7nZYyxultlV/6Lf/udWILDzV4mAQFymbZplnb7K5FXt9a+rPf+yd777x0eHm5/pbsPAgSSBQRIMqh2BAgQqCIgQKps2pwECBBIFhAgyaDaESBAoIqAAKmyaXMSIEAgWUCAJINqt3sCq9XqF3rvb+29v3aM8Uzv/e+Pj4+f2mw2n9q9p/VEBC6OgAC5OLvypAGBeZ5/a4zxzpNK9/b2vuHg4OAzgbanltz+vVePbT+h975Zr9dPZfbXi8AuCQiQXdqGZ0kVWK1W3957P+tbeP90WZYfynrReZ7/eozxnXf2672/Z71e/2LWa+hDYJcEBMgubcOzpAqc46fT2xjjGzN+uG+apv3W2vtP+Urnqw4ODp5JHU4zAjsgIEB2YAke4eURmKbpL7Z/g+Os7mOM791sNn/5xT7BWWGV9Rpf7DOqJ5AtIECyRfXbGYFpmrbvP/z6WQ+U9T7IPM9vGWNs/xriPR+3bt165Y0bN57dGRgPQiBJQIAkQWqzewLzPL9+jLH9Lb2nfXx4WZY3nPSPq9XqHb33J1tr39pa+7fe+4eOjo7ec/369X85rdk0TX/UWvvRO/99V34P1+5txxNdBgEBchm2aIZTBc74X0tPL8vyulPC47He+/Z/f9398Xd7e3vfd3Bw8B9nhMjbW2tvvP3v2z+le2A9BC6rgAC5rJs11/8JPPHEE48eHx+/q7X2pt77J46Pjz941t/jmOf5w2OM7zmF8L3Lsvw8XgIE/EVCN0DgHoFpmv6rtfalp9DcXJblB7ARICBA3ACBkwLkn7d//Okkmu0fhFqv1z+BjQABAeIGCNwjMM/zH48x3nwSjTfFHQyB/xfwHohrIHCCwDRN93wVIjycCoEXCwgQF0HgFIFpmn6ttfZI7/3ZMcYN31HlVAgIEDdAgAABAgkCvgJJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgoAASUDUggABAhUFBEjFrZuZAAECCQICJAFRCwIECFQUECAVt25mAgQIJAgIkARELQgQIFBRQIBU3LqZCRAgkCAgQBIQtSBAgEBFAQFScetmJkCAQIKAAElA1IIAAQIVBQRIxa2bmQABAgkCAiQBUQsCBAhUFBAgFbduZgIECCQICJAERC0IECBQUUCAVNy6mQkQIJAgIEASELUgQIBARQEBUnHrZiZAgECCgABJQNSCAAECFQUESMWtm5kAAQIJAgIkAVELAgQIVBQQIBW3bmYCBAgkCAiQBEQtCBAgUFFAgFTcupkJECCQICBAEhC1IECAQEUBAVJx62YmQIBAgsD/ABIX8KDNwgp1AAAAAElFTkSuQmCC'
			},
			// 加载失败的错误占位图
			errorImg: {
				type: String,
				default: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAYAAACAvzbMAAAAAXNSR0IArs4c6QAAHC9JREFUeF7t3Q2QpHldH/D/v+d2PXeRkxcPzAEWQS0IGGIoKL34ghJAoyBQgBS+JPhy4uZmnn/vnXeKL3hRA17u5vn3bXGE8wUsFEQ0iqggGnw3iSaEJJoyipYEjsPcnXoge3CzM3/rmXRP9Q6zuzO/ndmZqfp01dTsdD/f//PMp3913+vp7qdzciFAgAABAgGBHMiIECBAgACBpEAMAQECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQmxABAgQIKBAzQIAAAQIhAQUSYhMiQIAAAQViBggQIEAgJKBAQmxCBAgQIKBAzAABAgQIhAQUSIhNiAABAgQUiBkgQIAAgZCAAgmxCREgQICAAjEDBAgQIBASUCAhNiECBAgQUCBmgAABAgRCAgokxCZEgAABAgrEDBAgQIBASECBhNiECBAgQECBmAECBAgQCAkokBCbEAECBAgoEDNAgAABAiEBBRJiEyJAgAABBWIGCBAgQCAkoEBCbEIECBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQmxABAgQIKBAzQIAAAQIhAQUSYhMiQIAAAQViBggQIEAgJKBAQmxCBAgQIKBAzAABAgQIhAQUSIhNiAABAgQUiBkgQIAAgZCAAgmxCREgQICAAjEDBAgQIBASUCAhNiECBAgQUCBmgAABAgRCAgokxCZEgAABAgrEDBAgQIBASECBhNiECBAgQECBmAECBAgQCAkokBCbEAECBAgoEDNAgAABAiEBBRJiEyJAgAABBWIGCBAgQCAkoEBCbEIECBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQmxABAgQIKBAzQIAAAQIhAQUSYhMiQIAAAQViBggQIEAgJKBAQmxCBAgQIKBAzAABAgQIhAQUSIhNiAABAgQUiBkgQIAAgZCAAgmxCREgQICAAjEDBAgQIBASUCAhNiECBAgQUCBmgAABAgRCAgokxCZEgAABAgrEDBAgQIBASECBhNiECBAgQECBmAECBAgQCAkokBCbEAECBAgoEDNAgAABAiEBBRJiEyJAgAABBWIGCBAgQCAkoEBCbEIECBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQ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CBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQmxABAgQIKBAzQIAAAQIhAQUSYhMiQIAAAQViBggQIEAgJKBAQmxCBAgQIKBAzAABAgQIhAQUSIhNiAABAgQUiBkgQIAAgZCAAgmxCREgQICAAjEDBAgQIBASUCAhNiECBAgQUCBmgAABAgRCAgokxCZEgAABAgrEDBAgQIBASECBhNiECBAgQECBmAECBAgQCAkokBCbEAECBAgoEDNAgAABAiEBBRJiEyJAgAABBWIGCBAgQCAkoEBCbEIECBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICCiTEJkSAAAECCsQMECBAgEBIQIGE2IQIECBAQIGYAQIECBAICSiQEJsQAQIECCgQM0CAAAECIQEFEmITIkCAAAEFYgYIECBAICSgQEJsQgQIECCgQMwAAQIECIQEFEiITYgAAQIEFIgZIECAAIGQgAIJsQkRIECAgAIxAwQIECAQElAgITYhAgQIEFAgZoAAAQIEQgIKJMQmRIAAAQIKxAwQIECAQEhAgYTYhAgQIEBAgZgBAgQIEAgJKJAQmxABAgQIKBAzQIAAAQIhAQUSYhMiQIAAAQViBggQIEAgJKBAQmxCBAgQIKBAzAABAgQIhAQUSIhNiAABAgQUiBkgQIAAgZCAAgmxCREgQICAAjEDBAgQIBASUCAhNiECBAgQUCBmgAABAgRCAgokxCZEgAABAgrEDBAgQIBASECBhNiECBAgQECBmAECBAgQCAkokBCbEAECBAgoEDNAgAABAiEBBRJiEyJAgAABBWIGCBAgQCAkoEBCbEIECBAgoEDMAAECBAiEBBRIiE2IAAECBBSIGSBAgACBkIACCbEJESBAgIACMQMECBAgEBJQICE2IQIECBBQIGaAAAECBEICfw/SotDNxtksnAAAAABJRU5ErkJggg=='
			},
			// 图片进入可见区域前多少像素时，单位rpx，开始加载图片
			// 负数为图片超出屏幕底部多少距离后触发懒加载，正数为图片顶部距离屏幕底部多少距离时触发(图片还没出现在屏幕上)
			threshold: {
				type: [Number, String],
				default: 100
			},
			// 淡入淡出动画的过渡时间
			duration: {
				type: [Number, String],
				default: 500
			},
			// 渡效果的速度曲线，各个之间差别不大，因为这是淡入淡出，且时间很短，不是那些变形或者移动的情况，会明显
			// linear|ease|ease-in|ease-out|ease-in-out|cubic-bezier(n,n,n,n);
			effect: {
				type: String,
				default: 'ease-in-out'
			},
			// 是否使用过渡效果
			isEffect: {
				type: Boolean,
				default: true
			},
			// 圆角值
			borderRadius: {
				type: [String],
				default: 0
			},
			// 图片高度，单位rpx
			height: {
				type: [Number, String],
				default: '300'
			},
			// 是否是头像模式
			avator: {
				type: Boolean,
				default: false
			}
		},
		data() {
			return {
				isShow: false,
				loading: true,
				opacity: 1,
				time: this.duration,
				loadStatus: '', // 默认是懒加载中的状态
				isError: false, // 图片加载失败
				elIndex: guid(),
				load: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjYAAAAAIAAeIhvDMAAAAASUVORK5CYII='
			}
		},
		computed: {
			// 将threshold从rpx转为px
			getThreshold() {
				// 先取绝对值，因为threshold可能是负数，最后根据this.threshold是正数或者负数，重新还原
				let thresholdPx = uni.upx2px(Math.abs(this.threshold));
				return this.threshold < 0 ? -thresholdPx : thresholdPx;
			},
			// 计算图片的高度，可能为auto，带%，或者直接数值
			imgHeight() {
				return this.addUnit(this.height);
			}
		},
		created() {
			// 由于一些特殊原因，不能将此变量放到data中定义
			this.observer = {};
		},
		watch: {
			isShow(nVal) {
				// 如果是不开启过渡效果，直接返回
				if (!this.isEffect) return;
				this.time = 0;
				// 原来opacity为1(不透明，是为了显示占位图)，改成0(透明，意味着该元素显示的是背景颜色，默认的白色)，再改成1，是为了获得过渡效果
				this.opacity = 0;
				// 延时30ms，否则在浏览器H5，过渡效果无效
				setTimeout(() => {
					this.time = this.duration;
					this.opacity = 1;
				}, 30)
			},
			// 图片路径发生变化时，需要重新标记一些变量，否则会一直卡在某一个状态，比如isError
			image(n) {
				if (!n) {
					// 如果传入null或者''，或者undefined，标记为错误状态
					this.isError = true;
				} else {
					this.init();
					this.isError = false;
				}
			}
		},
		methods: {
			// 用于demo改变展示状态
			changeStatus(val) {
				if (val === 0) {
					this.isShow = false;
					this.loadStatus = 'lazyed';
					this.isError = false;
				} else if (val === 1) {
					this.isShow = false;
					this.isError = true;
				} else {
					this.loadStatus = 'loaded';
					this.isError = false
					this.isShow = true;
				}
			},
			addUnit(value = 'auto', unit = 'rpx') {
				value = String(value);
				return number(value) ? `${value}${unit}` : value;
			},
			// 用于重新初始化
			init() {
				this.isError = false;
				this.loadStatus = '';
			},
			// 点击图片触发的事件,loadlazy-还是懒加载中状态，loading-图片正在加载，loaded-图片加加载完成
			clickImg() {
				let whichImg = '';
				// 如果isShow为false，意味着图片还没开始加载，点击的只能是最开始的占位图
				if (this.isShow == false) whichImg = 'lazyImg';
				// 如果isError为true，意味着图片加载失败，这是只剩下错误的占位图，所以点击的只能是错误占位图
				// 当然，也可以给错误的占位图元素绑定点击事件，看你喜欢~
				else if (this.isError == true) whichImg = 'errorImg';
				// 总共三张图片，除了两个占位图，剩下的只能是正常的那张图片了
				else whichImg = 'realImg';
				// 只通知当前图片的index
				this.$emit('click', this.index);
			},
			// 图片加载完成事件，可能是加载占位图时触发，也可能是加载真正的图片完成时触发，通过isShow区分
			imgLoaded(e) {
				// 占位图加载完成
				if (this.loadStatus == '') {
					this.loadStatus = 'lazyed';
				}
				// 真正的图片加载完成 
				else if (this.loadStatus == 'lazyed') {
					this.loadStatus = 'loaded';
					this.$emit('load', e);
					// 成功与否都传出,看情况使用哪个
					this.$emit('finish', { e: e, index: this.index });
					setTimeout(() => {
						this.loading = false;
					}, 100)
				}
			},
			// 错误的图片加载完成
			errorImgLoaded(e) {
				this.$emit('error', e);
				// 成功与否都传出,看情况使用哪个
				this.$emit('finish', { e: e, index: this.index });
			},
			// 图片加载失败
			loadError() {
				this.isError = true;
			},
			disconnectObserver(observerName) {
				const observer = this[observerName];
				observer && observer.disconnect();
			},
		},
		beforeDestroy() {
			// 销毁页面时，可能还没触发某张很底部的懒加载图片，所以把这个事件给去掉
			//observer.disconnect();
		},
		mounted() {
			// 由父组件生命周期onReachBottom发出，目的是让页面到底时，保证所有图片都进行加载，做到绝对稳定且可靠
			this.$nextTick(() => {
				uni.$once('onReachBottom', () => {
					// console.log('触底监听')
					if (!this.isShow) this.isShow = true;
				});
			})
			// mounted的时候，不一定挂载了这个元素，延时30ms，否则会报错或者不报错，但是也没有效果
			setTimeout(() => {
				// 这里是组件内获取布局状态，不能用uni.createIntersectionObserver，而必须用this.createIntersectionObserver
				this.disconnectObserver('contentObserver');
				const contentObserver = uni.createIntersectionObserver(this);
				// 要理解IntersectionObserver工作原理,可参考：
				// https://www.ruanyifeng.com/blog/2016/11/intersectionobserver_api.html
				// 或
				// https://blog.csdn.net/qq_25324335/article/details/83687695
				contentObserver.relativeToViewport({
					bottom: this.getThreshold,
				}).observe('.zero-lazy-item-' + this.elIndex, (res) => {
					if (res.intersectionRatio > 0) {
						// 懒加载状态改变
						this.isShow = true;
						// 如果图片已经加载，去掉监听，减少性能的消耗
						this.disconnectObserver('contentObserver');
					}
				})
				this.contentObserver = contentObserver;
			}, 30)
		}
	}
</script>
<style lang="scss" scoped>
	.zero-content {
		overflow: hidden;
	}
	.loading-container {
		width: 240rpx;
		height: 240rpx;
		position: absolute;
		top: 50%;
		left: 50%;
		right: 0;
		z-index: 9;
		transform: translate(-50%, -55%);
		.loading {
			width: 100%;
			height: 100%;
			animation: move 1.2s linear infinite;
		}
	}
	.zero-lazy-item {
		width: 100%;
		// 骗系统开启硬件加速
		transform: transition3d(0, 0, 0);
		// 防止图片加载“闪一下”
		will-change: transform;
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		&.error {
			background-position: center center;
			background-repeat: no-repeat;
			background-color: #f2f2f2;
		}
		&-loading {
			background: url('') no-repeat center center;
			background-size: 260rpx;
		}
	}
	@keyframes move {
		from {
			transform: rotate(0);
		}
		to {
			transform: rotate(360deg);
		}
	}
</style>