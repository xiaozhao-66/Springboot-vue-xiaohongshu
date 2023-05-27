<template>
  <view class="waterfalls-box" :style="{ height: height + 'px' }">
    <!--  #ifdef  MP-WEIXIN -->
    <view
      v-for="(item, index) of list"
      class="waterfalls-list"
      :key="item[idKey]"
      :id="'waterfalls-list-id-' + item[idKey]"
      :ref="'waterfalls-list-id-' + item[idKey]"
      :style="{
        '--offset': offset + 'px',
        '--cols': cols,
        top: allPositionArr[index].top || 0,
        left: allPositionArr[index].left || 0,
      }"
      @click="$emit('wapper-lick', item)"
    >
      <image
        class="waterfalls-list-image"
        mode="widthFix"
        :class="{ single }"
        :style="imageStyle"
        :src="item[imageSrcKey] || ' '"
        @load="imageLoadHandle(index)"
        @error="imageLoadHandle(index)"
        @click="$emit('image-click', item)"
      />
      <slot name="slot{{index}}" />
    </view>
    <!--  #endif -->

    <!--  #ifndef  MP-WEIXIN -->
    <view
      v-for="(item, index) of list"
      class="waterfalls-list"
      :key="item[idKey]"
      :id="'waterfalls-list-id-' + item[idKey]"
      :ref="'waterfalls-list-id-' + item[idKey]"
      :style="{
        '--offset': offset + 'px',
        '--cols': cols,
        ...listStyle,
        ...(allPositionArr[index] || {}),
      }"
      @click="$emit('wapper-lick', item)"
    >
      <image
        class="waterfalls-list-image"
        :class="{ single }"
        mode="widthFix"
        :style="imageStyle"
        :src="item[imageSrcKey] || ' '"
        @load="imageLoadHandle(index)"
        @error="imageLoadHandle(index)"
        @click="$emit('image-click', item)"
      />
      <slot v-bind="item" />
    </view>
    <!--  #endif -->
  </view>
</template>
<script>
export default {
  props: {
    list: { type: Array, required: true },
    // offset 间距，单位为 px
    offset: { type: Number, default: 3 },
    // 列表渲染的 key 的键名，值必须唯一，默认为 id
    idKey: { type: String, default: "id" },
    // 图片 src 的键名
    imageSrcKey: { type: String, default: "cover" },
    // 列数
    cols: { type: Number, default: 2, validator: (num) => num >= 2 },
    imageStyle: { type: Object },

    // 是否是单独的渲染图片的样子，只控制图片圆角而已
    single: { type: Boolean, default: false },

    // #ifndef MP-WEIXIN
    listStyle: { type: Object },
    // #endif
  },
  data() {
    return {
      topArr: [], // left, right 多个时依次表示第几列的数据
      allPositionArr: [], // 保存所有的位置信息
      allHeightArr: [], // 保存所有的 height 信息
      height: 0, // 外层包裹高度
      oldNum: 0,
      num: 0,
    };
  },
  created() {
    this.refresh();
  },
  methods: {
    imageLoadHandle(index) {
      const id = "waterfalls-list-id-" + this.list[index][this.idKey],
        query = uni.createSelectorQuery().in(this);
      query
        .select("#" + id)
        .fields({ size: true }, (data) => {
          this.num++;
          this.$set(this.allHeightArr, index, data.height);
          if (this.num === this.list.length) {
            for (let i = this.oldNum; i < this.num; i++) {
              const getTopArrMsg = () => {
                let arrtmp = [...this.topArr].sort((a, b) => a - b);
                return {
                  shorterIndex: this.topArr.indexOf(arrtmp[0]),
                  shorterValue: arrtmp[0],
                  longerIndex: this.topArr.indexOf(arrtmp[this.cols - 1]),
                  longerValue: arrtmp[this.cols - 1],
                };
              };

              const { shorterIndex, shorterValue } = getTopArrMsg();
              const position = {
                top: shorterValue + "px",
                left: (data.width + this.offset) * shorterIndex + "px",
              };
              this.$set(this.allPositionArr, i, position);
              this.topArr[shorterIndex] =
                shorterValue + this.allHeightArr[i] + this.offset;
              this.height = getTopArrMsg().longerValue - this.offset;
            }
            this.oldNum = this.num;
            // 完成渲染 emit `image-load` 事件
            this.$emit("image-load");
          }
        })
        .exec();
    },
    refresh() {
	  console.log("子组件触发")
      let arr = [];
      for (let i = 0; i < this.cols; i++) {
        arr.push(0);
      }
      this.topArr = arr;
      this.num = 0;
      this.oldNum = 0;
      this.height = 0;
    },
  },
};
</script>
<style lang="scss" scoped>
// 这里可以自行配置
$border-radius: 6px;

.waterfalls-box {
  position: relative;
  width: 100%;
  overflow: hidden;
  .waterfalls-list {
    width: calc((100% - var(--offset) * (var(--cols) - 1)) / var(--cols));
    position: absolute;
    background-color: #fff;
    border-radius: $border-radius;
    // 防止刚开始渲染时堆叠在第一幅图的地方
    left: calc(-50% - var(--offset));
    .waterfalls-list-image {
      width: 100%;
	  // max-height: 600rpx;
      will-change: transform;
      border-radius: $border-radius $border-radius 0 0;
      display: block;
      &.single {
        border-radius: $border-radius;
      }
    }
  }
}
</style>
