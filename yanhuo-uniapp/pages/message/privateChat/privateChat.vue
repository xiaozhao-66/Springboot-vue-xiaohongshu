<template>
  <view class="chatInterface" @contextmenu.prevent="">
	  
	 
	<tui-navigation-bar backgroundColor="#fff" :isFixed="true" :isOpacity="false">
		<view class="nav">
			<tui-icon name="arrowleft" size="25" ></tui-icon>
			<view class="username" v-if="friend">{{ friend.username }}</view>
			<tui-icon name="more-fill" size="22"></tui-icon>
		</view>
	</tui-navigation-bar>
	 
    <view class="scroll-view">
      <image v-if="history.loading" class="history-loaded" src="../../../static/images/chat/loading.svg"/>
      <view v-else :class="history.allLoaded ? 'history-loaded':'load'" @click="loadHistoryMessage(false)">
        <view>{{ history.allLoaded ? '已经没有更多的历史消息' : '点击获取历史消息' }}</view>
      </view>

      <checkbox-group @change="selectMessages">
        <!--消息记录-->
        <view v-for="(message,index) in history.messages" :key="message.messageId">
          <!--时间显示，类似于微信，隔5分钟不发言，才显示时间-->
          <view class="time-lag">
            {{ renderMessageDate(message, index) }}
          </view>
          <view class="message-recalled" v-if="message.recalled">
            <view v-if="message.recaller.id === currentUser.id" class="message-recalled-self">
              <view>你撤回了一条消息</view>
              <span v-if="message.type === 'text' && Date.now()-message.timestamp< 60 * 1000 "
                    @click="editRecalledMessage(message.payload.text)">重新编辑</span>
            </view>
            <view v-else>{{ message.recaller.data.name }}撤回了一条消息</view>
          </view>
          <view class="message-item" v-else>
            <view class="message-item-checkbox">
              <checkbox v-show="messageSelector.visible && message.status !== 'sending'" :value="message.messageId"
                        :checked="messageSelector.messages.includes(message)"/>
            </view>
            <view class="message-item-content" :class="{'self' : message.senderId ===  currentUser.id}">
              <view class="avatar">
                <image :src="message.senderId === currentUser.id? currentUser.avatar : friend.avatar"  mode="aspectFill"   style="width: 80rpx; height: 80rpx; border-radius: 50%;"></image>
              </view>

              <view class="content" @click.right="showActionPopup(message)" @longpress="showActionPopup(message)">
                <view class="message-payload">
                  <b class="pending" v-if="message.status === 'sending'"></b>
                  <b class="send-fail" v-if="message.status === 'fail'"></b>
                  <view v-if="message.type === 'text'" v-html="renderTextMessage(message)"></view>
                  <image v-if="message.type === 'image'"
                     :data-url="message.payload.url"
                     :src="message.payload.thumbnail"
                     class="image-content"
                     mode="heightFix"
                     @click="showImageFullScreen"
                  ></image>
                  <view class="video-snapshot" v-if="message.type === 'video'" :data-url="message.payload.video.url"
                        @click="playVideo">
                    <image
                      :src="message.payload.thumbnail.url"
                      :style="{height: getImageHeight(message.payload.thumbnail.width,message.payload.thumbnail.height)+'rpx' }"
                      mode="heightFix"
                    ></image>
                    <view class="video-play-icon"></view>
                  </view>
                  <view class="file-content" v-if="message.type === 'file'">
                    <view class="file-info">
                      <span class="file-name">{{ message.payload.name }}</span>
                      <span class="file-size">{{ (message.payload.size / 1024).toFixed(2) }}KB</span>
                    </view>
                    <image class="file-img" src="../../../static/images/chat/file-icon.png"></image>
                  </view>
                  <view v-if="message.type ==='audio'" class="audio-content" @click="playAudio(message)">
                    <view class="audio-facade" :style="{width:Math.ceil(message.payload.duration)*7 + 50 + 'px'}">
                      <view
                        class="audio-facade-bg"
                        :class="{'play-icon':audioPlayer.playingMessage && audioPlayer.playingMessage.messageId === message.messageId}"
                      ></view>
                      <view>{{Math.ceil(message.payload.duration) || 1}}<span>"</span></view>
                    </view>
                  </view>
                </view>
                <view v-if="message.senderId === currentUser.id" :class="message.read ?'message-read':'message-unread'">
                  <view v-if="message.status === 'success'">{{ message.read ? '已读' : '未读' }}</view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </checkbox-group>
    </view>
    <view class="action-box" v-if="!videoPlayer.visible && !messageSelector.visible">
      <view class="action-top">
        <view @click="switchAudioKeyboard">
          <image class="more" v-if="audio.visible" src="../../../static/images/chat/jianpan.png"></image>
          <image class="more" v-else src="../../../static/images/chat/audio.png"></image>
        </view>
        <view v-if="audio.visible" class="record-input" @click="onRecordStart" @touchend.stop="onRecordEnd" @touchstart.stop="onRecordStart">
          {{ audio.recording ? '松开发送' : '按住录音' }}
        </view>
        <!-- GoEasyIM最大支持3k的文本消息，如需发送长文本，需调整输入框maxlength值 -->
        <input v-else v-model="text" @confirm="sendTextMessage" class="consult-input" maxlength="700" placeholder="发送消息" type="text" />
        <view @click="switchEmojiKeyboard">
          <image class="more" v-if="emoji.visible" src="../../../static/images/chat/jianpan.png"></image>
          <image class="more" v-else src="../../../static/images/chat/emoji.png"></image>
        </view>
        <view>
          <image @click="showOtherTypesMessagePanel()" class="more" src="../../../static/images/chat/more.png"/>
        </view>
        <view v-if="text" class="send-btn-box">
          <text class="btn" @click="sendTextMessage()">发送</text>
        </view>
      </view>
      <!--展示表情列表-->
      <view class="action-bottom action-bottom-emoji" v-if="emoji.visible">
        <image class="emoji-item" v-for="(emojiItem, emojiKey, index) in emoji.map" :key="index"
               :src="emoji.url + emojiItem" @click="chooseEmoji(emojiKey)"></image>
      </view>
      <!--其他类型消息面板-->
      <view v-if="otherTypesMessagePanelVisible" class="action-bottom">
        <view class="more-icon">
          <image @click="sendImageMessage()" class="operation-icon" src="../../../static/images/chat/picture.png"></image>
          <view class="operation-title">图片</view>
        </view>
        <view class="more-icon">
          <image @click="sendVideoMessage()" class="operation-icon" src="../../../static/images/chat/video.png"></image>
          <view class="operation-title">视频</view>
        </view>
      </view>
    </view>
    <view class="action-popup" @touchmove.stop.prevent v-if="actionPopup.visible">
      <view class="layer"></view>
      <view class="action-list">
        <view class="action-item" @click="deleteSingleMessage">删除</view>
        <view class="action-item" v-if="actionPopup.recallable" @click="recallMessage">撤回</view>
        <view class="action-item" @click="showCheckBox">多选</view>
        <view class="action-item" @click="hideActionPopup">取消</view>
      </view>
    </view>
    <view class="messageSelector-box" v-if="messageSelector.visible">
      <image class="messageSelector-btn" @click="deleteMultipleMessages" src="../../../static/images/chat/delete.png"></image>
    </view>
    <view class="record-loading" v-if="audio.recording"></view>
    <video v-if="videoPlayer.visible" :src="videoPlayer.url" id="videoPlayer"
           @fullscreenchange="onVideoFullScreenChange"></video>
  </view>
</template>

<script>
  import EmojiDecoder from '@/utils/EmojiDecoder.js';
  import {formatDate} from '@/utils/utils.js';
  import {getUserInfo} from '@/api/user.js'

  const IMAGE_MAX_WIDTH = 200;
  const IMAGE_MAX_HEIGHT = 150;
  const recorderManager = uni.getRecorderManager();
  export default {
    name: 'privateChat',
    data() {
      const emojiUrl = 'https://imgcache.qq.com/open/qcloud/tim/assets/emoji/';
      const emojiMap = {
        '[么么哒]': 'emoji_3@2x.png',
        '[乒乓]': 'emoji_4@2x.png',
        '[便便]': 'emoji_5@2x.png',
        '[信封]': 'emoji_6@2x.png',
        '[偷笑]': 'emoji_7@2x.png',
        '[傲慢]': 'emoji_8@2x.png'
      };
      return {
        //聊天文本框
		
        text: '',
        friend: null,
        to: {},// 作为createMessage的参数
        currentUser: null,

        //定义表情列表
        emoji: {
          url: emojiUrl,
          map: emojiMap,
          visible: false,
          decoder: new EmojiDecoder(emojiUrl, emojiMap),
        },
        //是否展示‘其他消息类型面板’
        otherTypesMessagePanelVisible: false,
       
        history: {
          messages: [],
          allLoaded: false,
          loading: false
        },
        audio: {
          startTime: null,
          //语音录音中
          recording: false,
          //录音按钮展示
          visible: false
        },
        audioPlayer: {
          innerAudioContext: null,
          playingMessage: null,
        },
        videoPlayer: {
          visible: false,
          url: '',
          context: null
        },
        // 展示消息删除弹出框
        actionPopup: {
          visible: false,
          message: null,
          recallable: false,
        },
        // 消息选择
        messageSelector: {
          visible: false,
          messages: []
        }
      }
    },
    onLoad(options) {
      //聊天对象
      let id = options.to;
	  this.getUserInfo(id);
    },
    onShow() {
      this.otherTypesMessagePanelVisible = false;
      this.emoji.visible = false;
    },
    onReady() {
      // this.loadHistoryMessage(true);
      // this.videoPlayer.context = uni.createVideoContext('videoPlayer', this);
    },
    onPullDownRefresh(e) {
      this.loadHistoryMessage(false);
    },
    onUnload() {
      //退出聊天页面之前，清空监听器
      this.goEasy.im.off(this.GoEasy.IM_EVENT.PRIVATE_MESSAGE_RECEIVED, this.onMessageReceived);
      this.goEasy.im.off(this.GoEasy.IM_EVENT.MESSAGE_DELETED, this.onMessageDeleted);
    },
    methods: {
		
	  getUserInfo(id){
		  let params = {
			  uid: id
		  }
		  getUserInfo(params).then(res=>{
			  this.friend = res.data			  
			  this.currentUser = getApp().globalData.currentUser;
			  
			  this.to = {
			    id: this.friend.id,
			    type: this.GoEasy.IM_SCENE.PRIVATE,
			    data: {
			      name: this.friend.username,
			      avatar: this.friend.avatar
			    }
			  };
			  
			  this.initialGoEasyListeners();
			  // 语音播放器
			  this.initialAudioPlayer();
			  // 录音监听器
			  this.initRecorderListeners();
			  
			  this.loadHistoryMessage(true);
			  this.videoPlayer.context = uni.createVideoContext('videoPlayer', this);
			  uni.setNavigationBarTitle({ title: this.friend.name });
		  })
		  
	  },
      //渲染文本消息，如果包含表情，替换为图片
      //todo:本不需要该方法，可以在标签里完成，但小程序有兼容性问题，被迫这样实现
      renderTextMessage(message) {
        return '<span class="text-content">' + this.emoji.decoder.decode(message.payload.text) + '</span>'
      },
      //像微信那样显示时间，如果有几分钟没发消息了，才显示时间
      //todo:本不需要该方法，可以在标签里完成，但小程序有兼容性问题，被迫这样实现
      renderMessageDate(message, index) {
        if (index === 0) {
          return formatDate(message.timestamp)
        } else {
          if (message.timestamp - this.history.messages[index - 1].timestamp > 5 * 60 * 1000) {
            return formatDate(message.timestamp)
          }
        }
        return '';
      },
      initialGoEasyListeners() {
        // 监听私聊消息
        this.goEasy.im.on(this.GoEasy.IM_EVENT.PRIVATE_MESSAGE_RECEIVED, this.onMessageReceived);
        //监听消息删除
        this.goEasy.im.on(this.GoEasy.IM_EVENT.MESSAGE_DELETED, this.onMessageDeleted);
      },
      onMessageReceived (message) {
        let senderId = message.senderId;
        let receiverId = message.receiverId;
        let friendId = this.currentUser.id === senderId ? receiverId : senderId;
        if (friendId === this.friend.id) {
          this.history.messages.push(message);
          //聊天时，收到消息标记为已读
          this.markPrivateMessageAsRead();
          //收到新消息，是滚动到最底部
          this.scrollToBottom();
        }
      },
      onMessageDeleted (deletedMessages) {
        deletedMessages.forEach(message => {
          let senderId = message.senderId;
          let receiverId = message.receiverId;
          let friendId = this.currentUser.id === senderId ? receiverId : senderId;
          if (friendId === this.friend.id) {
            let index = this.history.messages.indexOf(message);
            if (index > -1) {
              this.history.messages.splice(index, 1);
            }
          }
        });
      },
      initialAudioPlayer () {
        this.audioPlayer.innerAudioContext = uni.createInnerAudioContext();
        this.audioPlayer.innerAudioContext.onEnded(() => {
          this.audioPlayer.playingMessage = null;
        });
        this.audioPlayer.innerAudioContext.onStop(() => {
          this.audioPlayer.playingMessage = null;
        });
      },
      initRecorderListeners() {
        // 监听录音开始
        recorderManager.onStart(() => {
          this.audio.recording = true;
          this.audio.startTime = Date.now();
        });
        //录音结束后，发送
        recorderManager.onStop((res) => {
          let endTime = Date.now();
          this.audio.recording = false;
          let duration = endTime - this.audio.startTime;
          if (duration < 1000) {
            uni.showToast({
              icon: 'error',
              title: '录音时间太短',
              duration: 500
            });
            return;
          }
          res.duration = duration;
          this.goEasy.im.createAudioMessage({
            to: this.to,
            file: res,
            notification: {
              title: this.currentUser.username + '发来一段语音',
              body: '[语音消息]',		// 字段最长 50 字符
              sound: 'message',
              badge: '+1'
            },
            onProgress: function (progress) {
              console.log(progress)
            },
            onSuccess: (message) => {
              this.sendMessage(message);
            },
            onFailed: (e) => {
              console.log('error :', e);
            }
          });
        });
        // 监听录音报错
        recorderManager.onError((res) => {
          this.audio.recording = false;
          recorderManager.stop();
          uni.showToast({
            icon: 'none',
            title: '录音失败,请检查麦克风权限',
            duration: 1000
          });
        })
      },
      /**
       * 核心就是设置高度，产生明确占位
       *
       * 小  (宽度和高度都小于预设尺寸)
       *    设高=原始高度
       * 宽 (宽度>高度)
       *    高度= 根据宽度等比缩放
       * 窄  (宽度<高度)或方(宽度=高度)
       *    设高=MAX height
       *
       * @param width,height
       * @returns number
       */
      getImageHeight(width, height) {
        if (width < IMAGE_MAX_WIDTH && height < IMAGE_MAX_HEIGHT) {
          return height * 2;
        } else if (width > height) {
          return (IMAGE_MAX_WIDTH / width * height) * 2;
        } else if (width === height || width < height) {
          return IMAGE_MAX_HEIGHT * 2;
        }
      },
      sendMessage(message) {
        this.history.messages.push(message);
        this.scrollToBottom();
        this.goEasy.im.sendMessage({
          message: message,
          onSuccess: function (message) {
			  
            console.log('发送成功.', message);
          },
          onFailed: function (error) {
            if (error.code === 507) {
              console.log('发送语音/图片/视频/文件失败，没有配置OSS存储，详情参考：https://docs.goeasy.io/2.x/im/message/media/alioss');
            } else {
              console.log('发送失败:', error);
            }
          }
        });
      },
      sendTextMessage() {
        if (this.text.trim() !== '') {
          let body = this.text;
          if (this.text.length >= 50) {
            body = this.text.substring(0, 30) + '...';
          }
          this.goEasy.im.createTextMessage({
            text: this.text,
            to: this.to,
            notification: {
              title: this.currentUser.username + '发来一段文字',
              body: body,
              sound: 'message',
              badge: '+1'
            },
            onSuccess: (message) => {
              this.sendMessage(message);
            },
            onFailed: (e) => {
              console.log('error :', e);
            }
          });
        }
        this.text = '';
      },
      sendVideoMessage() {
        uni.chooseVideo({
          success: (res) => {
            this.goEasy.im.createVideoMessage({
              to: this.to,
              file: res,
              notification: {
                title: this.currentUser.username + '发来一个视频',
                body: '[视频消息]',		// 字段最长 50 字符
                sound: 'message',
                badge: '+1'
              },
              onProgress: function (progress) {
                console.log(progress)
              },
              onSuccess: (message) => {
                this.otherTypesMessagePanelVisible = false;
                this.sendMessage(message);
              },
              onFailed: (e) => {
                console.log('error :', e);
              }
            });
          }
        })
      },
      sendImageMessage() {
        uni.chooseImage({
          count: 9,
          success: (res) => {
            res.tempFiles.forEach(file => {
              this.goEasy.im.createImageMessage({
                to: this.to,
                file: file,
                notification: {
                  title: this.currentUser.username + '发来一张图片',
                  body: '[图片消息]',		// 字段最长 50 字符
                  sound: 'message',
                  badge: '+1'
                },
                onProgress: function (progress) {
                  console.log(progress)
                },
                onSuccess: (message) => {
                  this.otherTypesMessagePanelVisible = false;
                  this.sendMessage(message);
                },
                onFailed: (e) => {
                  console.log('error :', e);
                }
              });
            })
          }
        });
      },
      
      showActionPopup(message) {
        const MAX_RECALLABLE_TIME = 3 * 60 * 1000; //3分钟以内的消息才可以撤回
        this.messageSelector.messages = [message];
        if ((Date.now() - message.timestamp) < MAX_RECALLABLE_TIME && message.senderId === this.currentUser.id && message.status === 'success') {
          this.actionPopup.recallable = true;
        } else {
          this.actionPopup.recallable = false;
        }
        this.actionPopup.visible = true;
      },
      hideActionPopup () {
        this.actionPopup.visible = false;
        this.actionPopup.message = null;
      },
      deleteSingleMessage() {
        uni.showModal({
          content: '确认删除？',
          success: (res) => {
            this.actionPopup.visible = false;
            if (res.confirm) {
              this.deleteMessage();
            }
          },
        })
      },
      deleteMultipleMessages() {
        if (this.messageSelector.messages.length > 0) {
          uni.showModal({
            content: '确认删除？',
            success: (res) => {
              this.messageSelector.visible = false;
              if (res.confirm) {
                this.deleteMessage();
              }
            },
          })
        }
      },
      deleteMessage() {
        this.goEasy.im.deleteMessage({
          messages: this.messageSelector.messages,
          onSuccess: (result) => {
            this.messageSelector.messages.forEach(message => {
              let index = this.history.messages.indexOf(message);
              if (index > -1) {
                this.history.messages.splice(index, 1);
              }
            });
            this.messageSelector.messages = [];
          },
          onFailed: (error) => {
            console.log('error:', error);
          }
        });
      },
      recallMessage() {
        this.actionPopup.visible = false;
        this.goEasy.im.recallMessage({
          messages: this.messageSelector.messages,
          onSuccess: () => {
            console.log('撤回成功');
          },
          onFailed: (error) => {
            console.log('撤回失败,error:', error);
          }
        });
      },
      editRecalledMessage(text) {
        if (this.audio.visible) {
          this.audio.visible = false;
        }
        this.text = text;
      },
      showCheckBox() {
        this.messageSelector.messages = [];
        this.messageSelector.visible = true;
        this.actionPopup.visible = false;
      },
      selectMessages(e) {
        const selectedMessageIds = e.detail.value;
        let selectedMessages = [];
        this.history.messages.forEach(message => {
          if (selectedMessageIds.includes(message.messageId)) {
            selectedMessages.push(message);
          }
        })
        this.messageSelector.messages = selectedMessages;
      },
      loadHistoryMessage(scrollToBottom) {//历史消息
        this.history.loading = true;
        let lastMessageTimeStamp = null;
        let lastMessage = this.history.messages[0];
        if (lastMessage) {
          lastMessageTimeStamp = lastMessage.timestamp;
        }
        this.goEasy.im.history({
          userId: this.friend.id,
          lastTimestamp: lastMessageTimeStamp,
          limit: 10,
          onSuccess: (result) => {
            uni.stopPullDownRefresh();
            this.history.loading = false;
            let messages = result.content;
            if (messages.length === 0) {
              this.history.allLoaded = true;
            } else {
              if (lastMessageTimeStamp) {
                this.history.messages = messages.concat(this.history.messages);
              } else {
                this.history.messages = messages;
              }
              if (messages.length < 10) {
                this.history.allLoaded = true;
              }
              if (scrollToBottom) {
                this.scrollToBottom();
                //收到的消息设置为已读
                this.markPrivateMessageAsRead();
              }
            }
          },
          onFailed: (error) => {
            //获取失败
            console.log('获取历史消息失败:', error);
            uni.stopPullDownRefresh();
            this.history.loading = false;
          }
        });
      },
      //语音录制按钮和键盘输入的切换
      switchAudioKeyboard() {
        this.audio.visible = !this.audio.visible;
        if (uni.authorize) {
          uni.authorize({
            scope: 'scope.record',
            fail: () => {
              uni.showModal({
                title: '获取录音权限失败',
                content: '请先授权才能发送语音消息！'
              });
            }
          });
        }
      },
      onRecordStart() {
        try {
          // 更多配置参考uniapp：https://uniapp.dcloud.net.cn/api/media/record-manager.html#getrecordermanager
          recorderManager.start({
            duration: 600000 // 指定录音的时长,单位 ms
          });
        } catch (e) {
          uni.showModal({
            title: '录音错误',
            content: '请在app和小程序端体验录音，Uni官方明确H5不支持getRecorderManager, 详情查看Uni官方文档'
          });
        }
      },
      onRecordEnd() {
        try {
          recorderManager.stop();
        } catch (e) {
          console.log(e);
        }
      },
      showImageFullScreen(e) {
        let imagesUrl = [e.currentTarget.dataset.url];
        uni.previewImage({
          urls: imagesUrl
        });
      },
      playVideo(e) {
        this.videoPlayer.visible = true;
        this.videoPlayer.url = e.currentTarget.dataset.url;
        this.$nextTick(() => {
          this.videoPlayer.context.requestFullScreen({
            direction: 0
          });
          this.videoPlayer.context.play();
        });
      },
      playAudio (audioMessage) {
        let playingMessage = this.audioPlayer.playingMessage;

        if (playingMessage) {
          this.audioPlayer.innerAudioContext.stop();
          // 如果点击的消息正在播放，就认为是停止播放操作
          if (playingMessage === audioMessage) {
            return;
          }
        }
        this.audioPlayer.playingMessage = audioMessage;
        this.audioPlayer.innerAudioContext.src = audioMessage.payload.url;
        this.audioPlayer.innerAudioContext.play();
      },
      onVideoFullScreenChange(e) {
        //当退出全屏播放时，隐藏播放器
        if (this.videoPlayer.visible && !e.detail.fullScreen) {
          this.videoPlayer.visible = false;
          this.videoPlayer.context.stop();
        }
      },
      messageInputFocusin() {
        this.otherTypesMessagePanelVisible = false;
        this.emoji.visible = false;
      },
      switchEmojiKeyboard() {
        this.emoji.visible = !this.emoji.visible;
        this.otherTypesMessagePanelVisible = false;
      },
      showOtherTypesMessagePanel() {
        this.otherTypesMessagePanelVisible = !this.otherTypesMessagePanelVisible;
        this.emoji.visible = false;
      },
      chooseEmoji(emojiKey) {
        this.text += emojiKey;
      },
      showOrderMessageList() {
        this.orderList.orders = restApi.getOrderList();
        this.orderList.visible = true;
      },
      hideOrderMessageList() {
        this.orderList.visible = false;
      },
      scrollToBottom() {
        this.$nextTick(() => {
          uni.pageScrollTo({
            scrollTop: 2000000,
            duration: 0
          });
        });
      },
      markPrivateMessageAsRead() {
        this.goEasy.im.markMessageAsRead({
          id: this.to.id,
          type: this.to.type,
          onSuccess: function () {
            console.log('标记私聊已读成功');
          },
          onFailed: function (error) {
            console.log("标记私聊已读失败", error);
          }
        });
      }
    }
  }
</script>

<style scoped>
	@import url(./privateChat.css);
</style>
