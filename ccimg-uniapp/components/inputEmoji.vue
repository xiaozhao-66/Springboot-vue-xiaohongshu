<template>
    <!-- 发布表情的组件-->
    <view class="consult">

        <view :class="['keyboard', useful_flag ? 'keyboard-active' : '']">
            <view :class="['keyboard-tup', active_Up ? 'active-tup' : '']">
                <input v-model="value" :focus="cursor" :cursor="resStart" @keyboardheightchange.stop="openinput"
                    @blur="cursorss" @confirm="startSearch" @input="change" />

                <view v-if="T" style="position: absolute; left: 80rpx; color: #878787;">{{ placeholder }}</view>
                <view class="imface">
                    <tui-icon class="imface" name="imface" size="20" @click="openUseful(1)"></tui-icon>
                </view>

                <tui-button type="danger" height="60rpx" width="100rpx" @click="addComment">发送</tui-button>
                <!-- <image class="useful" src="/static/logo.png" mode="" @click="openUseful(2)"></image> -->
            </view>
            <!--常用语  -->
            <view class="useful-content" v-if="useful_expression === 2">
                <view class="useful-content-xiaoxi" v-for="item in 9">你好，可以帮我推荐几个兼职任务吗？</view>
            </view>
            <!-- 表情 -->
            <view class="expression" v-if="useful_expression === 1">
                <scroll-view class="emojis_scroll" :scroll-x="false" :scroll-y="true">
                    <view v-for="item in emojis" :key="item.emojis" class="emoji" @click="ChooseToLook(`${item.emoji}`)">
                        {{ item.emoji }}
                    </view>
                </scroll-view>
            </view>
        </view>
        <!-- 蒙板 -->
        <!-- <view class="shade" v-show="show" @click="close"></view> -->
    </view>
</template>

<script>

export default {
    props: {
        useful_flag: Boolean,
        cursor: Boolean,
        placeholder: String,
    },
    data() {
        return {
            show: false,
            cursor: false, //是否聚焦
            placeholder: '请输入内容~',
            value: '',
            active_Up: false,
            resStart: 0, //input光标位置
            //useful_flag: false, //控制动画效果
            useful_expression: 0, //2默认表情,1常用语
            T: true,
            emojis: [
                { emoji: '😃' },
                { emoji: '🤣' }, //真机才能看见效果
                { emoji: '😁' },
                { emoji: '😆' },
                { emoji: '😅' }
            ]
        }
    },
    watch: {
        useful_flag: {
            handler(newval, oldval) {
                this.show = newval
            }
        },
    },

    methods: {
        // 软键盘弹起，获取到焦点
        openinput(event) {
            // 解决软键盘遮挡input输入框
            this.active_Up = true //初始化
            if (event.detail.height != 0) {
                this.active_Up = true
            } else {
                this.active_Up = false
            }

            return false
        },
        // 失去焦点时获取当前光标的位置
        cursorss(event) {
            // this.active_Up = false
            // this.cursor = false; //取消聚焦
            this.resStart = event.target.cursor //保存光标位置

        },
        ChooseToLook(val) {
            // 返回获取从0到光标位置的字符串

            let str = this.value.substring(0, this.resStart)
            // 将表情插入指定位置
            this.value = this.value.replace(str, str + val)
            // 光标向后移动两位
            this.resStart += 2; //光标加2(表情是占两个位置)

        },
        change() {

            this.T = false
        },
        close() {

            // this.show = false;
            // this.useful_flag = false

        },
        // 打开常用语
        openUseful(e) {
            this.T = false
            if (this.useful_flag) {
                if (this.useful_expression === e) {
                    // 如果已经打开过常用语
                    this.useful_expression = e
                    // this.useful_flag = false //关闭
                } else {

                    this.useful_expression = e
                }
            } else {
                this.useful_expression = e
                //this.useful_flag = true //开启动画
            }
        },
        // 软键盘按下确定的事件监听
        startSearch() {

        },
        addComment() {

            this.$emit('addCommentWithEmoji', this.value);

        },
    }
}
</script>

<style lang="scss" scoped>
.consult {
    background-color: #fff;
    height: 100vh;

    .fotter {
        position: fixed;
        left: 0px;
        bottom: 0px;
        display: flex;

        width: 95%;
        justify-content: space-between;
        align-items: center;

        .fotter-content {
            color: #878787;
            line-height: 60rpx;
            margin-left: 20px;
            margin-right: 10px;
            width: 450rpx;
            height: 60rpx;
            background-color: #f4f4f4;
            padding-left: 10px;
            bottom: 0;
        }

    }

    .shade {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, .4);
        position: absolute;
        top: 0;
        left: 0;
    }

    .keyboard-active {
        bottom: 0 !important;
    }

    .keyboard {
        position: fixed;
        bottom: -488rpx;
        transition: bottom .3s;
        z-index: 999;
        width: 100%;

        .expression {
            height: 488rpx;
            background-color: #FAFAFA;

            .emojis_scroll {
                display: block;
                float: left;
                width: 100%;
                height: 400rpx;
                padding-left: 25rpx;
            }

            .emoji {
                display: block;
                float: left;
                width: 100rpx;
                height: 100rpx;
                font-size: 70rpx;
                text-align: center;
            }
        }

        .useful-content {
            height: 488rpx;
            background-color: #FAFAFA;
            overflow-y: auto;

            .useful-content-xiaoxi {
                height: 104rpx;
                text-align: center;
                line-height: 104rpx;
                border-bottom: 1rpx solid #F3F3F3;
            }
        }

        /deep/.keyboard-tup {
            display: flex;
            align-items: center;
            padding: 0 50rpx;

            .uni-input-input {
                background: #FFFFFF;
                padding: 0 20rpx;
            }

            .plus-circle {
                width: 56rpx;
                height: 56rpx;
                margin: 0 20rpx;
            }

            .useful {
                width: 64rpx;
                height: 64rpx;
                color: #333333;
            }

            input {
                width: 490rpx;
                height: 80rpx;
                box-sizing: border-box;
                border-radius: 8px;
            }

            .imface {
                margin-left: 10rpx;
                margin-right: 20rpx;
            }

            height: 115rpx;
            background-color: #F5F5F5;
        }

        .active-tup {
            height: 130rpx;

        }
    }
}</style>