import { ZIMMessageDirection, } from '../ZIMDefines';
import { ZIMLogger, ZIMLogTag, ZIMLogAction } from './ZIMLogger';
import { ZIMParamValid } from './ZIMParamValid';
import { ZIMManager } from './ZIMManager';
const ZIMNativeModule = uni.requireNativePlugin('zego-ZIMUniPlugin_ZIMUniEngine');
const ZIMEvent = uni.requireNativePlugin('globalEvent');
const Prefix = ZIMNativeModule.prefix();
export class ZIMEngine {
    handle;
    appID;
    appSign;
    logger;
    paramValid;
    loginUserID;
    uploadingMap;
    downloadingMap;
    messageAttachedMap;
    eventNameList;
    static _callMethod(method, args = {}) {
        return new Promise((resolve, reject) => {
            ZIMNativeModule.callMethod({ method, args }, (res) => {
                // console.log(`[ZIM][API][I] ${method}: ${JSON.stringify(res)}`);
                resolve(res);
            }, (err) => {
                // console.log(`[ZIM][API][E] ${method}: ${JSON.stringify(err)}`);
                reject(err);
            });
        });
    }
    constructor(handle, appID, appSign) {
        this.handle = handle;
        this.appID = appID;
        this.appSign = appSign;
        this.loginUserID = '';
        this.logger = new ZIMLogger();
        this.paramValid = new ZIMParamValid(this.logger);
        this.uploadingMap = new Map();
        this.downloadingMap = new Map();
        this.messageAttachedMap = new Map();
        this.eventNameList = [
            'connectionStateChanged',
            'error',
            'tokenWillExpire',
            'conversationChanged',
            'conversationTotalUnreadMessageCountUpdated',
            'receivePeerMessage',
            'receiveGroupMessage',
            'receiveRoomMessage',
            'roomStateChanged',
            'roomMemberJoined',
            'roomMemberLeft',
            'roomAttributesUpdated',
            'roomAttributesBatchUpdated',
            'roomMemberAttributesUpdated',
            'groupStateChanged',
            'groupNameUpdated',
            'groupAvatarUrlUpdated',
            'groupNoticeUpdated',
            'groupAttributesUpdated',
            'groupMemberStateChanged',
            'groupMemberInfoUpdated',
            'callInvitationReceived',
            'callInvitationCancelled',
            'callInvitationTimeout',
            'callInvitationAccepted',
            'callInvitationRejected',
            'callInviteesAnsweredTimeout',
            'conversationMessageReceiptChanged',
            'messageReceiptChanged',
            'messageRevokeReceived',
            'messageSentStatusChanged',
        ];
    }
    create() {
        ZIMEngine._callMethod("setAdvancedConfig", { handle: this.handle.toString(), key: "zim_cross_platform", value: "uni-app" });
        return ZIMEngine._callMethod("createEngine", { handle: this.handle.toString(), appID: this.appID, appSign: this.appSign });
    }
    destroy() {
        for (let event of this.eventNameList) {
            ZIMEvent.removeEventListener(Prefix + event);
        }
        return ZIMEngine._callMethod("destroyEngine", { handle: this.handle.toString() });
    }
    uploadLog() {
        return ZIMEngine._callMethod("uploadLog", { handle: this.handle.toString() });
    }
    on(type, listener) {
        if (!listener || typeof listener != 'function')
            throw new Error('listener must be a function.');
        const native_listener = (res) => {
            const { handle, data } = res;
            const engine = ZIMManager.engineMap.get(handle);
            this.logger.warn([], 'JSAPI.emit.' + type, data);
            // @ts-ignore
            listener(engine, ...data);
        };
        let map = ZIMManager.listeners.get(type);
        if (map === undefined) {
            map = new Map();
            ZIMManager.listeners.set(type, map);
        }
        map.set(listener, native_listener);
        ZIMEvent.addEventListener(Prefix + type, native_listener);
        this.logger.warn([], 'JSAPI.on.' + type);
    }
    off(type) {
        ZIMEvent.removeEventListener(Prefix + type);
        ZIMManager.listeners.delete(type);
    }
    login(userInfo, token) {
        const error = this.paramValid.login(userInfo);
        if (error)
            return Promise.reject(error);
        this.loginUserID = userInfo.userID;
        return ZIMEngine._callMethod("login", { handle: this.handle.toString(), userInfo, token });
    }
    logout() {
        return ZIMEngine._callMethod("logout", { handle: this.handle.toString() });
    }
    renewToken(token) {
        return ZIMEngine._callMethod("renewToken", { handle: this.handle.toString(), token });
    }
    updateUserName(userName) {
        const error = this.paramValid.validName('userName', userName, ZIMLogTag.User, ZIMLogAction.UpdateUserName);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateUserName", { handle: this.handle.toString(), userName });
    }
    updateUserAvatarUrl(userAvatarUrl) {
        const error = this.paramValid.validName('userAvatarUrl', userAvatarUrl, ZIMLogTag.User, ZIMLogAction.UpdateUserAvatarUrl);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateUserAvatarUrl", { handle: this.handle.toString(), userAvatarUrl });
    }
    updateUserExtendedData(extendedData) {
        const error = this.paramValid.validName('extendedData', extendedData, ZIMLogTag.User, ZIMLogAction.UpdateUserExtendedData);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateUserExtendedData", { handle: this.handle.toString(), extendedData });
    }
    queryUsersInfo(userIDs, config) {
        const error = this.paramValid.queryUsersInfo(userIDs);
        if (error)
            return Promise.reject(error);
        config = Object.assign({ isQueryFromServer: false }, config);
        return ZIMEngine._callMethod("queryUsersInfo", { handle: this.handle.toString(), userIDs, config });
    }
    // MARK: - Conversation
    queryConversationList(config) {
        const error = this.paramValid.queryConversationList(config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryConversationList", { handle: this.handle.toString(), config });
    }
    deleteConversation(conversationID, conversationType, config) {
        const error = this.paramValid.validConvIDAndType(conversationID, conversationType, ZIMLogAction.DeleteConversation);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("deleteConversation", { handle: this.handle.toString(), conversationID, conversationType, config });
    }
    setConversationNotificationStatus(status, conversationID, conversationType) {
        const error = this.paramValid.setConversationNotificationStatus(status, conversationID, conversationType);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setConversationNotificationStatus", { handle: this.handle.toString(), status, conversationID, conversationType });
    }
    clearConversationUnreadMessageCount(conversationID, conversationType) {
        const error = this.paramValid.validConvIDAndType(conversationID, conversationType, ZIMLogAction.ClearConversationUnreadMessageCount);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("clearConversationUnreadMessageCount", { handle: this.handle.toString(), conversationID, conversationType });
    }
    sendMessage(message, toConversationID, conversationType, config, notification) {
        const error = this.paramValid.sendMessage(message, toConversationID, conversationType, config);
        if (error)
            return Promise.reject(error);
        const messageAttachedCallBack = notification?.['onMessageAttached'] || Function.prototype;
        const native_messageAttached_listener = (res) => {
            const { messageAttachedHandle, data } = res;
            const messageAttached = this.messageAttachedMap.get(messageAttachedHandle);
            // @ts-ignore
            messageAttached?.(...data);
        };
        const type = 'messageAttached';
        ZIMEvent.addEventListener(Prefix + type, native_messageAttached_listener);
        const messageAttachedHandle = Symbol(Math.round(Math.random() * 10000));
        this.messageAttachedMap.set(messageAttachedHandle.toString(), messageAttachedCallBack);
        const methodName = 'sendMessage';
        return new Promise((resolve, reject) => {
            ZIMNativeModule.callMethod({
                method: methodName,
                args: {
                    handle: this.handle.toString(),
                    message,
                    conversationID: toConversationID,
                    conversationType,
                    config,
                    messageAttachedHandle: messageAttachedHandle.toString(),
                }
            }, (res) => {
                ZIMEvent.removeEventListener(Prefix + type, native_messageAttached_listener);
                this.messageAttachedMap.delete(messageAttachedHandle.toString());
                resolve(res);
            }, (err) => {
                ZIMEvent.removeEventListener(Prefix + type, native_messageAttached_listener);
                this.messageAttachedMap.delete(messageAttachedHandle.toString());
                reject(err);
            });
        });
    }
    sendMediaMessage(message, toConversationID, conversationType, config, notification) {
        const error = this.paramValid.sendMediaMessage(message, toConversationID, conversationType, config);
        if (error)
            return Promise.reject(error);
        const progressCallBack = notification?.["onMediaUploadingProgress"] || Function.prototype;
        const messageAttachedCallBack = notification?.['onMessageAttached'] || Function.prototype;
        const native_progress_listener = (res) => {
            const { progressHandle, data } = res;
            const progress = this.uploadingMap.get(progressHandle);
            // @ts-ignore
            progress?.(...data);
        };
        const native_messageAttached_listener = (res) => {
            const { messageAttachedHandle, data } = res;
            const messageAttached = this.messageAttachedMap.get(messageAttachedHandle);
            // @ts-ignore
            messageAttached?.(...data);
        };
        const progressType = 'mediaUploadingProgress';
        const messageAttchedType = 'messageAttched';
        ZIMEvent.addEventListener(Prefix + progressType, native_progress_listener);
        ZIMEvent.addEventListener(Prefix + messageAttchedType, native_messageAttached_listener);
        const progressHandle = Symbol(Math.round(Math.random() * 10000));
        const messageAttachedHandle = Symbol(Math.round(Math.random() * 10000));
        this.uploadingMap.set(progressHandle.toString(), progressCallBack);
        this.messageAttachedMap.set(messageAttachedHandle.toString(), messageAttachedCallBack);
        const methodName = 'sendMediaMessage';
        return new Promise((resolve, reject) => {
            const removeZIMEventListener = () => {
                ZIMEvent.removeEventListener(Prefix + progressType, native_progress_listener);
                ZIMEvent.removeEventListener(Prefix + messageAttchedType, native_messageAttached_listener);
                this.uploadingMap.delete(progressHandle.toString());
                this.uploadingMap.delete(messageAttachedHandle.toString());
            };
            ZIMNativeModule.callMethod({ method: methodName, args: { handle: this.handle.toString(), message, toConversationID, conversationType, config, progressHandle: progressHandle.toString(), messageAttachedHandle: messageAttachedHandle.toString() } }, (res) => {
                removeZIMEventListener();
                // console.log(`[ZIM][API][I] ${methodName}: ${JSON.stringify(res)}`);
                resolve(res);
            }, (err) => {
                removeZIMEventListener();
                // console.log(`[ZIM][API][E] ${methodName}: ${JSON.stringify(err)}`);
                reject(err);
            });
        });
    }
    deleteMessages(messageList, conversationID, conversationType, config) {
        config = Object.assign({ isAlsoDeleteServerMessage: true }, config);
        const error = this.paramValid.deleteMessages(messageList, conversationID, conversationType, config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("deleteMessages", { handle: this.handle.toString(), messageList, conversationID, conversationType, config });
    }
    deleteAllMessage(conversationID, conversationType, config) {
        const error = this.paramValid.validConvIDAndType(conversationID, conversationType, ZIMLogAction.DeleteAllMessage);
        if (error)
            return Promise.reject(error);
        config = Object.assign({ isAlsoDeleteServerMessage: true }, config);
        return ZIMEngine._callMethod("deleteAllMessage", { handle: this.handle.toString(), conversationID, conversationType, config });
    }
    insertMessageToLocalDB(message, conversationID, conversationType, senderUserID) {
        const error = this.paramValid.insertMessageToLocalDB(message, conversationID, conversationType, senderUserID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("insertMessageToLocalDB", { handle: this.handle.toString(), message, conversationID, conversationType, senderUserID });
    }
    sendConversationMessageReceiptRead(conversationID, conversationType) {
        const error = this.paramValid.sendConversationMessageReceiptRead(conversationID, conversationType);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("sendConversationMessageReceiptRead", { handle: this.handle.toString(), conversationID, conversationType });
    }
    sendMessageReceiptsRead(messageList, conversationID, conversationType) {
        const error = this.paramValid.sendMessageReceiptsRead(messageList, conversationID, conversationType);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("sendMessageReceiptsRead", { handle: this.handle.toString(), messageList, conversationID, conversationType });
    }
    queryMessageReceiptsInfo(messageList, conversationID, conversationType) {
        const error = this.paramValid.queryMessageReceiptsInfo(messageList, conversationID, conversationType);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryMessageReceiptsInfo", { handle: this.handle.toString(), messageList, conversationID, conversationType });
    }
    queryGroupMessageReceiptMemberList(message, groupID, config, read) {
        const error = this.paramValid.queryGroupMessageReceiptMemberList(message, groupID, config, read, this.loginUserID);
        if (error)
            return Promise.reject(error);
        if (config.count < 1 || message.direction == ZIMMessageDirection.Receive) {
            return Promise.resolve({ groupID, nextFlag: 0, userList: [] });
        }
        return ZIMEngine._callMethod("queryGroupMessageReceiptMemberList", { handle: this.handle.toString(), message, groupID, config, read });
    }
    revokeMessage(message, config) {
        const error = this.paramValid.revokeMessage(message, this.loginUserID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("revokeMessage", { handle: this.handle.toString(), message, config });
    }
    queryHistoryMessage(conversationID, conversationType, config) {
        const error = this.paramValid.queryHistoryMessage(conversationID, conversationType, config);
        if (error)
            return Promise.reject(error);
        if (config.count < 1)
            return Promise.resolve({ conversationID, conversationType, messageList: [] });
        return ZIMEngine._callMethod("queryHistoryMessage", { handle: this.handle.toString(), conversationID, conversationType, config });
    }
    downloadMediaFile(message, fileType, progress) {
        const error = this.paramValid.downloadMediaFile(message, fileType);
        if (error)
            return Promise.reject(error);
        const native_progress_listener = (res) => {
            const { progressHandle, data } = res;
            const progress = this.downloadingMap.get(progressHandle);
            // @ts-ignore
            progress?.(...data);
        };
        const type = 'mediaDownloadingProgress';
        ZIMEvent.addEventListener(Prefix + type, native_progress_listener);
        const progressHandle = Symbol(Math.round(Math.random() * 10000));
        this.downloadingMap.set(progressHandle.toString(), progress);
        const methodName = 'downloadMediaFile';
        return new Promise((resolve, reject) => {
            ZIMNativeModule.callMethod({ method: methodName, args: { handle: this.handle.toString(), message, fileType, progressHandle: progressHandle.toString() } }, (res) => {
                ZIMEvent.removeEventListener(Prefix + type, native_progress_listener);
                this.downloadingMap.delete(progressHandle.toString());
                // console.log(`[ZIM][API][I] ${methodName}: ${JSON.stringify(res)}`);
                resolve(res);
            }, (err) => {
                ZIMEvent.removeEventListener(Prefix + type, native_progress_listener);
                this.downloadingMap.delete(progressHandle.toString());
                // console.log(`[ZIM][API][E] ${methodName}: ${JSON.stringify(err)}`);
                reject(err);
            });
        });
    }
    // MARK: - Room
    createRoom(roomInfo, config) {
        const error = this.paramValid.createRoom(roomInfo, config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("createRoom", { handle: this.handle.toString(), roomInfo, config });
    }
    enterRoom(roomInfo, config) {
        const error = this.paramValid.createRoom(roomInfo, config, true);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("enterRoom", { handle: this.handle.toString(), roomInfo, config });
    }
    joinRoom(roomID) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.JoinRoom);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("joinRoom", { handle: this.handle.toString(), roomID });
    }
    leaveRoom(roomID) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.LeaveRoom);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("leaveRoom", { handle: this.handle.toString(), roomID });
    }
    queryRoomMemberList(roomID, config) {
        const error = this.paramValid.queryRoomMemberList(roomID, config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryRoomMemberList", { handle: this.handle.toString(), roomID, config });
    }
    queryRoomOnlineMemberCount(roomID) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.QueryRoomOnlineMemberCount);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryRoomOnlineMemberCount", { handle: this.handle.toString(), roomID });
    }
    queryRoomAllAttributes(roomID) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.QueryRoomAllAttributes);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryRoomAllAttributes", { handle: this.handle.toString(), roomID });
    }
    setRoomAttributes(roomAttributes, roomID, config) {
        const error = this.paramValid.setRoomAttributes(roomAttributes, roomID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setRoomAttributes", { handle: this.handle.toString(), roomAttributes, roomID, config });
    }
    deleteRoomAttributes(keys, roomID, config) {
        const error = this.paramValid.deleteRoomAttributes(keys, roomID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("deleteRoomAttributes", { handle: this.handle.toString(), keys, roomID, config });
    }
    beginRoomAttributesBatchOperation(roomID, config) {
        return ZIMEngine._callMethod("beginRoomAttributesBatchOperation", { handle: this.handle.toString(), roomID, config });
    }
    endRoomAttributesBatchOperation(roomID) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.EndRoomAttributesBatchOperation);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("endRoomAttributesBatchOperation", { handle: this.handle.toString(), roomID });
    }
    setRoomMembersAttributes(attributes, userIDs, roomID, config) {
        const error = this.paramValid.setRoomMembersAttributes(attributes, userIDs, roomID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setRoomMembersAttributes", { handle: this.handle.toString(), attributes, userIDs, roomID, config });
    }
    queryRoomMembersAttributes(userIDs, roomID) {
        const error = this.paramValid.queryRoomMembersAttributes(userIDs, roomID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryRoomMembersAttributes", { handle: this.handle.toString(), userIDs, roomID });
    }
    queryRoomMemberAttributesList(roomID, config) {
        const error = this.paramValid.validID('roomID', roomID, ZIMLogTag.Room, ZIMLogAction.QueryRoomMemberAttributesList);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryRoomMemberAttributesList", { handle: this.handle.toString(), roomID, config });
    }
    // MARK: - Group
    createGroup(groupInfo, userIDs, config) {
        groupInfo = Object.assign({ groupID: '', groupName: '', groupAvatarUrl: '' }, groupInfo);
        config = Object.assign({ groupAttributes: {}, groupNotice: '' }, config);
        const error = this.paramValid.createGroup(groupInfo, userIDs, config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("createGroup", { handle: this.handle.toString(), groupInfo, userIDs, config });
    }
    joinGroup(groupID) {
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.JoinGroup);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("joinGroup", { handle: this.handle.toString(), groupID });
    }
    leaveGroup(groupID) {
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.LeaveGroup);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("leaveGroup", { handle: this.handle.toString(), groupID });
    }
    dismissGroup(groupID) {
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.DismissGroup);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("dismissGroup", { handle: this.handle.toString(), groupID });
    }
    queryGroupList() {
        return ZIMEngine._callMethod("queryGroupList", { handle: this.handle.toString() });
    }
    updateGroupNotice(groupNotice, groupID) {
        const error = this.paramValid.validIDAndName('groupID', groupID, 'groupNotice', groupNotice, ZIMLogTag.Group, ZIMLogAction.UpdateGroupNotice);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateGroupNotice", { handle: this.handle.toString(), groupNotice, groupID });
    }
    updateGroupName(groupName, groupID) {
        const error = this.paramValid.validIDAndName('groupID', groupID, 'groupName', groupName, ZIMLogTag.Group, ZIMLogAction.UpdateGroupName);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateGroupName", { handle: this.handle.toString(), groupName, groupID });
    }
    updateGroupAvatarUrl(groupAvatarUrl, groupID) {
        const error = this.paramValid.validIDAndName('groupID', groupID, 'groupAvatarUrl', groupAvatarUrl, ZIMLogTag.Group, ZIMLogAction.UpdateGroupAvatarUrl);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("updateGroupAvatarUrl", { handle: this.handle.toString(), groupAvatarUrl, groupID });
    }
    queryGroupInfo(groupID) {
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.QueryGroupInfo);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryGroupInfo", { handle: this.handle.toString(), groupID });
    }
    setGroupAttributes(groupAttributes, groupID) {
        const error = this.paramValid.setGroupAttributes(groupAttributes, groupID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setGroupAttributes", { handle: this.handle.toString(), groupAttributes, groupID });
    }
    deleteGroupAttributes(keys, groupID) {
        const error = this.paramValid.deleteGroupAttributes(keys, groupID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("deleteGroupAttributes", { handle: this.handle.toString(), keys, groupID });
    }
    queryGroupAttributes(keys, groupID) {
        const error = this.paramValid.queryGroupAttributes(groupID, keys);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryGroupAttributes", { handle: this.handle.toString(), keys, groupID });
    }
    queryGroupAllAttributes(groupID) {
        const error = this.paramValid.queryGroupAttributes(groupID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryGroupAllAttributes", { handle: this.handle.toString(), groupID });
    }
    setGroupMemberNickname(nickname, forUserID, groupID) {
        const error = this.paramValid.setGroupMemberNickname(nickname, forUserID, groupID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setGroupMemberNickname", { handle: this.handle.toString(), nickname, forUserID, groupID });
    }
    setGroupMemberRole(role, forUserID, groupID) {
        const error = this.paramValid.setGroupMemberRole(role, forUserID, groupID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("setGroupMemberRole", { handle: this.handle.toString(), role, forUserID, groupID });
    }
    transferGroupOwner(toUserID, groupID) {
        const error = this.paramValid.validTwoID('toUserID', toUserID, 'groupID', groupID, ZIMLogTag.Group, ZIMLogAction.TransferGroupOwner);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("transferGroupOwner", { handle: this.handle.toString(), toUserID, groupID });
    }
    queryGroupMemberInfo(userID, groupID) {
        const error = this.paramValid.validTwoID('userID', userID, 'groupID', groupID, ZIMLogTag.Group, ZIMLogAction.QueryGroupMemberInfo);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryGroupMemberInfo", { handle: this.handle.toString(), userID, groupID });
    }
    inviteUsersIntoGroup(userIDs, groupID) {
        const error = this.paramValid.validGroupUserIDs(userIDs, groupID, ZIMLogAction.InviteUsersIntoGroup);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("inviteUsersIntoGroup", { handle: this.handle.toString(), userIDs, groupID });
    }
    kickGroupMembers(userIDs, groupID) {
        const error = this.paramValid.validGroupUserIDs(userIDs, groupID, ZIMLogAction.KickGroupMembers);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("kickGroupMembers", { handle: this.handle.toString(), userIDs, groupID });
    }
    queryGroupMemberList(groupID, config) {
        config = Object.assign({ count: 100, nextFlag: 0 }, config);
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.QueryGroupMemberList);
        if (error)
            return Promise.reject(error);
        if (config.count < 1)
            return Promise.resolve({ groupID, userList: [], nextFlag: config.nextFlag });
        return ZIMEngine._callMethod("queryGroupMemberList", { handle: this.handle.toString(), groupID, config });
    }
    queryGroupMemberCount(groupID) {
        const error = this.paramValid.validID('groupID', groupID, ZIMLogTag.Group, ZIMLogAction.QueryGroupMemberCount);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("queryGroupMemberCount", { handle: this.handle.toString(), groupID });
    }
    // MARK: - Call
    callInvite(invitees, config) {
        const error = this.paramValid.callInvite(invitees, config);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("callInvite", { handle: this.handle.toString(), invitees, config });
    }
    callCancel(invitees, callID, config) {
        const error = this.paramValid.callCancel(invitees, callID);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("callCancel", { handle: this.handle.toString(), invitees, callID, config });
    }
    callAccept(callID, config) {
        const error = this.paramValid.validID('callID', callID, ZIMLogTag.Call, ZIMLogAction.CallAccept);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("callAccept", { handle: this.handle.toString(), callID, config });
    }
    callReject(callID, config) {
        const error = this.paramValid.validID('callID', callID, ZIMLogTag.Call, ZIMLogAction.CallReject);
        if (error)
            return Promise.reject(error);
        return ZIMEngine._callMethod("callReject", { handle: this.handle.toString(), callID, config });
    }
}
