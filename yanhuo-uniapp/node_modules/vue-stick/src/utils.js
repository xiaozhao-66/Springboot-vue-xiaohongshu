//图片预加载
export function getImgSize(src,callback){
  if(!src){
    setTimeout(callback, 0)
    return;
  }
  var img = new Image();
  function End(){
    clearInterval(timer);
    callback && callback();
    callback = null;
  }
  img.onerror = img.onload = End;
  var timer = setInterval(function(){
    img.width>1 && End();
  },2);
  img.src=src;
}

/**
 * 获取浏览器滚动尺寸
 *  为了兼容firefox
 */
export function getScrollTop(){
  return Math.max(document.documentElement.scrollTop, document.body.scrollTop);
}