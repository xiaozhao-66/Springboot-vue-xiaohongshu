let list = ()=>{
	return new Promise((resolve,reject)=>{
		setTimeout(() => {
			// 生成随机数方法
			let random = (min = 0, max) => {
				return Math.floor(Math.random() * max) + min;
			}
			// 待选的图片数据
			let imgs = [];
			// 待选的标题数据
			let titles = [
				'桃花坞里桃花庵，桃花庵里桃花仙；',
				'桃花仙人种桃树，又摘桃花卖酒钱。',
				'酒醒只在花前坐，酒醉还来花下眠；半醒半醉日复日，花落花开年复年。',
				'但愿老死花酒间，不愿鞠躬车马前；',
				'车尘马足富者趣，酒盏花枝贫者缘。若将富贵比贫贱，',
				'一在平地一在天；若将贫贱比车马，他得驱驰我得闲。',
				'别人笑我太疯癫，我笑他人看不穿；不见五陵豪杰墓，无花无酒锄作田。'
			];
		
			let res = [];
			for (let i = 0; i < 10; i++) {
				res.push({
					id:i+1,
					url:`/uni_modules/helang-waterfall/static/waterfall/${random(0,3)}.jpg?t=${new Date().getTime()}`,
					title: titles[random(0, titles.length)],
					money: random(9, 9999),
					label:'官方自营',
					shop:'唐诗三百首旗舰店'
				})
			}			
			resolve(res);
		}, 500);
	})
}

export default {
	getList:list
}